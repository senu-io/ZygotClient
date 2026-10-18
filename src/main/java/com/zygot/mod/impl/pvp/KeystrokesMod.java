package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeystrokesMod extends HudMod {

    private final BooleanSetting showMouse;
    private final BooleanSetting showSpace;
    private final BooleanSetting showCps;
    private final ColorSetting pressedColor;

    public KeystrokesMod() {
        super("Keystrokes", "Displays keyboard and mouse inputs with press animations.", 10, 100, 74, 74);

        this.showMouse = new BooleanSetting("Show Mouse", true);
        this.showSpace = new BooleanSetting("Show Space", true);
        this.showCps = new BooleanSetting("Show CPS", true);
        this.pressedColor = new ColorSetting("Pressed Color", Theme.rgba(139, 92, 246, 180));

        addSettings(showMouse, showSpace, showCps, pressedColor);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderKeys(false, 0, 0);
    }

    @Override
    public void render(float partialTicks) {
        renderKeys(false, 0, 0);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderKeys(true, mouseX, mouseY);
    }

    private void renderKeys(boolean dummy, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        boolean wPressed = dummy || mc.gameSettings.keyBindForward.isKeyDown();
        boolean aPressed = dummy || mc.gameSettings.keyBindLeft.isKeyDown();
        boolean sPressed = dummy || mc.gameSettings.keyBindBack.isKeyDown();
        boolean dPressed = dummy || mc.gameSettings.keyBindRight.isKeyDown();
        boolean lmbPressed = dummy || mc.gameSettings.keyBindAttack.isKeyDown();
        boolean rmbPressed = dummy || mc.gameSettings.keyBindUseItem.isKeyDown();
        boolean spacePressed = dummy || mc.gameSettings.keyBindJump.isKeyDown();

        // Key W
        drawKey(25, 0, 24, 24, "W", wPressed);
        // Keys A, S, D
        drawKey(0, 25, 24, 24, "A", aPressed);
        drawKey(25, 25, 24, 24, "S", sPressed);
        drawKey(50, 25, 24, 24, "D", dPressed);

        float currentH = 50;

        // Mouse buttons
        if (showMouse.isEnabled()) {
            String lmbText = "LMB" + (showCps.isEnabled() ? "\n" + CpsMod.getLeftCps() : "");
            String rmbText = "RMB" + (showCps.isEnabled() ? "\n" + CpsMod.getRightCps() : "");
            drawKey(0, 50, 36, 24, lmbText, lmbPressed);
            drawKey(38, 50, 36, 24, rmbText, rmbPressed);
            currentH = 75;
        }

        // Space bar
        if (showSpace.isEnabled()) {
            float spaceY = showMouse.isEnabled() ? 75 : 50;
            drawKey(0, spaceY, 74, 14, "\u2014\u2014\u2014", spacePressed);
            currentH = spaceY + 14;
        }

        this.width = 74;
        this.height = currentH;

        GlStateManager.popMatrix();
    }

    private void drawKey(float kx, float ky, float kw, float kh, String text, boolean pressed) {
        int bg = pressed ? pressedColor.getColor() : bgColorSetting.getColor();
        int border = borderSetting.isEnabled() ? borderColorSetting.getColor() : 0;
        float radius = roundedSetting.isEnabled() ? 4f : 0f;

        Render2DHelper.drawGlassPanel(kx, ky, kw, kh, radius, bg, border, borderSetting.isEnabled() ? 1f : 0f);

        int textCol = getTextColor();
        if (text.contains("\n")) {
            String[] lines = text.split("\n");
            Zygot.INSTANCE.fontManager.regular14.drawCenteredString(lines[0], kx + kw / 2f, ky + 4, textCol);
            Zygot.INSTANCE.fontManager.light14.drawCenteredString(lines[1] + " CPS", kx + kw / 2f, ky + 13, Theme.rgba(200, 200, 220, 255));
        } else {
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(text, kx + kw / 2f, ky + (kh / 2f) - 4, textCol);
        }
    }
}
