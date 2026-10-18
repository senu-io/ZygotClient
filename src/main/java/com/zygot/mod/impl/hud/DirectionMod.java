package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class DirectionMod extends HudMod {

    private static final String[] DIRECTIONS = new String[] { "South [Z+]", "South-West", "West [X-]", "North-West", "North [Z-]", "North-East", "East [X+]", "South-East" };

    public DirectionMod() {
        super("Direction", "Displays player compass facing direction.", 10, 78, 80, 18);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderDirection(false);
    }

    @Override
    public void render(float partialTicks) {
        renderDirection(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderDirection(true);
    }

    private void renderDirection(boolean dummy) {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text = "North [Z-]";
        if (!dummy && mc.thePlayer != null) {
            float yaw = mc.thePlayer.rotationYaw;
            int dir = MathHelper.floor_double((yaw * 8.0F / 360.0F) + 0.5D) & 7;
            text = DIRECTIONS[dir];
        }

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(70, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
