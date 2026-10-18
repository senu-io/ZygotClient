package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.pvp.FreelookMod;
import com.zygot.mod.impl.pvp.NoHurtCamMod;
import com.zygot.mod.impl.render.ZoomMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            NoHurtCamMod mod = Zygot.INSTANCE.modManager.getMod(NoHurtCamMod.class);
            if (mod != null && mod.isToggled()) {
                if (mod.intensity.getValue() <= 0.05) {
                    ci.cancel();
                }
            }
        }
    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V"))
    private void onSetAngles(EntityPlayerSP instance, float x, float y) {
        FreelookMod mod = (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null)
                ? Zygot.INSTANCE.modManager.getMod(FreelookMod.class) : null;
        if (mod != null && mod.isActive()) {
            float yaw = mod.getCameraYaw() + x * 0.15F;
            float pitch = mod.getCameraPitch() - y * 0.15F;
            pitch = Math.max(-90.0F, Math.min(90.0F, pitch));
            mod.setCameraYaw(yaw);
            mod.setCameraPitch(pitch);
        } else {
            instance.setAngles(x, y);
        }
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F"))
    public float onOrientCameraYaw(Entity entity) {
        FreelookMod mod = (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null)
                ? Zygot.INSTANCE.modManager.getMod(FreelookMod.class) : null;
        if (mod != null && mod.isActive()) {
            return mod.getCameraYaw();
        }
        return entity.rotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F"))
    public float onOrientCameraPitch(Entity entity) {
        FreelookMod mod = (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null)
                ? Zygot.INSTANCE.modManager.getMod(FreelookMod.class) : null;
        if (mod != null && mod.isActive()) {
            return mod.getCameraPitch();
        }
        return entity.rotationPitch;
    }
}
