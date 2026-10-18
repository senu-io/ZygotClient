package com.zygot.mod.impl.render;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.ModeSetting;
import com.zygot.mod.setting.NumberSetting;

public class TimeChangerMod extends Mod {

    public final ModeSetting mode;
    public final NumberSetting customTime;

    public TimeChangerMod() {
        super("Time Changer", "Client-side lock for in-game world time.", Category.RENDER);
        this.mode = new ModeSetting("Time", "Day", "Day", "Noon", "Sunset", "Night", "Custom");
        this.customTime = new NumberSetting("Custom Time", 6000, 0, 24000, 500);
        addSettings(mode, customTime);
    }

    public long getAdjustedTime(long worldTime) {
        if (!isToggled()) return worldTime;

        if (mode.is("Day")) return 1000L;
        if (mode.is("Noon")) return 6000L;
        if (mode.is("Sunset")) return 12500L;
        if (mode.is("Night")) return 18000L;
        if (mode.is("Custom")) return (long) customTime.getValue();

        return worldTime;
    }
}
