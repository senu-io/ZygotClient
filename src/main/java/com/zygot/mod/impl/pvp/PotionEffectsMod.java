package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class PotionEffectsMod extends HudMod {

    public PotionEffectsMod() {
        super("Potion Status", "Shows active potion effects and remaining duration.", 10, 350, 100, 40);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderPotions(false);
    }

    @Override
    public void render(float partialTicks) {
        renderPotions(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderPotions(true);
    }

    private void renderPotions(boolean dummy) {
        if (mc.thePlayer == null && !dummy) return;

        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        float currentY = 0;
        float maxW = 100;

        if (dummy) {
            drawPotionCard(0, "Speed II", "01:45", Theme.rgba(79, 172, 254, 255));
            drawPotionCard(24, "Strength I", "00:30", Theme.rgba(239, 68, 68, 255));
            this.width = 110;
            this.height = 48;
        } else {
            Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
            if (effects.isEmpty()) {
                GlStateManager.popMatrix();
                return;
            }

            for (PotionEffect effect : effects) {
                Potion potion = Potion.potionTypes[effect.getPotionID()];
                String name = I18n.format(potion.getName());
                if (effect.getAmplifier() == 1) {
                    name += " II";
                } else if (effect.getAmplifier() == 2) {
                    name += " III";
                }

                String duration = Potion.getDurationString(effect);
                int color = potion.getLiquidColor() | 0xFF000000;

                drawPotionCard(currentY, name, duration, color);
                currentY += 24;
            }

            this.width = maxW;
            this.height = Math.max(20, currentY);
        }

        GlStateManager.popMatrix();
    }

    private void drawPotionCard(float py, String name, String duration, int color) {
        float cardW = 110;
        float cardH = 22;

        int bg = bgColorSetting.getColor();
        int border = borderSetting.isEnabled() ? borderColorSetting.getColor() : 0;
        Render2DHelper.drawGlassPanel(0, py, cardW, cardH, 4, bg, border, 1f);

        // Indicator colored pip
        Render2DHelper.drawRoundedRect(3, py + 3, 3, cardH - 6, 1.5f, color);

        // Text
        Zygot.INSTANCE.fontManager.bold16.drawString(name, 10, py + 2, getTextColor());
        Zygot.INSTANCE.fontManager.light14.drawString(duration, 10, py + 11, Theme.rgba(180, 180, 200, 255));
    }
}
