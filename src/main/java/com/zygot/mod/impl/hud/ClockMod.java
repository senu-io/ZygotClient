package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockMod extends HudMod {

    private final ModeSetting format;

    public ClockMod() {
        super("Clock", "Displays current real-world time.", 10, 410, 65, 18);
        this.format = new ModeSetting("Format", "12-Hour", "12-Hour", "24-Hour");
        addSettings(format);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderClock();
    }

    @Override
    public void render(float partialTicks) {
        renderClock();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderClock();
    }

    private void renderClock() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        SimpleDateFormat sdf = format.is("12-Hour") ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("HH:mm:ss");
        String text = sdf.format(new Date());

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(65, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
