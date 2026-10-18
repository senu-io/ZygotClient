package com.zygot.mod.impl.qol;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;

public class ChatTweaksMod extends Mod {

    public final BooleanSetting transparentChat;
    public final BooleanSetting timestamps;
    public final BooleanSetting unlimitedHistory;

    public ChatTweaksMod() {
        super("Chat Tweaks", "Customizes chat transparency, timestamps, and history length.", Category.QOL);

        this.transparentChat = new BooleanSetting("Transparent Background", false);
        this.timestamps = new BooleanSetting("Show Timestamps", false);
        this.unlimitedHistory = new BooleanSetting("Infinite History", true);

        addSettings(transparentChat, timestamps, unlimitedHistory);
        setToggled(true);
    }
}
