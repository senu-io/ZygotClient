package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.renderer.GlStateManager;

public class DayCounterMod extends HudMod {

    public DayCounterMod() {
        super("Day Counter", "Displays current in-game Minecraft world day.", 10, 485, 60, 18);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderDay();
    }

    @Override
    public void render(float partialTicks) {
        renderDay();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderDay();
    }

    private void renderDay() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        long time = mc.theWorld != null ? mc.theWorld.getWorldTime() : 24000;
        long day = time / 24000;
        String text = "Day " + day;

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(55, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
