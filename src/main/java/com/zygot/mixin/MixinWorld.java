package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.render.TimeChangerMod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Inject(method = "getWorldTime", at = @At("RETURN"), cancellable = true)
    public void onGetWorldTime(CallbackInfoReturnable<Long> cir) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            TimeChangerMod mod = Zygot.INSTANCE.modManager.getMod(TimeChangerMod.class);
            if (mod != null && mod.isToggled()) {
                long originalTime = cir.getReturnValue();
                cir.setReturnValue(mod.getAdjustedTime(originalTime));
            }
        }
    }
}
