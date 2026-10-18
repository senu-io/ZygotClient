package com.zygot.mod.impl.render;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;

public class FullbrightMod extends Mod {

    private float originalGamma;

    public FullbrightMod() {
        super("Fullbright", "Increases brightness/gamma for maximum night visibility.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.originalGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100.0F;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = originalGamma > 0 ? originalGamma : 1.0F;
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (isToggled() && mc.gameSettings.gammaSetting < 100.0F) {
            mc.gameSettings.gammaSetting = 100.0F;
        }
    }
}
