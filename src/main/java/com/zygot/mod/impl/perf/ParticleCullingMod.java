package com.zygot.mod.impl.perf;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class ParticleCullingMod extends Mod {

    public final BooleanSetting cullBehind;

    public ParticleCullingMod() {
        super("Particle Culling", "Significantly improves FPS by culling particles outside screen FOV.", Category.PERFORMANCE);
        this.cullBehind = new BooleanSetting("Cull Behind Player", true);
        addSettings(cullBehind);
        setToggled(true);
    }
}
