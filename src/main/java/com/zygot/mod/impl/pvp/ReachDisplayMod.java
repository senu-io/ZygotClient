package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.AttackEntityEvent;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.renderer.GlStateManager;

import java.text.DecimalFormat;

public class ReachDisplayMod extends HudMod {

    private double reach = 0.0;
    private long lastAttackTime = 0;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public ReachDisplayMod() {
        super("Reach Display", "Shows attack distance in blocks on target hit.", 10, 210, 65, 18);
    }

    @EventTarget
    public void onAttack(AttackEntityEvent e) {
        if (mc.thePlayer != null && e.getTarget() != null) {
            double dist = mc.thePlayer.getDistanceToEntity(e.getTarget());
            this.reach = dist;
            this.lastAttackTime = System.currentTimeMillis();
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderReach();
    }

    @Override
    public void render(float partialTicks) {
        renderReach();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderReach();
    }

    private void renderReach() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text;
        if (System.currentTimeMillis() - lastAttackTime > 3000) {
            text = "3.00 blocks";
        } else {
            text = df.format(reach) + " blocks";
        }

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(65, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
