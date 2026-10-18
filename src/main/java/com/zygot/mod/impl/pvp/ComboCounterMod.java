package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.AttackEntityEvent;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.renderer.GlStateManager;

public class ComboCounterMod extends HudMod {

    private int combo = 0;
    private long lastHitTime = 0;

    public ComboCounterMod() {
        super("Combo Counter", "Counts consecutive PvP attacks landed.", 10, 235, 60, 18);
    }

    @EventTarget
    public void onAttack(AttackEntityEvent e) {
        this.combo++;
        this.lastHitTime = System.currentTimeMillis();
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (mc.thePlayer != null && mc.thePlayer.hurtTime > 0) {
            this.combo = 0;
        }
        if (System.currentTimeMillis() - lastHitTime > 2500) {
            this.combo = 0;
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderCombo();
    }

    @Override
    public void render(float partialTicks) {
        renderCombo();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderCombo();
    }

    private void renderCombo() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text = combo + " Combo";
        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(60, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
