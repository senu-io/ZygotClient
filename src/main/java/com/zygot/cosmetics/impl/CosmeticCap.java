package com.zygot.cosmetics.impl;

import com.zygot.cosmetics.Cosmetic;
import com.zygot.cosmetics.CosmeticType;
import com.zygot.cosmetics.model.ModelCap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CosmeticCap extends Cosmetic {

    private final ModelCap modelCap = new ModelCap();

    public CosmeticCap(String id, String name, ResourceLocation texture) {
        super(id, name, CosmeticType.HAT, texture);
    }

    @Override
    public void render(AbstractClientPlayer player, ModelPlayer model, float partialTicks) {
        GlStateManager.pushMatrix();

        model.bipedHead.postRender(0.0625F);

        GlStateManager.translate(0.0F, 0.05F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        modelCap.renderCap(0.95F);

        GlStateManager.popMatrix();
    }
}
