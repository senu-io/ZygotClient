package com.zygot.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all 3D cosmetics in Zygot Client.
 */
public abstract class Cosmetic {

    private final String id;
    private final String name;
    private final CosmeticType type;
    private final ResourceLocation texture;

    public Cosmetic(String id, String name, CosmeticType type, ResourceLocation texture) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.texture = texture;
    }

    /**
     * Renders the cosmetic onto the player model.
     */
    public abstract void render(AbstractClientPlayer player, ModelPlayer model, float partialTicks);

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CosmeticType getType() {
        return type;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
