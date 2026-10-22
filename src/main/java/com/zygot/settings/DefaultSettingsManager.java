package com.zygot.settings;

import com.zygot.ZygotClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Manages default video and OptiFine performance/quality settings
 * applied on first launch of Zygot Client.
 */
public class DefaultSettingsManager {

    private static final String MARKER_FILE = "zygot/.defaults_applied";

    public static void initialize(File mcDataDir) {
        File marker = new File(mcDataDir, MARKER_FILE);
        if (marker.exists()) {
            return;
        }

        ZygotClient.LOGGER.info("[Zygot Client] Applying optimized default video and OptiFine settings...");

        if (marker.getParentFile() != null) {
            marker.getParentFile().mkdirs();
        }

        File optionsFile = new File(mcDataDir, "options.txt");
        File optionsOfFile = new File(mcDataDir, "optionsof.txt");

        // 1. If options.txt does not exist, copy from bundled defaults
        if (!optionsFile.exists()) {
            copyDefaultResource("/assets/zygot/defaults/options.txt", optionsFile);
        }

        // 2. If optionsof.txt does not exist, copy from bundled defaults
        if (!optionsOfFile.exists()) {
            copyDefaultResource("/assets/zygot/defaults/optionsof.txt", optionsOfFile);
        }

        // 3. Apply baseline settings to active GameSettings
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.gameSettings != null) {
            applyDefaultVideoSettings(mc.gameSettings);
            mc.gameSettings.saveOptions();
        }

        // 4. Mark defaults as applied so subsequent user customizations are preserved
        try {
            marker.createNewFile();
            ZygotClient.LOGGER.info("[Zygot Client] Default settings successfully applied and saved.");
        } catch (IOException e) {
            ZygotClient.LOGGER.error("[Zygot Client] Failed to create defaults marker file", e);
        }
    }

    private static void applyDefaultVideoSettings(GameSettings s) {
        s.fancyGraphics = true;
        s.renderDistanceChunks = 12;
        s.ambientOcclusion = 2; // Maximum
        s.limitFramerate = 260; // Unlimited
        s.guiScale = 2; // Normal
        s.useVbo = true;
        s.gammaSetting = 0.0F; // Moody
        s.particleSetting = 1; // Decreased
        s.heldItemTooltips = true;
        s.allowBlockAlternatives = true;
        s.entityShadows = true;
        s.mipmapLevels = 4; // Maximum
        s.viewBobbing = true;
    }

    private static void copyDefaultResource(String resourcePath, File destination) {
        try (InputStream in = DefaultSettingsManager.class.getResourceAsStream(resourcePath);
             OutputStream out = new FileOutputStream(destination)) {
            if (in != null) {
                IOUtils.copy(in, out);
            }
        } catch (IOException e) {
            ZygotClient.LOGGER.error("[Zygot Client] Failed to copy default resource: " + resourcePath, e);
        }
    }
}
