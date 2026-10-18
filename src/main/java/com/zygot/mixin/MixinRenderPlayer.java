package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.event.impl.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {

    @Inject(method = "renderRightArm", at = @At("RETURN"))
    public void onRenderRightArm(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    public void onDoRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            RenderPlayer renderer = (RenderPlayer) (Object) this;
            ModelPlayer model = renderer.getMainModel();
            Zygot.INSTANCE.eventBus.post(new RenderPlayerEvent(entity, model, partialTicks));
        }
    }
}
