package com.zygot.mod.impl.pvp;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.KeyEvent;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.KeybindSetting;
import org.lwjgl.input.Keyboard;

public class FreelookMod extends Mod {

    public final KeybindSetting keybindSetting;
    public final BooleanSetting invertPitch;

    private boolean active = false;
    private float cameraYaw = 0f;
    private float cameraPitch = 0f;

    public FreelookMod() {
        super("Freelook", "Allows looking around in 360 degrees without turning the player.", Category.PVP);
        this.keybindSetting = new KeybindSetting("Keybind", Keyboard.KEY_LMENU); // Left Alt
        this.invertPitch = new BooleanSetting("Invert Pitch", false);
        addSettings(keybindSetting, invertPitch);
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!isToggled()) return;

        int key = keybindSetting.getKeyCode();
        if (key != 0) {
            boolean down = Keyboard.isKeyDown(key);
            if (down && !active) {
                active = true;
                if (mc.thePlayer != null) {
                    cameraYaw = mc.thePlayer.rotationYaw;
                    cameraPitch = mc.thePlayer.rotationPitch;
                }
            } else if (!down && active) {
                active = false;
            }
        }
    }

    public boolean isActive() {
        return isToggled() && active;
    }

    public float getCameraYaw() {
        return cameraYaw;
    }

    public void setCameraYaw(float cameraYaw) {
        this.cameraYaw = cameraYaw;
    }

    public float getCameraPitch() {
        return cameraPitch;
    }

    public void setCameraPitch(float cameraPitch) {
        this.cameraPitch = cameraPitch;
    }
}
