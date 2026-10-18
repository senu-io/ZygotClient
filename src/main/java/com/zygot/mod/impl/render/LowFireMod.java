package com.zygot.mod.impl.render;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class LowFireMod extends Mod {

    public final NumberSetting fireHeight;

    public LowFireMod() {
        super("Low Fire", "Lowers the first-person burning fire effect for clearer visibility.", Category.RENDER);
        this.fireHeight = new NumberSetting("Fire Height", 0.3, 0.0, 1.0, 0.05);
        addSettings(fireHeight);
        setToggled(true);
    }
}
