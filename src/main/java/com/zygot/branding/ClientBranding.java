package com.zygot.branding;

/**
 * Centralized Branding Configuration for Zygot Client.
 * <p>
 * Modify the constants in this single file to completely change:
 * - Client Name
 * - Subtitle / Tagline
 * - Version & MC Version
 * - Authors / "Made by" Credits
 * - Window Title format
 * - Background rendering mode (Animated Particles, Panorama, Gradient, or Static Image)
 * - Texture / Asset paths
 * - Links (Discord, Website, Store)
 * - Loading & Splash Screen messages
 */
public class ClientBranding {

    // ==========================================
    // Core Identity & Attribution
    // ==========================================
    public static String NAME = "Zygot";
    public static String TAGLINE = "The Next Generation 1.8.9 PvP Client";
    public static String VERSION = "1.0.0";
    public static String MC_VERSION = "1.8.9";
    public static String AUTHORS = "Zygot Development Team";
    public static String COPYRIGHT = "Copyright \u00A9 2026 Zygot Client. All rights reserved.";

    // Window title format. Available tokens: %name%, %version%, %mcversion%
    public static String WINDOW_TITLE_FORMAT = "%name% Client %version% | %mcversion%";

    // ==========================================
    // Visual & Background Styling
    // ==========================================
    /**
     * Active background mode for Main Menu and Loading Screens.
     * Options:
     * - BackgroundMode.ANIMATED_PARTICLES (Default: sleek modern particle web)
     * - BackgroundMode.STATIC_IMAGE       (Uses STATIC_WALLPAPER_PATH)
     * - BackgroundMode.FLOWING_GRADIENT   (Smooth animated shader-like gradient wave)
     * - BackgroundMode.ANIMATED_PANORAMA  (Rotating skybox panorama)
     */
    public static BackgroundMode DEFAULT_BACKGROUND_MODE = BackgroundMode.STATIC_IMAGE;

    // Static Wallpaper Texture Location (used when BackgroundMode is STATIC_IMAGE or fallback)
    public static String STATIC_WALLPAPER_PATH = "zygot/textures/main_menu.png";

    // Splash / Loading Screen Background Location
    public static String SPLASH_WALLPAPER_PATH = "zygot/textures/splash.png";

    // Logo Texture Locations
    public static String LOGO_PATH = "zygot/textures/logo.png";
    public static String LOGO_WHITE_PATH = "zygot/textures/logo_white_nobg.png";
    public static String LOGO_DARK_PATH = "zygot/textures/logo_dark_nobg.png";

    // Watermark Icon Location
    public static String WATERMARK_ICON_PATH = "zygot/textures/logo_white_nobg.png";

    // Default TTF font family name from assets/zygot/fonts/
    public static String DEFAULT_FONT_NAME = "Comfortaa";

    /**
     * Returns the appropriate logo texture path depending on theme contrast.
     */
    public static String getThemeLogoPath(boolean isDark) {
        return isDark ? LOGO_WHITE_PATH : LOGO_DARK_PATH;
    }

    // ==========================================
    // Community & Social Links
    // ==========================================
    public static String DISCORD_URL = "https://discord.gg/zygot";
    public static String WEBSITE_URL = "https://zygotclient.com";
    public static String GITHUB_URL = "https://github.com/ZygotClient";

    // ==========================================
    // Loading Screen Messages & Tips
    // ==========================================
    public static String LOADING_TITLE = "Zygot Client";
    public static String LOADING_SUBTITLE = "Loading high performance PvP modules...";
    public static String[] LOADING_TIPS = new String[] {
            "Tip: Press Right Shift to open the interactive HUD Editor.",
            "Tip: Customize your theme, accent colors, and HUD in the Mod Menu.",
            "Tip: 1.7 Animations allow block-hitting just like in 1.7.10!",
            "Tip: Visit the 3D Wardrobe to equip custom capes, animated wings, and halos.",
            "Tip: Toggle Particle Culling and Fast Math for a significant FPS boost in PvP."
    };

    // ==========================================
    // Utility Methods
    // ==========================================

    /**
     * Generates formatted window title.
     */
    public static String getFormattedWindowTitle() {
        return WINDOW_TITLE_FORMAT
                .replace("%name%", NAME)
                .replace("%version%", VERSION)
                .replace("%mcversion%", MC_VERSION);
    }

    /**
     * Generates watermark string for HUD.
     */
    public static String getWatermarkText() {
        return NAME + " Client v" + VERSION;
    }

    /**
     * Generates attribution string for main menu footer.
     */
    public static String getAttributionText() {
        return NAME + " Client v" + VERSION + " \u2022 Made by " + AUTHORS;
    }
}
