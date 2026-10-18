package com.zygot.ui.hud;

import com.zygot.Zygot;
import com.zygot.mod.HudMod;
import com.zygot.theme.Theme;
import com.zygot.ui.modmenu.ZygotModMenu;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * Interactive in-game HUD Layout Customizer.
 * Supports fluid click-and-drag, magnetic snapping, mouse wheel scaling,
 * and quick access to the Mod Menu.
 */
public class ZygotHudEditor extends GuiScreen {

    private boolean snapping = true;
    private HudMod selectedMod = null;

    private boolean snappedX = false;
    private boolean snappedY = false;
    private float snapLineX = -1;
    private float snapLineY = -1;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Dark translucent overlay
        Render2DHelper.drawRect(0, 0, width, height, Theme.rgba(0, 0, 0, 110));

        ScaledResolution sr = new ScaledResolution(mc);
        float centerX = sr.getScaledWidth() / 2f;
        float centerY = sr.getScaledHeight() / 2f;
        Theme theme = Zygot.INSTANCE.themeManager.getCurrentTheme();

        // Screen Center Guides (subtle dashed lines)
        drawDottedLine(centerX, 0, centerX, height, Theme.rgba(255, 255, 255, 25));
        drawDottedLine(0, centerY, width, centerY, Theme.rgba(255, 255, 255, 25));

        // Magnetic Active Snap Lines
        if (snappedX && snapLineX >= 0) {
            Render2DHelper.drawRect(snapLineX, 0, 1, height, theme.accent);
        }
        if (snappedY && snapLineY >= 0) {
            Render2DHelper.drawRect(0, snapLineY, width, 1, theme.accent);
        }

        // Top Toolbar Panel
        float barW = 340;
        float barH = 34;
        float barX = (width - barW) / 2f;
        float barY = 14;

        Render2DHelper.drawGlassPanel(barX, barY, barW, barH, 8, theme.surface, theme.surfaceBorder, 1.2F);

        // Toolbar Buttons
        drawToolbarButton("Mod Menu", barX + 12, barY + 6, 85, 22, mouseX, mouseY, theme);
        drawToolbarButton("Reset HUD", barX + 105, barY + 6, 85, 22, mouseX, mouseY, theme);
        String snapLabel = "Snap: " + (snapping ? "ON" : "OFF");
        drawToolbarButton(snapLabel, barX + 198, barY + 6, 75, 22, mouseX, mouseY, theme);
        drawToolbarButton("Exit", barX + 281, barY + 6, 47, 22, mouseX, mouseY, theme);

        // Render all HUD modules in edit mode
        List<HudMod> hudMods = Zygot.INSTANCE.modManager.getHudMods();
        snappedX = false;
        snappedY = false;

        for (HudMod mod : hudMods) {
            if (!mod.isToggled()) continue;

            mod.renderDummy(mouseX, mouseY);
            float mw = mod.getWidth() * mod.getScale();
            float mh = mod.getHeight() * mod.getScale();

            // Dragging & Snapping calculation
            if (mod.isDragging()) {
                mod.updateDrag(mouseX, mouseY);

                if (snapping) {
                    int snapRange = 6;

                    // Snap to screen center X
                    if (Math.abs(mod.getX() + mw / 2f - centerX) < snapRange) {
                        mod.setX(centerX - mw / 2f);
                        snappedX = true;
                        snapLineX = centerX;
                    }
                    // Snap to screen center Y
                    if (Math.abs(mod.getY() + mh / 2f - centerY) < snapRange) {
                        mod.setY(centerY - mh / 2f);
                        snappedY = true;
                        snapLineY = centerY;
                    }

                    // Snap to screen left / right edges
                    if (Math.abs(mod.getX() - 10) < snapRange) {
                        mod.setX(10);
                        snappedX = true;
                        snapLineX = 10;
                    }
                    if (Math.abs(mod.getX() + mw - (width - 10)) < snapRange) {
                        mod.setX(width - 10 - mw);
                        snappedX = true;
                        snapLineX = width - 10;
                    }
                }

                // Bounds clamp
                if (mod.getX() < 0) mod.setX(0);
                if (mod.getX() + mw > width) mod.setX(width - mw);
                if (mod.getY() < 0) mod.setY(0);
                if (mod.getY() + mh > height) mod.setY(height - mh);
            }

            // Mouse wheel scaling on hovered element
            if (mod.isHovered(mouseX, mouseY)) {
                int dWheel = Mouse.getDWheel();
                if (dWheel != 0) {
                    float newScale = mod.getScale() + (dWheel > 0 ? 0.05F : -0.05F);
                    newScale = Math.max(0.5F, Math.min(2.0F, newScale));
                    if (mod.getSetting("Scale") != null) {
                        mod.getSetting("Scale").getName(); // accessed
                        ((com.zygot.mod.setting.NumberSetting) mod.getSetting("Scale")).setValue(newScale);
                    }
                }

                // Hover / Drag outline
                Render2DHelper.drawRoundedOutline(mod.getX() - 2, mod.getY() - 2, mw + 4, mh + 4, 4, 1.2F, theme.accent);

                // Module name & scale badge
                String info = mod.getName() + " (" + Math.round(mod.getScale() * 100) + "%)";
                float infoW = Zygot.INSTANCE.fontManager.light14.getStringWidth(info);
                float infoY = mod.getY() > 20 ? mod.getY() - 14 : mod.getY() + mh + 4;
                Render2DHelper.drawGlassPanel(mod.getX(), infoY, infoW + 12, 14, 3, theme.surface, theme.cardBorder, 1f);
                Zygot.INSTANCE.fontManager.light14.drawString(info, mod.getX() + 6, infoY + 2, theme.textPrimary);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawToolbarButton(String text, float bx, float by, float bw, float bh, int mx, int my, Theme theme) {
        boolean hovered = mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
        int bg = hovered ? theme.accent : theme.cardBg;
        int border = hovered ? theme.accentHover : theme.cardBorder;

        Render2DHelper.drawGlassPanel(bx, by, bw, bh, 5, bg, border, 1f);
        Zygot.INSTANCE.fontManager.bold16.drawCenteredString(text, bx + bw / 2f, by + 4, hovered ? 0xFFFFFFFF : theme.textPrimary);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float barW = 340;
        float barH = 34;
        float barX = (width - barW) / 2f;
        float barY = 14;

        // Toolbar Button Clicks
        if (mouseY >= barY + 6 && mouseY <= barY + 28) {
            if (mouseX >= barX + 12 && mouseX <= barX + 97) {
                mc.displayGuiScreen(new ZygotModMenu(this));
                return;
            } else if (mouseX >= barX + 105 && mouseX <= barX + 190) {
                resetHudPositions();
                return;
            } else if (mouseX >= barX + 198 && mouseX <= barX + 273) {
                this.snapping = !this.snapping;
                return;
            } else if (mouseX >= barX + 281 && mouseX <= barX + 328) {
                mc.displayGuiScreen(null);
                return;
            }
        }

        // Module Drag start
        List<HudMod> hudMods = Zygot.INSTANCE.modManager.getHudMods();
        for (HudMod mod : hudMods) {
            if (mod.isToggled() && mod.isHovered(mouseX, mouseY)) {
                mod.startDragging(mouseX, mouseY);
                selectedMod = mod;
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (selectedMod != null) {
            selectedMod.stopDragging();
            selectedMod = null;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private void resetHudPositions() {
        float defaultY = 10;
        for (HudMod mod : Zygot.INSTANCE.modManager.getHudMods()) {
            mod.setX(10);
            mod.setY(defaultY);
            defaultY += 24;
        }
    }

    private void drawDottedLine(float x1, float y1, float x2, float y2, int color) {
        Render2DHelper.drawRect(x1, y1, Math.max(1, Math.abs(x2 - x1)), Math.max(1, Math.abs(y2 - y1)), color);
    }
}
