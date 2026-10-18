package com.zygot.mod.impl.qol;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class NickHiderMod extends Mod {

    public final BooleanSetting hideOthers;
    private String customNick = "You";

    public NickHiderMod() {
        super("Nick Hider", "Hides your username on screen for streaming/recording privacy.", Category.QOL);
        this.hideOthers = new BooleanSetting("Hide Other Players", false);
        addSettings(hideOthers);
    }

    public String replaceNick(String input) {
        if (!isToggled() || mc.thePlayer == null || input == null) return input;
        String realName = mc.thePlayer.getName();
        return input.replace(realName, customNick);
    }

    public String getCustomNick() {
        return customNick;
    }

    public void setCustomNick(String customNick) {
        this.customNick = customNick;
    }
}
