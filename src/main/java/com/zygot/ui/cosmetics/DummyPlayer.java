package com.zygot.ui.cosmetics;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * An offline dummy player entity used for rendering full 3D cosmetics,
 * skin previews, and animations in menus when no world or server is loaded.
 */
public class DummyPlayer extends AbstractClientPlayer {

    private ResourceLocation skinLocation;
    private String skinType = "default";

    public DummyPlayer(GameProfile profile) {
        super(null, profile != null ? profile : Minecraft.getMinecraft().getSession().getProfile());
        loadSkin();
    }

    private void loadSkin() {
        Minecraft mc = Minecraft.getMinecraft();
        SkinManager skinManager = mc.getSkinManager();
        GameProfile profile = getGameProfile();

        if (skinManager != null && profile != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.loadSkinFromCache(profile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                MinecraftProfileTexture tex = map.get(MinecraftProfileTexture.Type.SKIN);
                this.skinLocation = skinManager.loadSkin(tex, MinecraftProfileTexture.Type.SKIN);
                String modelType = tex.getMetadata("model");
                if (modelType != null) {
                    this.skinType = modelType;
                }
            } else {
                skinManager.loadProfileTextures(profile, new SkinManager.SkinAvailableCallback() {
                    @Override
                    public void skinAvailable(MinecraftProfileTexture.Type type, ResourceLocation location, MinecraftProfileTexture profileTexture) {
                        if (type == MinecraftProfileTexture.Type.SKIN) {
                            DummyPlayer.this.skinLocation = location;
                            String modelType = profileTexture.getMetadata("model");
                            if (modelType != null) {
                                DummyPlayer.this.skinType = modelType;
                            }
                        }
                    }
                }, true);
            }
        }
    }

    @Override
    public ResourceLocation getLocationSkin() {
        return skinLocation != null ? skinLocation : DefaultPlayerSkin.getDefaultSkin(getUniqueID());
    }

    @Override
    public String getSkinType() {
        return skinType != null ? skinType : DefaultPlayerSkin.getSkinType(getUniqueID());
    }

    @Override
    public boolean hasPlayerInfo() {
        return false;
    }

    @Override
    protected NetworkPlayerInfo getPlayerInfo() {
        return null;
    }

    @Override
    public boolean isWearing(EnumPlayerModelParts part) {
        return true;
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
}
