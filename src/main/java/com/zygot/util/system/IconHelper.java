package com.zygot.util.system;

import com.zygot.ZygotClient;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles dynamic window title and taskbar icon loading.
 * Automatically adapts between dark logo (for Light Theme Windows) and
 * white logo (for Dark Theme Windows), with multi-resolution scaling for LWJGL.
 */
public class IconHelper {

    private static final int[] ICON_SIZES = new int[]{16, 32, 48, 128, 256};
    private static final String WINDOW_TITLE = "Zygot Client 1.8.9";

    public static void updateDisplay() {
        // 1. Set clean window title
        Display.setTitle(WINDOW_TITLE);

        // 2. Set taskbar and window icons based on theme
        try {
            boolean isLight = WindowsThemeHelper.isWindowsLightTheme();
            String themePrefix = isLight ? "dark" : "white";
            ZygotClient.LOGGER.info("[Zygot Client] Loading " + themePrefix + " logo icon for taskbar...");

            List<ByteBuffer> iconBuffers = new ArrayList<>();

            for (int size : ICON_SIZES) {
                String path = "/assets/zygot/icons/icon_" + themePrefix + "_" + size + ".png";
                BufferedImage img = null;

                try (InputStream is = IconHelper.class.getResourceAsStream(path)) {
                    if (is != null) {
                        img = ImageIO.read(is);
                    }
                } catch (Exception ignored) {
                }

                // If pre-generated resolution not found, scale from base logo
                if (img == null) {
                    String basePath = "/assets/zygot/textures/logo_" + themePrefix + "_nobg.png";
                    try (InputStream baseIs = IconHelper.class.getResourceAsStream(basePath)) {
                        if (baseIs != null) {
                            BufferedImage baseImg = ImageIO.read(baseIs);
                            img = resizeImage(baseImg, size, size);
                        }
                    }
                }

                if (img != null) {
                    iconBuffers.add(readImageToBuffer(img));
                }
            }

            if (!iconBuffers.isEmpty()) {
                Display.setIcon(iconBuffers.toArray(new ByteBuffer[0]));
                ZygotClient.LOGGER.info("[Zygot Client] Taskbar icon successfully applied (" + iconBuffers.size() + " resolutions).");
            }
        } catch (Throwable t) {
            ZygotClient.LOGGER.error("[Zygot Client] Failed to set window icon", t);
        }
    }

    private static BufferedImage resizeImage(BufferedImage src, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    private static ByteBuffer readImageToBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * rgb.length);

        for (int pixel : rgb) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
            buffer.put((byte) (pixel & 0xFF));         // Blue
            buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
        }

        buffer.flip();
        return buffer;
    }
}
