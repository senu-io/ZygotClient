package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.impl.hud.BossbarMod;
import com.zygot.mod.impl.hud.ScoreboardMod;
import com.zygot.mod.impl.pvp.CrosshairMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame extends Gui {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    public void onRenderGameOverlay(float partialTicks, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            Zygot.INSTANCE.eventBus.post(new Render2DEvent(partialTicks));
        }
    }

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    public void onRenderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ScoreboardMod mod = Zygot.INSTANCE.modManager.getMod(ScoreboardMod.class);
            if (mod != null && mod.isToggled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    public void onRenderBossHealth(CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            BossbarMod mod = Zygot.INSTANCE.modManager.getMod(BossbarMod.class);
            if (mod != null && mod.isToggled()) {
                ci.cancel();
            }
        }
    }
}
