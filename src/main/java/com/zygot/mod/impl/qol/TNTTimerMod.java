package com.zygot.mod.impl.qol;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class TNTTimerMod extends Mod {

    public final BooleanSetting showDecimal;

    public TNTTimerMod() {
        super("TNT Timer", "Displays exact countdown timer ticks above primed TNT.", Category.QOL);
        this.showDecimal = new BooleanSetting("Decimal Seconds", true);
        addSettings(showDecimal);
        setToggled(true);
    }
}
