package com.zygot.cosmetics.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

/**
 * 3D Witch Hat model with brim, cone, and bent tip.
 */
public class ModelWitchHat extends ModelBase {

    private final ModelRenderer brim;
    private final ModelRenderer coneBase;
    private final ModelRenderer coneMiddle;
    private final ModelRenderer coneTip;

    public ModelWitchHat() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        // Wide brim
        this.brim = new ModelRenderer(this, 0, 0);
        this.brim.addBox(-7.0F, -8.5F, -7.0F, 14, 1, 14);

        // Lower cone
        this.coneBase = new ModelRenderer(this, 0, 16);
        this.coneBase.addBox(-5.0F, -12.0F, -5.0F, 10, 4, 10);
        this.brim.addChild(this.coneBase);

        // Middle cone
        this.coneMiddle = new ModelRenderer(this, 0, 31);
        this.coneMiddle.addBox(-3.5F, -16.0F, -3.5F, 7, 4, 7);
        this.coneMiddle.rotateAngleX = (float) Math.toRadians(-5.0);
        this.coneBase.addChild(this.coneMiddle);

        // Bent Tip
        this.coneTip = new ModelRenderer(this, 0, 43);
        this.coneTip.addBox(-2.0F, -19.5F, -2.0F, 4, 4, 4);
        this.coneTip.rotateAngleX = (float) Math.toRadians(-12.0);
        this.coneMiddle.addChild(this.coneTip);
    }

    public void renderHat(float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        this.brim.render(0.0625F);
        GL11.glPopMatrix();
    }
}
