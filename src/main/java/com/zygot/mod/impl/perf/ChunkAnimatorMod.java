package com.zygot.mod.impl.perf;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.ModeSetting;
import com.zygot.mod.setting.NumberSetting;

public class ChunkAnimatorMod extends Mod {

    public final ModeSetting animationMode;
    public final NumberSetting duration;

    public ChunkAnimatorMod() {
        super("Chunk Animator", "Smoothly animates newly loaded world chunks into view.", Category.PERFORMANCE);

        this.animationMode = new ModeSetting("Mode", "Below", "Below", "Above", "Horizon");
        this.duration = new NumberSetting("Duration (ms)", 500, 100, 2000, 50);

        addSettings(animationMode, duration);
        setToggled(true);
    }
}
