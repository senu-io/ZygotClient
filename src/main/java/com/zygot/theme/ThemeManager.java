package com.zygot.theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages active UI themes and built-in theme presets.
 */
public class ThemeManager {

    private final List<Theme> themes = new ArrayList<>();
    private Theme currentTheme;

    public ThemeManager() {
        initThemes();
    }

    private void initThemes() {
        // 1. Zygot Violet (Default Signature Theme)
        Theme violet = new Theme("Zygot Violet", true);
        violet.accent = Theme.rgba(139, 92, 246, 255);      // #8B5CF6 Electric Violet
        violet.accentHover = Theme.rgba(167, 139, 250, 255); // #A78BFA
        violet.accentGlow = Theme.rgba(139, 92, 246, 120);
        violet.background = Theme.rgba(17, 17, 22, 230);     // #111116 Frosted Glass
        violet.backgroundSecondary = Theme.rgba(24, 24, 32, 240);
        violet.surface = Theme.rgba(28, 28, 38, 190);        // Translucent card surface
        violet.surfaceHover = Theme.rgba(36, 36, 48, 220);
        violet.surfaceBorder = Theme.rgba(139, 92, 246, 60); // Subtle violet edge
        violet.textPrimary = Theme.rgba(255, 255, 255, 255);
        violet.textSecondary = Theme.rgba(160, 160, 185, 255);
        violet.textMuted = Theme.rgba(110, 110, 135, 255);
        violet.cardBg = Theme.rgba(22, 22, 30, 200);
        violet.cardBorder = Theme.rgba(255, 255, 255, 18);
        violet.cardHover = Theme.rgba(32, 32, 44, 220);
        violet.hudBg = Theme.rgba(17, 17, 22, 160);
        violet.hudBorder = Theme.rgba(139, 92, 246, 100);
        violet.hudText = Theme.rgba(255, 255, 255, 255);
        violet.hudAccent = violet.accent;
        violet.danger = Theme.rgba(239, 68, 68, 255);
        violet.success = Theme.rgba(34, 197, 94, 255);
        themes.add(violet);

        // 2. Midnight Cyber
        Theme cyber = new Theme("Midnight Cyber", true);
        cyber.accent = Theme.rgba(0, 242, 254, 255);        // #00F2FE Neon Cyan
        cyber.accentHover = Theme.rgba(79, 172, 254, 255);
        cyber.accentGlow = Theme.rgba(0, 242, 254, 120);
        cyber.background = Theme.rgba(10, 14, 23, 235);     // Deep Navy
        cyber.backgroundSecondary = Theme.rgba(15, 22, 36, 240);
        cyber.surface = Theme.rgba(18, 26, 42, 190);
        cyber.surfaceHover = Theme.rgba(24, 35, 56, 220);
        cyber.surfaceBorder = Theme.rgba(0, 242, 254, 70);
        cyber.textPrimary = Theme.rgba(240, 248, 255, 255);
        cyber.textSecondary = Theme.rgba(140, 165, 195, 255);
        cyber.textMuted = Theme.rgba(90, 115, 145, 255);
        cyber.cardBg = Theme.rgba(14, 20, 32, 200);
        cyber.cardBorder = Theme.rgba(0, 242, 254, 30);
        cyber.cardHover = Theme.rgba(20, 30, 48, 220);
        cyber.hudBg = Theme.rgba(10, 14, 23, 160);
        cyber.hudBorder = Theme.rgba(0, 242, 254, 100);
        cyber.hudText = Theme.rgba(240, 248, 255, 255);
        cyber.hudAccent = cyber.accent;
        cyber.danger = Theme.rgba(255, 75, 75, 255);
        cyber.success = Theme.rgba(0, 230, 150, 255);
        themes.add(cyber);

        // 3. Crimson Blood
        Theme crimson = new Theme("Crimson Blood", true);
        crimson.accent = Theme.rgba(255, 51, 102, 255);     // #FF3366 Fiery Crimson
        crimson.accentHover = Theme.rgba(255, 101, 132, 255);
        crimson.accentGlow = Theme.rgba(255, 51, 102, 120);
        crimson.background = Theme.rgba(20, 18, 22, 235);
        crimson.backgroundSecondary = Theme.rgba(28, 24, 30, 240);
        crimson.surface = Theme.rgba(34, 28, 36, 190);
        crimson.surfaceHover = Theme.rgba(44, 36, 48, 220);
        crimson.surfaceBorder = Theme.rgba(255, 51, 102, 60);
        crimson.textPrimary = Theme.rgba(255, 255, 255, 255);
        crimson.textSecondary = Theme.rgba(185, 160, 165, 255);
        crimson.textMuted = Theme.rgba(135, 110, 115, 255);
        crimson.cardBg = Theme.rgba(26, 22, 28, 200);
        crimson.cardBorder = Theme.rgba(255, 51, 102, 30);
        crimson.cardHover = Theme.rgba(38, 32, 42, 220);
        crimson.hudBg = Theme.rgba(20, 18, 22, 160);
        crimson.hudBorder = Theme.rgba(255, 51, 102, 100);
        crimson.hudText = Theme.rgba(255, 255, 255, 255);
        crimson.hudAccent = crimson.accent;
        crimson.danger = Theme.rgba(255, 50, 50, 255);
        crimson.success = Theme.rgba(46, 213, 115, 255);
        themes.add(crimson);

        // 4. Emerald PvP
        Theme emerald = new Theme("Emerald PvP", true);
        emerald.accent = Theme.rgba(16, 185, 129, 255);     // #10B981 Mint Emerald
        emerald.accentHover = Theme.rgba(52, 211, 153, 255);
        emerald.accentGlow = Theme.rgba(16, 185, 129, 120);
        emerald.background = Theme.rgba(13, 22, 18, 235);
        emerald.backgroundSecondary = Theme.rgba(18, 30, 24, 240);
        emerald.surface = Theme.rgba(22, 38, 30, 190);
        emerald.surfaceHover = Theme.rgba(30, 48, 38, 220);
        emerald.surfaceBorder = Theme.rgba(16, 185, 129, 60);
        emerald.textPrimary = Theme.rgba(240, 255, 245, 255);
        emerald.textSecondary = Theme.rgba(145, 185, 165, 255);
        emerald.textMuted = Theme.rgba(95, 135, 115, 255);
        emerald.cardBg = Theme.rgba(16, 28, 22, 200);
        emerald.cardBorder = Theme.rgba(16, 185, 129, 30);
        emerald.cardHover = Theme.rgba(24, 40, 32, 220);
        emerald.hudBg = Theme.rgba(13, 22, 18, 160);
        emerald.hudBorder = Theme.rgba(16, 185, 129, 100);
        emerald.hudText = Theme.rgba(240, 255, 245, 255);
        emerald.hudAccent = emerald.accent;
        emerald.danger = Theme.rgba(239, 68, 68, 255);
        emerald.success = Theme.rgba(16, 185, 129, 255);
        themes.add(emerald);

        // 5. Sunset Amber
        Theme amber = new Theme("Sunset Amber", true);
        amber.accent = Theme.rgba(245, 158, 11, 255);       // #F59E0B Vibrant Amber
        amber.accentHover = Theme.rgba(251, 191, 36, 255);
        amber.accentGlow = Theme.rgba(245, 158, 11, 120);
        amber.background = Theme.rgba(22, 18, 14, 235);
        amber.backgroundSecondary = Theme.rgba(30, 24, 18, 240);
        amber.surface = Theme.rgba(38, 30, 22, 190);
        amber.surfaceHover = Theme.rgba(48, 38, 28, 220);
        amber.surfaceBorder = Theme.rgba(245, 158, 11, 60);
        amber.textPrimary = Theme.rgba(255, 250, 240, 255);
        amber.textSecondary = Theme.rgba(195, 175, 150, 255);
        amber.textMuted = Theme.rgba(145, 125, 100, 255);
        amber.cardBg = Theme.rgba(28, 22, 16, 200);
        amber.cardBorder = Theme.rgba(245, 158, 11, 30);
        amber.cardHover = Theme.rgba(40, 32, 24, 220);
        amber.hudBg = Theme.rgba(22, 18, 14, 160);
        amber.hudBorder = Theme.rgba(245, 158, 11, 100);
        amber.hudText = Theme.rgba(255, 250, 240, 255);
        amber.hudAccent = amber.accent;
        amber.danger = Theme.rgba(239, 68, 68, 255);
        amber.success = Theme.rgba(34, 197, 94, 255);
        themes.add(amber);

        // 6. Clean Minimal Light
        Theme light = new Theme("Clean Light", false);
        light.accent = Theme.rgba(124, 58, 237, 255);       // #7C3AED Deep Purple Accent
        light.accentHover = Theme.rgba(139, 92, 246, 255);
        light.accentGlow = Theme.rgba(124, 58, 237, 80);
        light.background = Theme.rgba(243, 244, 246, 245);  // Soft off-white
        light.backgroundSecondary = Theme.rgba(229, 231, 235, 250);
        light.surface = Theme.rgba(255, 255, 255, 220);
        light.surfaceHover = Theme.rgba(249, 250, 251, 240);
        light.surfaceBorder = Theme.rgba(209, 213, 219, 160);
        light.textPrimary = Theme.rgba(17, 24, 39, 255);
        light.textSecondary = Theme.rgba(75, 85, 99, 255);
        light.textMuted = Theme.rgba(156, 163, 175, 255);
        light.cardBg = Theme.rgba(255, 255, 255, 230);
        light.cardBorder = Theme.rgba(229, 231, 235, 200);
        light.cardHover = Theme.rgba(243, 244, 246, 240);
        light.hudBg = Theme.rgba(255, 255, 255, 180);
        light.hudBorder = Theme.rgba(124, 58, 237, 100);
        light.hudText = Theme.rgba(17, 24, 39, 255);
        light.hudAccent = light.accent;
        light.danger = Theme.rgba(220, 38, 38, 255);
        light.success = Theme.rgba(22, 163, 74, 255);
        themes.add(light);

        // Set default to Zygot Violet
        currentTheme = violet;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        if (theme != null) {
            this.currentTheme = theme;
        }
    }

    public void setThemeByName(String name) {
        for (Theme t : themes) {
            if (t.name.equalsIgnoreCase(name)) {
                this.currentTheme = t;
                return;
            }
        }
    }

    public List<Theme> getThemes() {
        return themes;
    }
}
