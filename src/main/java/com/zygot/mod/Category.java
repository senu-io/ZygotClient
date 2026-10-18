package com.zygot.mod;

public enum Category {
    PVP("PvP"),
    HUD("HUD"),
    RENDER("Render"),
    PERFORMANCE("Performance"),
    QOL("QoL"),
    COSMETICS("Cosmetics");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
