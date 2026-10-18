package com.zygot.cosmetics.impl;

import com.zygot.cosmetics.Cosmetic;
import com.zygot.cosmetics.CosmeticType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CosmeticCape extends Cosmetic {

    public CosmeticCape(String id, String name, ResourceLocation texture) {
        super(id, name, CosmeticType.CAPE, texture);
    }

    @Override
    public void render(AbstractClientPlayer player, ModelPlayer model, float partialTicks) {
        if (player.isInvisible()) return;

        GlStateManager.pushMatrix();

        model.bipedBody.postRender(0.0625F);

        GlStateManager.translate(0.0F, 0.0F, 0.125F);

        float[] rot = CapePhysics.calculateCapeRotations(player, partialTicks);
        GlStateManager.rotate(rot[0], 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(rot[1], 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(rot[2], 0.0F, 1.0F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Render cape box: 10 wide, 16 high, 1 deep
        model.renderCape(0.0625F);

        GlStateManager.popMatrix();
    }
}
