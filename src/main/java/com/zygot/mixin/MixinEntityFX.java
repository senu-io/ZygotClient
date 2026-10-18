package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.perf.ParticleCullingMod;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
public abstract class MixinEntityFX {

    @Inject(method = "renderParticle", at = @At("HEAD"), cancellable = true)
    public void onRenderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ParticleCullingMod mod = Zygot.INSTANCE.modManager.getMod(ParticleCullingMod.class);
            if (mod != null && mod.isToggled()) {
                EntityFX fx = (EntityFX) (Object) this;
                // Frustum / Distance culling check
                if (entityIn.getDistanceSq(fx.posX, fx.posY, fx.posZ) > 1024.0D) {
                    ci.cancel();
                }
            }
        }
    }
}
