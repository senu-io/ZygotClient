package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;

import java.text.DecimalFormat;

public class SpeedometerMod extends HudMod {

    private final ModeSetting unit;
    private final DecimalFormat df = new DecimalFormat("0.0");

    public SpeedometerMod() {
        super("Speedometer", "Displays current player movement speed.", 10, 435, 75, 18);
        this.unit = new ModeSetting("Unit", "m/s", "m/s", "km/h");
        addSettings(unit);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderSpeed();
    }

    @Override
    public void render(float partialTicks) {
        renderSpeed();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderSpeed();
    }

    private void renderSpeed() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        double speed = 0.0;
        if (mc.thePlayer != null) {
            double dx = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double dz = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            speed = Math.sqrt(dx * dx + dz * dz) * 20.0; // blocks per second
            if (unit.is("km/h")) {
                speed *= 3.6;
            }
        }

        String text = df.format(speed) + " " + unit.getMode();
        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(70, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
