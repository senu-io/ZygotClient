package com.zygot.mod.impl.render;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class ItemPhysicsMod extends Mod {

    public final NumberSetting rotationSpeed;

    public ItemPhysicsMod() {
        super("Item Physics", "Realistic 3D ground physics for dropped items.", Category.RENDER);
        this.rotationSpeed = new NumberSetting("Rotation Speed", 1.0, 0.2, 3.0, 0.1);
        addSettings(rotationSpeed);
        setToggled(true);
    }
}
