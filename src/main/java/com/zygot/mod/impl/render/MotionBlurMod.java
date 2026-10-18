package com.zygot.mod.impl.render;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class MotionBlurMod extends Mod {

    public final NumberSetting blurAmount;

    public MotionBlurMod() {
        super("Motion Blur", "Adds smooth cinematic camera motion blur.", Category.RENDER);
        this.blurAmount = new NumberSetting("Amount", 5.0, 1.0, 10.0, 1.0);
        addSettings(blurAmount);
    }

    public float getBlurFactor() {
        return (float) (blurAmount.getValue() / 10.0);
    }
}
