package com.zygot.cosmetics.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

/**
 * 3D Halo model floating above player head with dynamic rotation and hover bobbing.
 */
public class ModelHalo extends ModelBase {

    private final ModelRenderer haloRing;

    public ModelHalo() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.haloRing = new ModelRenderer(this, 0, 0);
        // Create circular ring box
        this.haloRing.addBox(-5.0F, -13.0F, -5.0F, 10, 1, 10);
    }

    public void renderHalo(float scale) {
        GL11.glPushMatrix();

        // Hover bobbing
        float bob = (float) Math.sin(System.currentTimeMillis() / 400.0) * 0.8F;
        GL11.glTranslatef(0.0F, bob * 0.05F, 0.0F);

        // Spin rotation
        float spin = (System.currentTimeMillis() % 3600L) / 10.0F;
        GL11.glRotatef(spin, 0.0F, 1.0F, 0.0F);

        GL11.glScalef(scale, scale, scale);
        this.haloRing.render(0.0625F);

        GL11.glPopMatrix();
    }
}
