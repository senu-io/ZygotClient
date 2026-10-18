package com.zygot.cosmetics.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

/**
 * 3D Baseball Cap model with visor.
 */
public class ModelCap extends ModelBase {

    private final ModelRenderer crown;
    private final ModelRenderer visor;

    public ModelCap() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.crown = new ModelRenderer(this, 0, 0);
        this.crown.addBox(-4.5F, -8.5F, -4.5F, 9, 3, 9);

        this.visor = new ModelRenderer(this, 0, 14);
        this.visor.addBox(-4.5F, -6.5F, -8.5F, 9, 1, 4);
        this.visor.rotateAngleX = (float) Math.toRadians(5.0);
        this.crown.addChild(this.visor);
    }

    public void renderCap(float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        this.crown.render(0.0625F);
        GL11.glPopMatrix();
    }
}
