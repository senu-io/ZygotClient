package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.qol.ScrollTooltipsMod;
import com.zygot.mod.impl.render.MenuBlurMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    private static int lastTooltipX = -1;
    private static int lastTooltipY = -1;
    private static int tooltipScrollOffset = 0;

    @Inject(method = "drawWorldBackground", at = @At("HEAD"))
    public void onDrawWorldBackground(int tint, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            MenuBlurMod mod = Zygot.INSTANCE.modManager.getMod(MenuBlurMod.class);
            if (mod != null && mod.isToggled()) {
                // Menu blur effect active
            }
        }
    }

    @Inject(method = "drawHoveringText", at = @At("HEAD"))
    public void onDrawHoveringTextHead(List<String> textLines, int x, int y, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ScrollTooltipsMod mod = Zygot.INSTANCE.modManager.getMod(ScrollTooltipsMod.class);
            if (mod != null && mod.isToggled()) {
                if (Math.abs(x - lastTooltipX) > 15 || Math.abs(y - lastTooltipY) > 15) {
                    tooltipScrollOffset = 0;
                    lastTooltipX = x;
                    lastTooltipY = y;
                }

                int dWheel = Mouse.getDWheel();
                if (dWheel > 0) {
                    tooltipScrollOffset += 15;
                } else if (dWheel < 0) {
                    tooltipScrollOffset -= 15;
                }

                if (tooltipScrollOffset != 0) {
                    GlStateManager.translate(0.0F, (float) tooltipScrollOffset, 0.0F);
                }
            }
        }
    }

    @Inject(method = "drawHoveringText", at = @At("RETURN"))
    public void onDrawHoveringTextReturn(List<String> textLines, int x, int y, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ScrollTooltipsMod mod = Zygot.INSTANCE.modManager.getMod(ScrollTooltipsMod.class);
            if (mod != null && mod.isToggled() && tooltipScrollOffset != 0) {
                GlStateManager.translate(0.0F, (float) -tooltipScrollOffset, 0.0F);
            }
        }
    }
}
