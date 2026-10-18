package com.zygot.mod.setting;

import java.awt.Color;

public class ColorSetting extends Setting {
    private int color;
    private boolean rainbow;
    private float rainbowSpeed = 2.0F;

    public ColorSetting(String name, int defaultColor) {
        super(name);
        this.color = defaultColor;
    }

    public int getColor() {
        if (rainbow) {
            float hue = (System.currentTimeMillis() % (int) (10000 / rainbowSpeed)) / (10000 / rainbowSpeed);
            int rgb = Color.HSBtoRGB(hue, 0.8F, 1.0F);
            int alpha = (color >> 24) & 0xFF;
            return (alpha << 24) | (rgb & 0x00FFFFFF);
        }
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public float getRainbowSpeed() {
        return rainbowSpeed;
    }

    public void setRainbowSpeed(float rainbowSpeed) {
        this.rainbowSpeed = rainbowSpeed;
    }
}
