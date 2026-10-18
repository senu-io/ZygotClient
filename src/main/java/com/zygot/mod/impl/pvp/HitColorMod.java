package com.zygot.mod.impl.pvp;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.mod.setting.NumberSetting;
import com.zygot.theme.Theme;

public class HitColorMod extends Mod {

    public final ColorSetting color;
    public final NumberSetting alpha;

    public HitColorMod() {
        super("Hit Color", "Changes the red damage tint color of entities.", Category.PVP);

        this.color = new ColorSetting("Damage Color", Theme.rgba(255, 50, 80, 255));
        this.alpha = new NumberSetting("Intensity", 0.6, 0.1, 1.0, 0.05);

        addSettings(color, alpha);
    }

    public int getTintColor() {
        int c = color.getColor();
        int a = (int) (alpha.getValueFloat() * 255);
        return (a << 24) | (c & 0x00FFFFFF);
    }
}
