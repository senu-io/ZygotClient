package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.qol.NickHiderMod;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @ModifyArg(method = "renderString", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public String modifyRenderString(String text) {
        if (text != null && Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            NickHiderMod mod = Zygot.INSTANCE.modManager.getMod(NickHiderMod.class);
            if (mod != null && mod.isToggled()) {
                return mod.replaceNick(text);
            }
        }
        return text;
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    public String modifyStringWidth(String text) {
        if (text != null && Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            NickHiderMod mod = Zygot.INSTANCE.modManager.getMod(NickHiderMod.class);
            if (mod != null && mod.isToggled()) {
                return mod.replaceNick(text);
            }
        }
        return text;
    }
}
