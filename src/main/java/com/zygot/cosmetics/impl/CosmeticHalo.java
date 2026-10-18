package com.zygot.cosmetics.impl;

import com.zygot.cosmetics.Cosmetic;
import com.zygot.cosmetics.CosmeticType;
import com.zygot.cosmetics.model.ModelHalo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CosmeticHalo extends Cosmetic {

    private final ModelHalo modelHalo = new ModelHalo();

    public CosmeticHalo(String id, String name, ResourceLocation texture) {
        super(id, name, CosmeticType.HALO, texture);
    }

    @Override
    public void render(AbstractClientPlayer player, ModelPlayer model, float partialTicks) {
        GlStateManager.pushMatrix();

        // Follow head movements and orientation
        model.bipedHead.postRender(0.0625F);

        GlStateManager.translate(0.0F, -0.2F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        modelHalo.renderHalo(0.7F);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
