package com.zygot.mod.setting;

import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting {
    private int keyCode;

    public KeybindSetting(String name, int defaultKeyCode) {
        super(name);
        this.keyCode = defaultKeyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyName() {
        return keyCode == 0 ? "None" : Keyboard.getKeyName(keyCode);
    }
}
