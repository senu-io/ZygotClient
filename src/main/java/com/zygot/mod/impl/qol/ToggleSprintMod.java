package com.zygot.mod.impl.qol;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class ToggleSprintMod extends Mod {

    public final BooleanSetting showHudText;

    public ToggleSprintMod() {
        super("Toggle Sprint", "Automatically sprints when moving forward.", Category.QOL);
        this.showHudText = new BooleanSetting("Show Status", true);
        addSettings(showHudText);
        setToggled(true);
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!isToggled() || mc.thePlayer == null) return;

        if (mc.thePlayer.movementInput.moveForward > 0 &&
                !mc.thePlayer.isSneaking() &&
                !mc.thePlayer.isCollidedHorizontally &&
                mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
