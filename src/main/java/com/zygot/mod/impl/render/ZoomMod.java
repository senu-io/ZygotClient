package com.zygot.mod.impl.render;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.KeybindSetting;
import com.zygot.mod.setting.NumberSetting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ZoomMod extends Mod {

    public final KeybindSetting zoomKey;
    public final NumberSetting zoomFactor;
    public final BooleanSetting smoothCamera;

    private boolean zooming = false;
    private float currentFov = 0f;
    private float targetFov = 0f;

    public ZoomMod() {
        super("Zoom", "Cinematic OptiFine-style smooth FOV zoom with scroll adjustment.", Category.RENDER);
        this.zoomKey = new KeybindSetting("Zoom Key", Keyboard.KEY_C);
        this.zoomFactor = new NumberSetting("Zoom Factor", 4.0, 2.0, 10.0, 0.5);
        this.smoothCamera = new BooleanSetting("Cinematic Camera", true);
        addSettings(zoomKey, zoomFactor, smoothCamera);
        setToggled(true);
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!isToggled()) return;

        int key = zoomKey.getKeyCode();
        if (key != 0) {
            boolean down = Keyboard.isKeyDown(key);
            if (down && !zooming) {
                zooming = true;
                if (smoothCamera.isEnabled()) {
                    mc.gameSettings.smoothCamera = true;
                }
            } else if (!down && zooming) {
                zooming = false;
                if (smoothCamera.isEnabled()) {
                    mc.gameSettings.smoothCamera = false;
                }
            }
        }
    }

    public boolean isZooming() {
        return isToggled() && zooming;
    }

    public float getZoomFactor() {
        return zoomFactor.getValueFloat();
    }
}
