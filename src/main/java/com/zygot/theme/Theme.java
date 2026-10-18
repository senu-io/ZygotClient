package com.zygot.theme;

import java.awt.Color;

/**
 * Encapsulates color palettes, geometry metrics, and translucent styling tokens.
 * Supports Feather / Lunar style glassmorphism and modern UI themes.
 */
public class Theme implements Cloneable {

    public String name;
    public boolean dark;

    // Accent colors
    public int accent;
    public int accentHover;
    public int accentGlow;

    // Backgrounds (with alpha for glassmorphism)
    public int background;
    public int backgroundSecondary;
    public int surface;
    public int surfaceHover;
    public int surfaceBorder;

    // Typography colors
    public int textPrimary;
    public int textSecondary;
    public int textMuted;

    // Card & Panel tokens
    public int cardBg;
    public int cardBorder;
    public int cardHover;

    // HUD Tokens
    public int hudBg;
    public int hudBorder;
    public int hudText;
    public int hudAccent;

    // Status colors
    public int danger;
    public int success;

    // Geometry / Radii
    public int cornerRadiusSmall = 4;
    public int cornerRadiusMedium = 8;
    public int cornerRadiusLarge = 12;

    public Theme(String name, boolean dark) {
        this.name = name;
        this.dark = dark;
    }

    public static int rgba(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int withAlpha(int argb, int newAlpha) {
        return ((newAlpha & 0xFF) << 24) | (argb & 0x00FFFFFF);
    }

    public static int lerpColor(int from, int to, float factor) {
        factor = Math.max(0f, Math.min(1f, factor));
        int a1 = (from >> 24) & 0xFF;
        int r1 = (from >> 16) & 0xFF;
        int g1 = (from >> 8) & 0xFF;
        int b1 = from & 0xFF;

        int a2 = (to >> 24) & 0xFF;
        int r2 = (to >> 16) & 0xFF;
        int g2 = (to >> 8) & 0xFF;
        int b2 = to & 0xFF;

        int a = (int) (a1 + (a2 - a1) * factor);
        int r = (int) (r1 + (r2 - r1) * factor);
        int g = (int) (g1 + (g2 - g1) * factor);
        int b = (int) (b1 + (b2 - b1) * factor);

        return rgba(r, g, b, a);
    }

    @Override
    public Theme clone() {
        try {
            return (Theme) super.clone();
        } catch (CloneNotSupportedException e) {
            Theme t = new Theme(this.name, this.dark);
            t.accent = this.accent;
            t.accentHover = this.accentHover;
            t.accentGlow = this.accentGlow;
            t.background = this.background;
            t.backgroundSecondary = this.backgroundSecondary;
            t.surface = this.surface;
            t.surfaceHover = this.surfaceHover;
            t.surfaceBorder = this.surfaceBorder;
            t.textPrimary = this.textPrimary;
            t.textSecondary = this.textSecondary;
            t.textMuted = this.textMuted;
            t.cardBg = this.cardBg;
            t.cardBorder = this.cardBorder;
            t.cardHover = this.cardHover;
            t.hudBg = this.hudBg;
            t.hudBorder = this.hudBorder;
            t.hudText = this.hudText;
            t.hudAccent = this.hudAccent;
            t.danger = this.danger;
            t.success = this.success;
            t.cornerRadiusSmall = this.cornerRadiusSmall;
            t.cornerRadiusMedium = this.cornerRadiusMedium;
            t.cornerRadiusLarge = this.cornerRadiusLarge;
            return t;
        }
    }
}
