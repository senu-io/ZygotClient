package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class FpsMod extends HudMod {

    public FpsMod() {
        super("FPS", "Displays current frames per second.", 10, 10, 60, 18);
        setToggled(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderFps();
    }

    @Override
    public void render(float partialTicks) {
        renderFps();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderFps();
    }

    private void renderFps() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text = Minecraft.getDebugFPS() + " FPS";
        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(55, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
