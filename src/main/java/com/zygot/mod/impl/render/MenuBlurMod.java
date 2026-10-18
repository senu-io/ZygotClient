package com.zygot.mod.impl.render;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class MenuBlurMod extends Mod {

    public final NumberSetting blurRadius;

    public MenuBlurMod() {
        super("Menu Blur", "Renders a modern frosted blur behind in-game menus.", Category.RENDER);
        this.blurRadius = new NumberSetting("Radius", 8.0, 2.0, 20.0, 1.0);
        addSettings(blurRadius);
        setToggled(true);
    }
}
