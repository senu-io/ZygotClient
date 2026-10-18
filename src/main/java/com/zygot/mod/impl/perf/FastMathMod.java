package com.zygot.mod.impl.perf;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;

public class FastMathMod extends Mod {

    private static final float[] SIN_TABLE_FAST = new float[4096];

    static {
        for (int i = 0; i < 4096; ++i) {
            SIN_TABLE_FAST[i] = (float) Math.sin((double) (((float) i + 0.5F) / 4096.0F * ((float) Math.PI * 2F)));
        }
    }

    public FastMathMod() {
        super("Fast Math", "Uses precomputed trigonometric tables for lower CPU usage.", Category.PERFORMANCE);
        setToggled(true);
    }

    public static float sin(float value) {
        return SIN_TABLE_FAST[(int) (value * 651.8986F) & 4095];
    }

    public static float cos(float value) {
        return SIN_TABLE_FAST[(int) ((value + ((float) Math.PI / 2F)) * 651.8986F) & 4095];
    }
}
