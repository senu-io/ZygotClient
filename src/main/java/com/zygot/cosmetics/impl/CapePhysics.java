package com.zygot.cosmetics.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.MathHelper;

/**
 * Simulates realistic 1.8.9 cape movement, velocity inertia, and wind flutter.
 */
public class CapePhysics {

    public static float[] calculateCapeRotations(AbstractClientPlayer player, float partialTicks) {
        double d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks);
        double d1 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks);
        double d2 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks);

        float f = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        double d3 = MathHelper.sin(f * (float) Math.PI / 180.0F);
        double d4 = -MathHelper.cos(f * (float) Math.PI / 180.0F);

        float f1 = (float) d1 * 10.0F;
        f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);

        float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
        float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

        if (f2 < 0.0F) f2 = 0.0F;

        float f4 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        f1 = f1 + MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

        if (player.isSneaking()) {
            f1 += 25.0F;
        }

        return new float[] { 6.0F + f2 / 2.0F + f1, f3 / 2.0F, -f3 / 2.0F };
    }
}
