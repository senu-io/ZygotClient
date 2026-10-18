package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.event.impl.KeyEvent;
import com.zygot.event.impl.MouseEvent;
import com.zygot.event.impl.TickEvent;
import com.zygot.ui.hud.ZygotHudEditor;
import com.zygot.util.animation.DeltaTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public GuiScreen currentScreen;

    private long lastFrame = Sys.getTime();

    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void runGameLoop(CallbackInfo ci) {
        long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;
        DeltaTime.setDeltaTime(deltaTime);
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onTickStart(CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            Zygot.INSTANCE.eventBus.post(new TickEvent(TickEvent.Phase.START));
        }
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void onTickEnd(CallbackInfo ci) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            Zygot.INSTANCE.eventBus.post(new TickEvent(TickEvent.Phase.END));
        }

        // Check for Right Shift to open HUD Editor
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && currentScreen == null) {
            Minecraft.getMinecraft().displayGuiScreen(new ZygotHudEditor());
        }
    }

    @Inject(method = "dispatchKeypresses", at = @At("HEAD"))
    public void onKey(CallbackInfo ci) {
        int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (Keyboard.getEventKeyState() && Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            Zygot.INSTANCE.eventBus.post(new KeyEvent(k));
        }
    }
}
