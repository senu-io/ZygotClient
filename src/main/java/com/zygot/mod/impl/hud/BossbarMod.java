package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class BossbarMod extends HudMod {

    public BossbarMod() {
        super("Bossbar", "Modern boss health bar with customizable styling.", 0, 15, 182, 16);
        setToggled(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderBossbar(false);
    }

    @Override
    public void render(float partialTicks) {
        renderBossbar(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderBossbar(true);
    }

    public void renderBossbar(boolean dummy) {
        if (BossStatus.bossName == null && !dummy) return;
        if (BossStatus.statusBarTime <= 0 && !dummy) return;

        GlStateManager.pushMatrix();
        float s = getScale();

        if (x == 0) {
            ScaledResolution sr = new ScaledResolution(mc);
            x = (sr.getScaledWidth() - 182) / 2f;
        }

        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String bossName = dummy ? "Ender Dragon" : BossStatus.bossName;
        float healthPct = dummy ? 0.75f : BossStatus.healthScale;

        this.width = 182;
        this.height = 18;

        drawHudBackground(width, height);

        // Progress track
        Render2DHelper.drawRoundedRect(6, 12, 170, 3, 1.5f, Theme.rgba(30, 30, 40, 200));

        // Health bar fill
        int barCol = Zygot.INSTANCE.themeManager.getCurrentTheme().accent;
        Render2DHelper.drawRoundedRect(6, 12, 170 * healthPct, 3, 1.5f, barCol);

        // Name
        Zygot.INSTANCE.fontManager.bold16.drawCenteredString(bossName, width / 2f, 2, getTextColor());

        GlStateManager.popMatrix();
    }
}
