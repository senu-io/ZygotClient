package com.zygot.branding;

/**
 * Defines the background rendering mode for Main Menu and Loading Screens.
 * Allows effortless switching between animated visual effects and static wallpapers.
 */
public enum BackgroundMode {
    /**
     * Modern interactive animated particle mesh with connecting lines and glowing nodes.
     */
    ANIMATED_PARTICLES("Animated Particles"),

    /**
     * Classic rotating 3D Minecraft panoramic skybox.
     */
    ANIMATED_PANORAMA("Animated Panorama"),

    /**
     * Sleek flowing dynamic color gradient wave.
     */
    FLOWING_GRADIENT("Flowing Gradient"),

    /**
     * High-resolution static wallpaper image (e.g., assets/zygot/textures/main_menu.jpg).
     */
    STATIC_IMAGE("Static Image");

    private final String displayName;

    BackgroundMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
