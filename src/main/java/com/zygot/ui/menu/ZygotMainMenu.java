package com.zygot.ui.menu;

import com.zygot.Zygot;
import com.zygot.branding.BackgroundMode;
import com.zygot.branding.ClientBranding;
import com.zygot.theme.Theme;
import com.zygot.ui.cosmetics.ZygotCosmeticsScreen;
import com.zygot.ui.modmenu.ZygotModMenu;
import com.zygot.util.animation.Animate;
import com.zygot.util.animation.Easing;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Magnificent Feather/Lunar Client style Main Menu for Zygot Client.
 * Supports switchable animated particle mesh vs. static wallpaper image,
 * translucent pill buttons, glowing accent highlights, and community links.
 */
public class ZygotMainMenu extends GuiScreen {

    private final List<MenuButton> buttons = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private float time = 0;

    private static final ResourceLocation STATIC_BG = new ResourceLocation("zygot", "textures/main_menu.png");
    private static final ResourceLocation LOGO_WHITE = new ResourceLocation("zygot", "textures/logo_white_nobg.png");
    private static final ResourceLocation LOGO_DARK = new ResourceLocation("zygot", "textures/logo_dark_nobg.png");

    @Override
    public void initGui() {
        buttons.clear();

        int centerX = width / 2;
        int totalBlockH = 234;
        int startY = (int) Math.max(25, (height - totalBlockH - 24) / 2f);
        int btnStartY = startY + 104;

        int btnW = 260;
        int halfW = 126;
        int btnH = 25;
        int gap = 6;

        // Row 1: Singleplayer & Multiplayer
        buttons.add(new MenuButton(1, "Singleplayer", centerX - 130, btnStartY, halfW, btnH));
        buttons.add(new MenuButton(2, "Multiplayer", centerX + 4, btnStartY, halfW, btnH));

        // Row 2: Zygot Mods & HUD (Full Width Accent)
        buttons.add(new MenuButton(3, "Zygot Mods & HUD", centerX - 130, btnStartY + (btnH + gap), btnW, btnH, true));

        // Row 3: 3D Cosmetics Wardrobe (Full Width)
        buttons.add(new MenuButton(4, "3D Cosmetics Wardrobe", centerX - 130, btnStartY + (btnH + gap) * 2, btnW, btnH));

        // Row 4: Options & Quit Game
        buttons.add(new MenuButton(5, "Options", centerX - 130, btnStartY + (btnH + gap) * 3, halfW, btnH));
        buttons.add(new MenuButton(6, "Quit Game", centerX + 4, btnStartY + (btnH + gap) * 3, halfW, btnH));

        // Initialize particles if animated mode is active
        particles.clear();
        for (int i = 0; i < 70; i++) {
            particles.add(new Particle(random.nextFloat() * width, random.nextFloat() * height,
                    (random.nextFloat() - 0.5F) * 0.8F, (random.nextFloat() - 0.5F) * 0.8F,
                    random.nextFloat() * 2.5F + 1.5F));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        time += partialTicks * 0.05F;
        Theme theme = Zygot.INSTANCE.themeManager.getCurrentTheme();

        // 1. Draw Background according to configured BackgroundMode
        if (ClientBranding.DEFAULT_BACKGROUND_MODE == BackgroundMode.STATIC_IMAGE) {
            // High-resolution static wallpaper
            Render2DHelper.drawTexturedRect(0, 0, width, height, STATIC_BG);
            Render2DHelper.drawRect(0, 0, width, height, Theme.rgba(0, 0, 0, 70)); // Soft darkening overlay
        } else if (ClientBranding.DEFAULT_BACKGROUND_MODE == BackgroundMode.FLOWING_GRADIENT) {
            // Smooth gradient wave
            int col1 = theme.background;
            int col2 = theme.accentGlow;
            Render2DHelper.drawGradientRect(0, 0, width, height, col1, col2, false);
        } else {
            // Animated Particles Mesh
            Render2DHelper.drawRect(0, 0, width, height, theme.background);
            renderParticleMesh(mouseX, mouseY, theme);
        }

        // Top-right Background Mode Toggle Pill
        String modeText = (ClientBranding.DEFAULT_BACKGROUND_MODE == BackgroundMode.STATIC_IMAGE) ? "Mode: Wallpaper" : "Mode: Particles";
        float modeW = Zygot.INSTANCE.fontManager.regular14.getStringWidth(modeText) + 20;
        float modeX = width - modeW - 14;
        float modeY = 14;
        boolean modeHovered = mouseX >= modeX && mouseX <= modeX + modeW && mouseY >= modeY && mouseY <= modeY + 22;
        int modeBg = modeHovered ? theme.surfaceHover : theme.surface;
        int modeBorder = modeHovered ? theme.accentHover : theme.cardBorder;
        Render2DHelper.drawGlassPanel(modeX, modeY, modeW, 22, 6, modeBg, modeBorder, 1.0F);
        Zygot.INSTANCE.fontManager.regular14.drawCenteredString(modeText, modeX + modeW / 2f, modeY + 5, theme.textPrimary);

        // 2. Client Logo & Branding Header
        float centerX = width / 2f;
        int totalBlockH = 234;
        float startY = Math.max(25, (height - totalBlockH - 24) / 2f);

        // Render logo icon with appropriate contrast
        ResourceLocation logo = theme.isDark() ? LOGO_WHITE : LOGO_DARK;
        Render2DHelper.drawTexturedRect(centerX - 24, startY, 48, 48, logo);

        // Title and Tagline
        Zygot.INSTANCE.fontManager.bold32.drawCenteredString(ClientBranding.NAME, centerX, startY + 52, theme.textPrimary);
        Zygot.INSTANCE.fontManager.regular14.drawCenteredString(ClientBranding.TAGLINE, centerX, startY + 78, theme.textSecondary);

        // 3. Render Buttons
        for (MenuButton btn : buttons) {
            btn.draw(mouseX, mouseY, theme);
        }

        // 4. Footer Attribution & Social Buttons
        float footerY = height - 20;
        Zygot.INSTANCE.fontManager.regular14.drawString(ClientBranding.getAttributionText(), 14, footerY, theme.textMuted);

        // Social links on bottom-right
        float linkX = width - 180;
        Zygot.INSTANCE.fontManager.bold16.drawString("Discord", linkX, footerY, theme.accent);
        Zygot.INSTANCE.fontManager.bold16.drawString("Website", linkX + 60, footerY, theme.accent);
        Zygot.INSTANCE.fontManager.bold16.drawString("GitHub", linkX + 125, footerY, theme.accent);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderParticleMesh(int mouseX, int mouseY, Theme theme) {
        // Update and draw particles with connecting web lines
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.x += p.vx;
            p.y += p.vy;

            if (p.x < 0) p.x = width;
            if (p.x > width) p.x = 0;
            if (p.y < 0) p.y = height;
            if (p.y > height) p.y = 0;

            Render2DHelper.drawCircle(p.x, p.y, p.radius, Theme.withAlpha(theme.accent, 140));

            // Connect lines between nearby particles
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p2 = particles.get(j);
                float dx = p.x - p2.x;
                float dy = p.y - p2.y;
                float distSq = dx * dx + dy * dy;
                if (distSq < 7000) {
                    float alpha = (1.0F - distSq / 7000F) * 60;
                    drawLine(p.x, p.y, p2.x, p2.y, Theme.withAlpha(theme.accent, (int) alpha));
                }
            }
        }
    }

    private void drawLine(float x1, float y1, float x2, float y2, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.0F);
        Render2DHelper.color(color);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Top-right Background Mode Switcher Click
        String modeText = (ClientBranding.DEFAULT_BACKGROUND_MODE == BackgroundMode.STATIC_IMAGE) ? "Mode: Wallpaper" : "Mode: Particles";
        float modeW = Zygot.INSTANCE.fontManager.regular14.getStringWidth(modeText) + 20;
        float modeX = width - modeW - 14;
        float modeY = 14;
        if (mouseX >= modeX && mouseX <= modeX + modeW && mouseY >= modeY && mouseY <= modeY + 22) {
            if (ClientBranding.DEFAULT_BACKGROUND_MODE == BackgroundMode.STATIC_IMAGE) {
                ClientBranding.DEFAULT_BACKGROUND_MODE = BackgroundMode.ANIMATED_PARTICLES;
            } else {
                ClientBranding.DEFAULT_BACKGROUND_MODE = BackgroundMode.STATIC_IMAGE;
            }
            return;
        }

        for (MenuButton btn : buttons) {
            if (btn.isHovered(mouseX, mouseY)) {
                btn.onClick();
                return;
            }
        }

        // Footer Social Links Click
        float footerY = height - 20;
        if (mouseY >= footerY - 4 && mouseY <= footerY + 16) {
            float linkX = width - 180;
            if (mouseX >= linkX && mouseX <= linkX + 50) {
                openUrl(ClientBranding.DISCORD_URL);
            } else if (mouseX >= linkX + 60 && mouseX <= linkX + 115) {
                openUrl(ClientBranding.WEBSITE_URL);
            } else if (mouseX >= linkX + 125 && mouseX <= linkX + 175) {
                openUrl(ClientBranding.GITHUB_URL);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception ignored) {}
    }

    private static class Particle {
        float x, y, vx, vy, radius;

        Particle(float x, float y, float vx, float vy, float radius) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = radius;
        }
    }

    private class MenuButton {
        final int id;
        final String text;
        final int x, y, w, h;
        final boolean highlight;
        final Animate hoverAnim = new Animate().setEase(Easing.CUBIC_OUT).setMin(0).setMax(1).setSpeed(120);

        MenuButton(int id, String text, int x, int y, int w, int h) {
            this(id, text, x, y, w, h, false);
        }

        MenuButton(int id, String text, int x, int y, int w, int h, boolean highlight) {
            this.id = id;
            this.text = text;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.highlight = highlight;
        }

        boolean isHovered(int mx, int my) {
            return mx >= x && mx <= x + w && my >= y && my <= y + h;
        }

        void draw(int mx, int my, Theme theme) {
            boolean hovered = isHovered(mx, my);
            hoverAnim.setReversed(!hovered).update();
            float val = hoverAnim.getValue();

            int baseBg = highlight ? Theme.withAlpha(theme.accent, 60) : theme.surface;
            int hoverBg = highlight ? theme.accent : theme.surfaceHover;
            int bg = Theme.lerpColor(baseBg, hoverBg, val);

            int baseBorder = highlight ? theme.accent : theme.cardBorder;
            int hoverBorder = theme.accentHover;
            int border = Theme.lerpColor(baseBorder, hoverBorder, val);

            float extraW = val * 4F;
            Render2DHelper.drawGlassPanel(x - extraW / 2f, y, w + extraW, h, 6, bg, border, 1.2F);

            int textCol = highlight && hovered ? 0xFFFFFFFF : Theme.lerpColor(theme.textPrimary, 0xFFFFFFFF, val);
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(text, x + w / 2f, y + 6, textCol);
        }

        void onClick() {
            switch (id) {
                case 1:
                    mc.displayGuiScreen(new GuiSelectWorld(ZygotMainMenu.this));
                    break;
                case 2:
                    mc.displayGuiScreen(new GuiMultiplayer(ZygotMainMenu.this));
                    break;
                case 3:
                    mc.displayGuiScreen(new ZygotModMenu(ZygotMainMenu.this));
                    break;
                case 4:
                    mc.displayGuiScreen(new ZygotCosmeticsScreen(ZygotMainMenu.this));
                    break;
                case 5:
                    mc.displayGuiScreen(new GuiOptions(ZygotMainMenu.this, mc.gameSettings));
                    break;
                case 6:
                    mc.shutdown();
                    break;
            }
        }
    }
}
