package com.zygot.util.system;

import com.zygot.ZygotClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Detects whether the host operating system (Windows) is currently using
 * Light Theme or Dark Theme for apps and system shell.
 */
public class WindowsThemeHelper {

    private static Boolean isLightTheme = null;

    /**
     * Checks if Windows is configured to use Light Theme.
     * Returns true if light theme is active, false if dark theme or non-Windows.
     */
    public static boolean isWindowsLightTheme() {
        if (isLightTheme != null) {
            return isLightTheme;
        }

        String os = System.getProperty("os.name");
        if (os == null || !os.toLowerCase().contains("win")) {
            isLightTheme = false;
            return false;
        }

        try {
            Process process = Runtime.getRuntime().exec(
                    "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme"
            );
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        // Example value: "    AppsUseLightTheme    REG_DWORD    0x1"
                        if (line.contains("0x1")) {
                            isLightTheme = true;
                            ZygotClient.LOGGER.info("[Zygot Client] Detected Windows Light Theme.");
                            return true;
                        } else if (line.contains("0x0")) {
                            isLightTheme = false;
                            ZygotClient.LOGGER.info("[Zygot Client] Detected Windows Dark Theme.");
                            return false;
                        }
                    }
                }
            }
            process.waitFor();
        } catch (Throwable t) {
            ZygotClient.LOGGER.warn("[Zygot Client] Could not query Windows theme registry: " + t.getMessage());
        }

        // Default to dark theme (white logo) if unable to detect
        isLightTheme = false;
        return false;
    }
}
