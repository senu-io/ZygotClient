package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.branding.ClientBranding;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;

public class WatermarkMod extends HudMod {

    public WatermarkMod() {
        super("Watermark", "Displays the magnificent Zygot Client watermark.", 10, 510, 110, 22);
        setToggled(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderWatermark();
    }

    @Override
    public void render(float partialTicks) {
        renderWatermark();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderWatermark();
    }

    private void renderWatermark() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text = ClientBranding.getWatermarkText();
        float textWidth = Zygot.INSTANCE.fontManager.bold16.getStringWidth(text);
        this.width = textWidth + 24;
        this.height = 22;

        drawHudBackground(width, height);

        // Glowing accent marker pip
        int accent = Zygot.INSTANCE.themeManager.getCurrentTheme().accent;
        Render2DHelper.drawRoundedRect(5, 5, 3, height - 10, 1.5f, accent);

        Zygot.INSTANCE.fontManager.bold16.drawString(text, 14, 5, getTextColor());

        GlStateManager.popMatrix();
    }
}
