package com.zygot.cosmetics;

public enum CosmeticType {
    CAPE("Capes"),
    WINGS("Wings"),
    HAT("Hats"),
    HALO("Halos"),
    BANDANA("Bandanas");

    private final String displayName;

    CosmeticType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
