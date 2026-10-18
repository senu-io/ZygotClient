package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.render.MenuBlurMod;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Inject(method = "drawWorldBackground", at = @At("HEAD"))
    public void onDrawWorldBackground(int tint, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            MenuBlurMod mod = Zygot.INSTANCE.modManager.getMod(MenuBlurMod.class);
            if (mod != null && mod.isToggled()) {
                // Menu blur effect active
            }
        }
    }
}
