package com.zygot.mod.impl.pvp;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class NoHurtCamMod extends Mod {

    public final NumberSetting intensity;

    public NoHurtCamMod() {
        super("No Hurt Cam", "Reduces or disables camera shake when taking damage.", Category.PVP);
        this.intensity = new NumberSetting("Shake Scale", 0.0, 0.0, 1.0, 0.1);
        addSettings(intensity);
    }
}
