package com.zygot.mixin;

import com.zygot.ui.menu.ZygotIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    public void onInitGui(CallbackInfo ci) {
        if (!((Object) this instanceof ZygotIngameMenu)) {
            int centerX = width / 2;
            this.buttonList.add(new GuiButton(666, centerX - 100, height / 4 + 144, 200, 20, "Zygot Client Settings"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void onActionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 666) {
            Minecraft.getMinecraft().displayGuiScreen(new com.zygot.ui.modmenu.ZygotModMenu(this));
        }
    }
}
