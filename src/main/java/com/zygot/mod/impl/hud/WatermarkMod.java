package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.branding.ClientBranding;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class WatermarkMod extends HudMod {

    private static final ResourceLocation LOGO_ICON = new ResourceLocation("zygot/textures/logo_white_nobg.png");

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
        this.width = textWidth + 34;
        this.height = 22;

        drawHudBackground(width, height);

        // Logo icon
        Render2DHelper.drawTexturedRect(6, 4, 14, 14, LOGO_ICON);

        Zygot.INSTANCE.fontManager.bold16.drawString(text, 24, 5, getTextColor());

        GlStateManager.popMatrix();
    }
}
