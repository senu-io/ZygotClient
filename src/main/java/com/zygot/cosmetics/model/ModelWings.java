package com.zygot.cosmetics.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * 3D Dragon/Crystal/Galaxy Wings model with dynamic flapping physics.
 */
public class ModelWings extends ModelBase {

    private final ModelRenderer wingLeft;
    private final ModelRenderer wingRight;
    private final ModelRenderer wingTipLeft;
    private final ModelRenderer wingTipRight;

    public ModelWings() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        // Left Wing
        this.wingLeft = new ModelRenderer(this, 22, 0);
        this.wingLeft.addBox(-10.0F, 0.0F, 0.0F, 10, 20, 1);
        this.wingTipLeft = new ModelRenderer(this, 0, 0);
        this.wingTipLeft.addBox(-20.0F, 0.0F, 0.0F, 10, 18, 1);
        this.wingLeft.addChild(this.wingTipLeft);

        // Right Wing
        this.wingRight = new ModelRenderer(this, 22, 0);
        this.wingRight.mirror = true;
        this.wingRight.addBox(0.0F, 0.0F, 0.0F, 10, 20, 1);
        this.wingTipRight = new ModelRenderer(this, 0, 0);
        this.wingTipRight.mirror = true;
        this.wingTipRight.addBox(10.0F, 0.0F, 0.0F, 10, 18, 1);
        this.wingRight.addChild(this.wingTipRight);
    }

    public void renderWings(Entity entity, float partialTicks, float scale) {
        // Flapping animation calculation
        float time = (System.currentTimeMillis() % 1000L) / 1000.0F;
        float flap = (float) Math.sin(time * Math.PI * 2.0) * 20.0F;

        boolean flying = entity.isAirBorne || !entity.onGround;
        if (flying) {
            flap *= 1.8F;
        }

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);

        // Left Wing angle
        this.wingLeft.rotateAngleZ = (float) Math.toRadians(20.0 + flap);
        this.wingLeft.rotateAngleY = (float) Math.toRadians(-25.0 + flap * 0.5);
        this.wingTipLeft.rotateAngleZ = (float) Math.toRadians(-15.0 - flap * 0.5);

        // Right Wing angle
        this.wingRight.rotateAngleZ = (float) Math.toRadians(-20.0 - flap);
        this.wingRight.rotateAngleY = (float) Math.toRadians(25.0 - flap * 0.5);
        this.wingTipRight.rotateAngleZ = (float) Math.toRadians(15.0 + flap * 0.5);

        this.wingLeft.render(0.0625F);
        this.wingRight.render(0.0625F);

        GL11.glPopMatrix();
    }
}
