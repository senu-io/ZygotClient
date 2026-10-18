package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.qol.TNTTimerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(RenderTNTPrimed.class)
public abstract class MixinRenderTNTPrimed {

    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("0.00");

    @Inject(method = "doRender", at = @At("RETURN"))
    public void onDoRenderTNT(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Zygot.INSTANCE == null || Zygot.INSTANCE.modManager == null) return;
        TNTTimerMod mod = Zygot.INSTANCE.modManager.getMod(TNTTimerMod.class);
        if (mod == null || !mod.isToggled()) return;

        int fuse = entity.fuse;
        if (fuse < 1) return;

        double seconds = fuse / 20.0;
        String text = mod.showDecimal.isEnabled() ? (SECONDS_FORMAT.format(seconds) + "s") : ((fuse / 20) + "s");

        int color = 0x55FF55;
        if (fuse < 20) {
            color = 0xFF5555;
        } else if (fuse < 40) {
            color = 0xFFAA00;
        } else if (fuse < 60) {
            color = 0xFFFF55;
        }

        renderTag(entity, text, x, y + entity.height + 0.5D, z, color);
    }

    private void renderTag(EntityTNTPrimed entity, String str, double x, double y, double z, int color) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rm.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) (-j - 2), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.45F).endVertex();
        worldrenderer.pos((double) (-j - 2), 9.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.45F).endVertex();
        worldrenderer.pos((double) (j + 2), 9.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.45F).endVertex();
        worldrenderer.pos((double) (j + 2), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.45F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, color);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
