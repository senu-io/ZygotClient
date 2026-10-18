package com.zygot.cosmetics.impl;

import com.zygot.cosmetics.Cosmetic;
import com.zygot.cosmetics.CosmeticType;
import com.zygot.cosmetics.model.ModelWings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CosmeticWings extends Cosmetic {

    private final ModelWings modelWings = new ModelWings();

    public CosmeticWings(String id, String name, ResourceLocation texture) {
        super(id, name, CosmeticType.WINGS, texture);
    }

    @Override
    public void render(AbstractClientPlayer player, ModelPlayer model, float partialTicks) {
        GlStateManager.pushMatrix();

        // Bind to player's torso/body orientation
        model.bipedBody.postRender(0.0625F);

        // Position slightly behind the back
        GlStateManager.translate(0.0F, 0.05F, 0.12F);
        GlStateManager.scale(0.85F, 0.85F, 0.85F);

        // Bind wing texture
        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        modelWings.renderWings(player, partialTicks, 1.0F);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
