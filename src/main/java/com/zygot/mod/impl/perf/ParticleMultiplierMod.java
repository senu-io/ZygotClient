package com.zygot.mod.impl.perf;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class ParticleMultiplierMod extends Mod {

    public final NumberSetting multiplier;

    public ParticleMultiplierMod() {
        super("Particle Multiplier", "Adjusts the density of critical hit and sharpness particles.", Category.PERFORMANCE);
        this.multiplier = new NumberSetting("Multiplier", 1.0, 0.0, 5.0, 0.5);
        addSettings(multiplier);
    }

    public float getMultiplier() {
        return isToggled() ? multiplier.getValueFloat() : 1.0F;
    }
}
