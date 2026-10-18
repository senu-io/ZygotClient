package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.pvp.FreelookMod;
import com.zygot.mod.impl.render.ZoomMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = "getFOVModifier", at = @At("RETURN"), cancellable = true)
    public void onGetFOVModifier(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> cir) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ZoomMod mod = Zygot.INSTANCE.modManager.getMod(ZoomMod.class);
            if (mod != null && mod.isZooming()) {
                float fov = cir.getReturnValue();
                cir.setReturnValue(fov / mod.getZoomFactor());
            }
        }
    }
}
