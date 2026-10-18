package com.zygot.branding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Manages external runtime profile overrides for client branding and themes.
 * If a 'zygot_profile.json' is present in the game directory, it will automatically
 * override client branding properties without requiring any code modification or recompilation.
 */
public class ZygotProfile {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load(File profileFile) {
        if (profileFile == null || !profileFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(profileFile)) {
            JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();

            if (obj.has("clientName")) {
                ClientBranding.NAME = obj.get("clientName").getAsString();
            }
            if (obj.has("tagline")) {
                ClientBranding.TAGLINE = obj.get("tagline").getAsString();
            }
            if (obj.has("version")) {
                ClientBranding.VERSION = obj.get("version").getAsString();
            }
            if (obj.has("authors")) {
                ClientBranding.AUTHORS = obj.get("authors").getAsString();
            }
            if (obj.has("windowTitleFormat")) {
                ClientBranding.WINDOW_TITLE_FORMAT = obj.get("windowTitleFormat").getAsString();
            }
            if (obj.has("backgroundMode")) {
                try {
                    ClientBranding.DEFAULT_BACKGROUND_MODE = BackgroundMode.valueOf(obj.get("backgroundMode").getAsString().toUpperCase());
                } catch (Exception ignored) {}
            }
            if (obj.has("staticWallpaperPath")) {
                ClientBranding.STATIC_WALLPAPER_PATH = obj.get("staticWallpaperPath").getAsString();
            }
            if (obj.has("discordUrl")) {
                ClientBranding.DISCORD_URL = obj.get("discordUrl").getAsString();
            }
            if (obj.has("websiteUrl")) {
                ClientBranding.WEBSITE_URL = obj.get("websiteUrl").getAsString();
            }
        } catch (Exception e) {
            System.err.println("[Zygot] Failed to load external zygot_profile.json: " + e.getMessage());
        }
    }

    public static void saveSample(File profileFile) {
        if (profileFile == null || profileFile.exists()) return;

        try {
            if (profileFile.getParentFile() != null) {
                profileFile.getParentFile().mkdirs();
            }
            JsonObject obj = new JsonObject();
            obj.addProperty("clientName", ClientBranding.NAME);
            obj.addProperty("tagline", ClientBranding.TAGLINE);
            obj.addProperty("version", ClientBranding.VERSION);
            obj.addProperty("authors", ClientBranding.AUTHORS);
            obj.addProperty("windowTitleFormat", ClientBranding.WINDOW_TITLE_FORMAT);
            obj.addProperty("backgroundMode", ClientBranding.DEFAULT_BACKGROUND_MODE.name());
            obj.addProperty("staticWallpaperPath", ClientBranding.STATIC_WALLPAPER_PATH);
            obj.addProperty("discordUrl", ClientBranding.DISCORD_URL);
            obj.addProperty("websiteUrl", ClientBranding.WEBSITE_URL);

            try (FileWriter writer = new FileWriter(profileFile)) {
                GSON.toJson(obj, writer);
            }
        } catch (Exception e) {
            System.err.println("[Zygot] Failed to write sample zygot_profile.json: " + e.getMessage());
        }
    }
}
