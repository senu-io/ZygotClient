package com.zygot.mod.impl.qol;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.KeyEvent;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import net.minecraft.client.settings.KeyBinding;

public class ToggleSneakMod extends Mod {

    public final BooleanSetting containerSafety;
    private boolean sneaking = false;

    public ToggleSneakMod() {
        super("Toggle Sneak", "Toggles sneaking so you don't have to hold Shift.", Category.QOL);
        this.containerSafety = new BooleanSetting("Unsneak in Menus", true);
        addSettings(containerSafety);
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if (!isToggled() || mc.thePlayer == null) return;
        if (e.getKey() == mc.gameSettings.keyBindSneak.getKeyCode()) {
            sneaking = !sneaking;
        }
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!isToggled() || mc.thePlayer == null) return;

        if (sneaking) {
            if (containerSafety.isEnabled() && mc.currentScreen != null) {
                return;
            }
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }
}
