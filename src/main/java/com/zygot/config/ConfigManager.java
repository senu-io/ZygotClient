package com.zygot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zygot.Zygot;
import com.zygot.cosmetics.CosmeticType;
import com.zygot.mod.HudMod;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.*;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

/**
 * Manages JSON configuration saving and loading for Zygot Client.
 * Automatically persists mod states, HUD layout coordinates, settings values,
 * active themes, and equipped cosmetics.
 */
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;

    public ConfigManager() {
        File dir = new File(Minecraft.getMinecraft().mcDataDir, "zygot");
        dir.mkdirs();
        this.configFile = new File(dir, "config.json");
    }

    public void saveConfig() {
        try {
            JsonObject root = new JsonObject();

            // 1. Save Active Theme
            if (Zygot.INSTANCE.themeManager != null && Zygot.INSTANCE.themeManager.getCurrentTheme() != null) {
                root.addProperty("theme", Zygot.INSTANCE.themeManager.getCurrentTheme().name);
            }

            // 2. Save Equipped Cosmetics
            if (Zygot.INSTANCE.cosmeticManager != null) {
                JsonObject cosmeticsObj = new JsonObject();
                for (Map.Entry<CosmeticType, String> entry : Zygot.INSTANCE.cosmeticManager.getEquippedMap().entrySet()) {
                    cosmeticsObj.addProperty(entry.getKey().name(), entry.getValue());
                }
                root.add("cosmetics", cosmeticsObj);
            }

            // 3. Save Mods & Settings
            JsonObject modsObj = new JsonObject();
            if (Zygot.INSTANCE.modManager != null) {
                for (Mod mod : Zygot.INSTANCE.modManager.getMods()) {
                    JsonObject modObj = new JsonObject();
                    modObj.addProperty("toggled", mod.isToggled());
                    modObj.addProperty("keybind", mod.getKeybind());

                    if (mod instanceof HudMod) {
                        HudMod hud = (HudMod) mod;
                        modObj.addProperty("x", hud.getX());
                        modObj.addProperty("y", hud.getY());
                    }

                    // Save settings
                    JsonObject settingsObj = new JsonObject();
                    for (Setting s : mod.getSettings()) {
                        if (s instanceof BooleanSetting) {
                            settingsObj.addProperty(s.getName(), ((BooleanSetting) s).isEnabled());
                        } else if (s instanceof NumberSetting) {
                            settingsObj.addProperty(s.getName(), ((NumberSetting) s).getValue());
                        } else if (s instanceof ModeSetting) {
                            settingsObj.addProperty(s.getName(), ((ModeSetting) s).getMode());
                        } else if (s instanceof ColorSetting) {
                            settingsObj.addProperty(s.getName(), ((ColorSetting) s).getColor());
                        } else if (s instanceof KeybindSetting) {
                            settingsObj.addProperty(s.getName(), ((KeybindSetting) s).getKeyCode());
                        }
                    }
                    modObj.add("settings", settingsObj);

                    modsObj.add(mod.getName(), modObj);
                }
            }
            root.add("mods", modsObj);

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(root, writer);
            }
        } catch (Exception e) {
            System.err.println("[Zygot] Failed to save config: " + e.getMessage());
        }
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            saveConfig();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject root = new JsonParser().parse(reader).getAsJsonObject();

            // 1. Load Theme
            if (root.has("theme") && Zygot.INSTANCE.themeManager != null) {
                Zygot.INSTANCE.themeManager.setThemeByName(root.get("theme").getAsString());
            }

            // 2. Load Cosmetics
            if (root.has("cosmetics") && Zygot.INSTANCE.cosmeticManager != null) {
                JsonObject cosmeticsObj = root.getAsJsonObject("cosmetics");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : cosmeticsObj.entrySet()) {
                    try {
                        CosmeticType type = CosmeticType.valueOf(entry.getKey());
                        Zygot.INSTANCE.cosmeticManager.equip(entry.getValue().getAsString());
                    } catch (Exception ignored) {}
                }
            }

            // 3. Load Mods & Settings
            if (root.has("mods") && Zygot.INSTANCE.modManager != null) {
                JsonObject modsObj = root.getAsJsonObject("mods");
                for (Mod mod : Zygot.INSTANCE.modManager.getMods()) {
                    if (modsObj.has(mod.getName())) {
                        JsonObject modObj = modsObj.getAsJsonObject(mod.getName());
                        if (modObj.has("toggled")) {
                            mod.setToggled(modObj.get("toggled").getAsBoolean());
                        }
                        if (modObj.has("keybind")) {
                            mod.setKeybind(modObj.get("keybind").getAsInt());
                        }
                        if (mod instanceof HudMod) {
                            HudMod hud = (HudMod) mod;
                            if (modObj.has("x")) hud.setX(modObj.get("x").getAsFloat());
                            if (modObj.has("y")) hud.setY(modObj.get("y").getAsFloat());
                        }

                        if (modObj.has("settings")) {
                            JsonObject settingsObj = modObj.getAsJsonObject("settings");
                            for (Setting s : mod.getSettings()) {
                                if (settingsObj.has(s.getName())) {
                                    if (s instanceof BooleanSetting) {
                                        ((BooleanSetting) s).setEnabled(settingsObj.get(s.getName()).getAsBoolean());
                                    } else if (s instanceof NumberSetting) {
                                        ((NumberSetting) s).setValue(settingsObj.get(s.getName()).getAsDouble());
                                    } else if (s instanceof ModeSetting) {
                                        ((ModeSetting) s).setMode(settingsObj.get(s.getName()).getAsString());
                                    } else if (s instanceof ColorSetting) {
                                        ((ColorSetting) s).setColor(settingsObj.get(s.getName()).getAsInt());
                                    } else if (s instanceof KeybindSetting) {
                                        ((KeybindSetting) s).setKeyCode(settingsObj.get(s.getName()).getAsInt());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Zygot] Failed to load config: " + e.getMessage());
        }
    }
}
