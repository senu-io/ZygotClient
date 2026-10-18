package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.pvp.HitColorMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> {

    @Inject(method = "setDoRenderBrightness", at = @At("HEAD"), cancellable = true)
    public void onSetDoRenderBrightness(T entityLivingBaseIn, float partialTicks, CallbackInfoReturnable<Boolean> cir) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            HitColorMod mod = Zygot.INSTANCE.modManager.getMod(HitColorMod.class);
            if (mod != null && mod.isToggled() && entityLivingBaseIn.hurtTime > 0) {
                int col = mod.getTintColor();
                float r = (float) (col >> 16 & 255) / 255.0F;
                float g = (float) (col >> 8 & 255) / 255.0F;
                float b = (float) (col & 255) / 255.0F;
                float a = (float) (col >> 24 & 255) / 255.0F;
                GlStateManager.color(r, g, b, a);
            }
        }
    }
}
