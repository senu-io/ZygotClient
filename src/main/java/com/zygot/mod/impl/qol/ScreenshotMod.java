package com.zygot.mod.impl.qol;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class ScreenshotMod extends Mod {

    public final BooleanSetting copyToClipboard;

    public ScreenshotMod() {
        super("Fast Screenshot", "Automatically copies captured screenshots to system clipboard.", Category.QOL);
        this.copyToClipboard = new BooleanSetting("Copy to Clipboard", true);
        addSettings(copyToClipboard);
        setToggled(true);
    }
}
