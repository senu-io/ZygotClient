package com.zygot.ui.modmenu;

import com.zygot.Zygot;
import com.zygot.branding.ClientBranding;
import com.zygot.mod.Category;
import com.zygot.mod.HudMod;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.*;
import com.zygot.theme.Theme;
import com.zygot.ui.cosmetics.ZygotCosmeticsScreen;
import com.zygot.ui.hud.ZygotHudEditor;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern Feather / Lunar Client style Mod Menu and Configuration Dashboard.
 * Features translucent frosted glass panels, anti-aliased rounded corners,
 * fluid toggle switches, smooth sliders, live search, and theme switching.
 */
public class ZygotModMenu extends GuiScreen {

    private static final ResourceLocation LOGO_WHITE = new ResourceLocation("zygot/textures/logo_white_nobg.png");
    private static final ResourceLocation LOGO_DARK = new ResourceLocation("zygot/textures/logo_dark_nobg.png");

    private final GuiScreen parent;
    private Category selectedCategory = null; // null means "All"
    private boolean showingThemes = false;
    private String searchQuery = "";
    private float scrollOffset = 0;
    private float maxScroll = 0;
    private Mod expandedMod = null;

    private NumberSetting draggingSlider = null;
    private float draggingSliderX = 0;
    private float draggingSliderW = 75;
    private KeybindSetting listeningKeybind = null;

    public ZygotModMenu(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(mc);
        float pw = sr.getScaledWidth() * 0.88F;
        float ph = sr.getScaledHeight() * 0.88F;
        float px = (sr.getScaledWidth() - pw) / 2F;
        float py = (sr.getScaledHeight() - ph) / 2F;

        Theme theme = Zygot.INSTANCE.themeManager.getCurrentTheme();

        // 1. Main Frosted Glass Dashboard Panel
        Render2DHelper.drawGlassPanel(px, py, pw, ph, 10, theme.background, theme.surfaceBorder, 1.5F);

        // 2. Left Sidebar (Categories)
        float sideW = 140;
        Render2DHelper.drawGlassPanel(px, py, sideW, ph, 10, theme.backgroundSecondary, theme.cardBorder, 1f);

        // Branding Logo & Text
        ResourceLocation logo = theme.isDark() ? LOGO_WHITE : LOGO_DARK;
        Render2DHelper.drawTexturedRect(px + 12, py + 18, 26, 26, logo);
        Zygot.INSTANCE.fontManager.bold20.drawString(ClientBranding.NAME, px + 44, py + 17, theme.accent);
        Zygot.INSTANCE.fontManager.light14.drawString("Settings & Mods", px + 44, py + 34, theme.textMuted);

        // Category Buttons
        float catY = py + 65;
        drawCategoryTab("All Mods", null, px + 10, catY, sideW - 20, 24, mouseX, mouseY, theme, !showingThemes && selectedCategory == null);
        catY += 28;

        for (Category cat : Category.values()) {
            if (cat == Category.COSMETICS) continue;
            drawCategoryTab(cat.getDisplayName(), cat, px + 10, catY, sideW - 20, 24, mouseX, mouseY, theme, !showingThemes && selectedCategory == cat);
            catY += 28;
        }

        // Cosmetics Tab button
        drawCategoryTab("3D Cosmetics", Category.COSMETICS, px + 10, catY, sideW - 20, 24, mouseX, mouseY, theme, false);
        catY += 28;

        // Themes Tab button
        drawCategoryTab("Themes & Colors", null, px + 10, catY, sideW - 20, 24, mouseX, mouseY, theme, showingThemes);

        // Quick HUD Editor button at bottom of sidebar
        float hudBtnY = py + ph - 38;
        Render2DHelper.drawGlassPanel(px + 10, hudBtnY, sideW - 20, 26, 6, theme.accent, theme.accentHover, 1f);
        Zygot.INSTANCE.fontManager.bold16.drawCenteredString("Open HUD Editor", px + sideW / 2f, hudBtnY + 6, 0xFFFFFFFF);

        // 3. Right Content Area (Search Bar + Mod Cards)
        float contentX = px + sideW + 16;
        float contentY = py + 16;
        float contentW = pw - sideW - 32;
        float contentH = ph - 32;

        // Search Bar
        float searchW = Math.min(220, contentW);
        float searchH = 24;
        Render2DHelper.drawGlassPanel(contentX, contentY, searchW, searchH, 6, theme.surface, theme.cardBorder, 1f);
        String searchDisplay = searchQuery.isEmpty() ? "Search mods..." : (searchQuery + (System.currentTimeMillis() % 1000 > 500 ? "|" : ""));
        int searchCol = searchQuery.isEmpty() ? theme.textMuted : theme.textPrimary;
        Zygot.INSTANCE.fontManager.regular14.drawString(searchDisplay, contentX + 10, contentY + 5, searchCol);

        // Mod Cards Container with Scissor Clipping
        float listY = contentY + searchH + 12;
        float listH = contentH - searchH - 12;

        Render2DHelper.startScissor((int) contentX, (int) listY, (int) contentW, (int) listH);

        if (showingThemes) {
            drawThemesPanel(contentX, listY - scrollOffset, contentW, mouseX, mouseY, theme);
        } else {
            drawModsList(contentX, listY - scrollOffset, contentW, mouseX, mouseY, theme);
        }

        Render2DHelper.stopScissor();

        // Mouse Wheel Scrolling
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (dWheel > 0 ? 30 : -30)));
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCategoryTab(String title, Category cat, float bx, float by, float bw, float bh, int mx, int my, Theme theme, boolean active) {
        boolean hovered = mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
        int bg = active ? theme.accent : (hovered ? theme.surfaceHover : theme.surface);
        int border = active ? theme.accentHover : theme.cardBorder;

        Render2DHelper.drawGlassPanel(bx, by, bw, bh, 6, bg, border, 1f);
        int textCol = active ? 0xFFFFFFFF : (hovered ? theme.textPrimary : theme.textSecondary);
        Zygot.INSTANCE.fontManager.bold16.drawString(title, bx + 12, by + 5, textCol);
    }

    private void drawModsList(float cx, float cy, float cw, int mx, int my, Theme theme) {
        List<Mod> all = Zygot.INSTANCE.modManager.getMods();
        List<Mod> filtered = new ArrayList<>();

        for (Mod m : all) {
            if (selectedCategory != null && m.getCategory() != selectedCategory) continue;
            if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;
            filtered.add(m);
        }

        float cardY = cy;
        float cardW = (cw - 16) / 2f;
        int col = 0;

        for (Mod m : filtered) {
            float cardX = cx + col * (cardW + 12);
            boolean isExpanded = (expandedMod == m);
            float cardH = isExpanded ? 70 + (m.getSettings().size() * 24) : 64;

            boolean hovered = mx >= cardX && mx <= cardX + cardW && my >= cardY && my <= cardY + cardH;

            // Card Panel
            int cardBg = m.isToggled() ? Theme.withAlpha(theme.accent, 25) : theme.cardBg;
            int cardBorder = m.isToggled() ? theme.accent : (hovered ? theme.accentHover : theme.cardBorder);
            Render2DHelper.drawGlassPanel(cardX, cardY, cardW, cardH, 6, cardBg, cardBorder, 1f);

            // Mod Name & Category Tag
            Zygot.INSTANCE.fontManager.bold16.drawString(m.getName(), cardX + 12, cardY + 10, theme.textPrimary);
            Zygot.INSTANCE.fontManager.light14.drawString(m.getCategory().getDisplayName(), cardX + 12, cardY + 26, theme.accent);
            Zygot.INSTANCE.fontManager.regular14.drawString(m.getDescription(), cardX + 12, cardY + 42, theme.textMuted);

            // Toggle Switch
            float swW = 34;
            float swH = 16;
            float swX = cardX + cardW - swW - 12;
            float swY = cardY + 12;
            drawModernToggle(swX, swY, swW, swH, m.isToggled(), theme);

            // Expand Settings Gear Icon
            if (!m.getSettings().isEmpty()) {
                float gearX = swX - 22;
                float gearY = cardY + 12;
                int gearCol = isExpanded ? theme.accent : theme.textMuted;
                Zygot.INSTANCE.fontManager.bold16.drawString("\u2699", gearX, gearY, gearCol);
            }

            // Draw Settings if expanded
            if (isExpanded) {
                float sy = cardY + 62;
                for (Setting s : m.getSettings()) {
                    drawSettingRow(s, cardX + 12, sy, cardW - 24, mx, my, theme);
                    sy += 24;
                }
            }

            col++;
            if (col >= 2) {
                col = 0;
                cardY += cardH + 10;
            }
        }

        this.maxScroll = Math.max(0, (cardY - cy) - 200);
    }

    private void drawModernToggle(float sx, float sy, float sw, float sh, boolean enabled, Theme theme) {
        int trackBg = enabled ? theme.accent : Theme.rgba(40, 40, 52, 255);
        int trackBorder = enabled ? theme.accentHover : Theme.rgba(60, 60, 75, 255);
        Render2DHelper.drawGlassPanel(sx, sy, sw, sh, sh / 2f, trackBg, trackBorder, 1f);

        float knobRadius = sh / 2f - 2f;
        float knobX = enabled ? (sx + sw - knobRadius - 2f) : (sx + knobRadius + 2f);
        Render2DHelper.drawCircle(knobX, sy + sh / 2f, knobRadius, 0xFFFFFFFF);
    }

    private void drawSettingRow(Setting s, float rx, float ry, float rw, int mx, int my, Theme theme) {
        Render2DHelper.drawGlassPanel(rx, ry, rw, 22, 4, theme.surface, theme.cardBorder, 1f);
        Zygot.INSTANCE.fontManager.regular14.drawString(s.getName(), rx + 8, ry + 5, theme.textPrimary);

        if (s instanceof BooleanSetting) {
            BooleanSetting bs = (BooleanSetting) s;
            drawModernToggle(rx + rw - 36, ry + 3, 28, 14, bs.isEnabled(), theme);
        } else if (s instanceof NumberSetting) {
            NumberSetting ns = (NumberSetting) s;
            float sliderW = 75;
            float sliderX = rx + rw - sliderW - 8;
            float sliderY = ry + 8;
            float progress = (float) ((ns.getValue() - ns.getMin()) / (ns.getMax() - ns.getMin()));

            Render2DHelper.drawRoundedRect(sliderX, sliderY, sliderW, 4, 2, Theme.rgba(40, 40, 52, 255));
            Render2DHelper.drawRoundedRect(sliderX, sliderY, sliderW * progress, 4, 2, theme.accent);
            Render2DHelper.drawCircle(sliderX + sliderW * progress, sliderY + 2, 4, 0xFFFFFFFF);

            String valStr = String.format("%.1f", ns.getValue());
            float vw = Zygot.INSTANCE.fontManager.light14.getStringWidth(valStr);
            Zygot.INSTANCE.fontManager.light14.drawString(valStr, sliderX - vw - 6, ry + 4, theme.textSecondary);
        } else if (s instanceof ModeSetting) {
            ModeSetting ms = (ModeSetting) s;
            String modeStr = ms.getMode();
            float mw = Zygot.INSTANCE.fontManager.bold16.getStringWidth(modeStr) + 16;
            Render2DHelper.drawGlassPanel(rx + rw - mw - 8, ry + 2, mw, 18, 4, theme.surfaceHover, theme.cardBorder, 1f);
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(modeStr, rx + rw - mw / 2f - 8, ry + 4, theme.accent);
        } else if (s instanceof KeybindSetting) {
            KeybindSetting ks = (KeybindSetting) s;
            String keyStr = (listeningKeybind == ks) ? "Press Key..." : ks.getKeyName();
            float kw = Zygot.INSTANCE.fontManager.bold16.getStringWidth(keyStr) + 16;
            Render2DHelper.drawGlassPanel(rx + rw - kw - 8, ry + 2, kw, 18, 4, theme.surfaceHover, theme.cardBorder, 1f);
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(keyStr, rx + rw - kw / 2f - 8, ry + 4, theme.accent);
        }
    }

    private void drawThemesPanel(float cx, float cy, float cw, int mx, int my, Theme currentTheme) {
        Zygot.INSTANCE.fontManager.bold20.drawString("Available Visual Themes", cx, cy + 8, currentTheme.textPrimary);
        Zygot.INSTANCE.fontManager.regular14.drawString("One-click switch client color palette, gradients, and accents", cx, cy + 28, currentTheme.textSecondary);

        float ty = cy + 50;
        List<Theme> themes = Zygot.INSTANCE.themeManager.getThemes();

        for (Theme t : themes) {
            boolean active = (t == currentTheme);
            boolean hovered = mx >= cx && mx <= cx + cw - 20 && my >= ty && my <= ty + 42;

            int cardBg = active ? Theme.withAlpha(t.accent, 40) : (hovered ? currentTheme.surfaceHover : currentTheme.cardBg);
            int cardBorder = active ? t.accent : currentTheme.cardBorder;

            Render2DHelper.drawGlassPanel(cx, ty, cw - 20, 42, 6, cardBg, cardBorder, 1.2F);

            // Palette preview colored circles
            Render2DHelper.drawCircle(cx + 20, ty + 21, 8, t.accent);
            Render2DHelper.drawCircle(cx + 42, ty + 21, 8, t.background);
            Render2DHelper.drawCircle(cx + 64, ty + 21, 8, t.surface);

            Zygot.INSTANCE.fontManager.bold16.drawString(t.name, cx + 84, ty + 12, currentTheme.textPrimary);
            if (active) {
                float bw = 70;
                Render2DHelper.drawGlassPanel(cx + cw - bw - 35, ty + 10, bw, 22, 4, t.accent, t.accentHover, 1f);
                Zygot.INSTANCE.fontManager.bold16.drawCenteredString("ACTIVE", cx + cw - bw / 2f - 35, ty + 14, 0xFFFFFFFF);
            }

            ty += 52;
        }

        this.maxScroll = Math.max(0, (ty - cy) - 200);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        float pw = sr.getScaledWidth() * 0.88F;
        float ph = sr.getScaledHeight() * 0.88F;
        float px = (sr.getScaledWidth() - pw) / 2F;
        float py = (sr.getScaledHeight() - ph) / 2F;
        float sideW = 140;

        // Category clicks
        float catY = py + 65;
        if (mouseX >= px + 10 && mouseX <= px + sideW - 10) {
            if (mouseY >= catY && mouseY <= catY + 24) {
                selectedCategory = null;
                showingThemes = false;
                scrollOffset = 0;
                return;
            }
            catY += 28;

            for (Category cat : Category.values()) {
                if (cat == Category.COSMETICS) continue;
                if (mouseY >= catY && mouseY <= catY + 24) {
                    selectedCategory = cat;
                    showingThemes = false;
                    scrollOffset = 0;
                    return;
                }
                catY += 28;
            }

            // Cosmetics
            if (mouseY >= catY && mouseY <= catY + 24) {
                mc.displayGuiScreen(new ZygotCosmeticsScreen(this));
                return;
            }
            catY += 28;

            // Themes
            if (mouseY >= catY && mouseY <= catY + 24) {
                showingThemes = true;
                scrollOffset = 0;
                return;
            }

            // HUD Editor button click
            float hudBtnY = py + ph - 38;
            if (mouseY >= hudBtnY && mouseY <= hudBtnY + 26) {
                mc.displayGuiScreen(new ZygotHudEditor());
                return;
            }
        }

        // Themes Selection Click
        if (showingThemes) {
            float contentX = px + sideW + 16;
            float listY = py + 52 - scrollOffset;
            float ty = listY + 50;
            for (Theme t : Zygot.INSTANCE.themeManager.getThemes()) {
                if (mouseX >= contentX && mouseX <= contentX + (pw - sideW - 52) && mouseY >= ty && mouseY <= ty + 42) {
                    Zygot.INSTANCE.themeManager.setCurrentTheme(t);
                    return;
                }
                ty += 52;
            }
            return;
        }

        // Mod Cards Click
        float contentX = px + sideW + 16;
        float listY = py + 52 - scrollOffset;
        float cw = pw - sideW - 32;
        float cardW = (cw - 16) / 2f;
        float cardY = listY;
        int col = 0;

        List<Mod> all = Zygot.INSTANCE.modManager.getMods();
        for (Mod m : all) {
            if (selectedCategory != null && m.getCategory() != selectedCategory) continue;
            if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;

            float cardX = contentX + col * (cardW + 12);
            boolean isExpanded = (expandedMod == m);
            float cardH = isExpanded ? 70 + (m.getSettings().size() * 24) : 64;

            // Toggle switch click
            float swW = 34;
            float swH = 16;
            float swX = cardX + cardW - swW - 12;
            float swY = cardY + 12;
            if (mouseX >= swX && mouseX <= swX + swW && mouseY >= swY && mouseY <= swY + swH) {
                m.toggle();
                return;
            }

            // Gear icon click
            if (!m.getSettings().isEmpty()) {
                float gearX = swX - 22;
                if (mouseX >= gearX && mouseX <= gearX + 18 && mouseY >= swY && mouseY <= swY + 18) {
                    expandedMod = (expandedMod == m) ? null : m;
                    return;
                }
            }

            // Settings clicks if expanded
            if (isExpanded) {
                float sy = cardY + 62;
                for (Setting s : m.getSettings()) {
                    if (mouseX >= cardX + 12 && mouseX <= cardX + cardW - 12 && mouseY >= sy && mouseY <= sy + 22) {
                        if (s instanceof BooleanSetting) {
                            ((BooleanSetting) s).toggle();
                            return;
                        } else if (s instanceof ModeSetting) {
                            ((ModeSetting) s).cycle();
                            return;
                        } else if (s instanceof KeybindSetting) {
                            listeningKeybind = (KeybindSetting) s;
                            return;
                        } else if (s instanceof NumberSetting) {
                            NumberSetting ns = (NumberSetting) s;
                            float rx = cardX + 12;
                            float rw = cardW - 24;
                            float sliderW = 75;
                            float sliderX = rx + rw - sliderW - 8;
                            if (mouseX >= sliderX - 6 && mouseX <= sliderX + sliderW + 6 && mouseY >= sy && mouseY <= sy + 22) {
                                draggingSlider = ns;
                                draggingSliderX = sliderX;
                                draggingSliderW = sliderW;
                                updateSliderValue(ns, draggingSliderX, draggingSliderW, mouseX);
                                return;
                            }
                        }
                    }
                    sy += 24;
                }
            }

            col++;
            if (col >= 2) {
                col = 0;
                cardY += cardH + 10;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (draggingSlider != null) {
            updateSliderValue(draggingSlider, draggingSliderX, draggingSliderW, mouseX);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    private void updateSliderValue(NumberSetting ns, float sx, float sw, int mx) {
        float percent = Math.max(0.0F, Math.min(1.0F, (mx - sx) / sw));
        double val = ns.getMin() + percent * (ns.getMax() - ns.getMin());
        double step = ns.getIncrement();
        if (step > 0) {
            val = Math.round(val / step) * step;
        }
        ns.setValue(val);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listeningKeybind != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listeningKeybind.setKeyCode(0);
            } else {
                listeningKeybind.setKeyCode(keyCode);
            }
            listeningKeybind = null;
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
            return;
        }

        // Search bar typing
        if (keyCode == Keyboard.KEY_BACK) {
            if (!searchQuery.isEmpty()) {
                searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            }
        } else if (keyCode == Keyboard.KEY_DELETE) {
            searchQuery = "";
        } else if (!Character.isISOControl(typedChar)) {
            if (searchQuery.length() < 24) {
                searchQuery += typedChar;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }
}
