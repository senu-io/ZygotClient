package com.zygot.ui.splash;

import com.zygot.ZygotClient;
import com.zygot.font.FontManager;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Modern Glassmorphism Splash / Loading Screen for Zygot Client.
 * Displays the custom splash artwork, a floating translucent dark glass panel,
 * progressive loading stages with Comfortaa typography, and an emerald green progress bar.
 */
public class ZygotSplashScreen {

    private static int splashTextureId = -1;
    private static int currentStage = 1;
    private static final int MAX_STAGES = 5;
    private static String currentStatus = "Initiating Zygot Client...";

    public static void setProgress(int stage, int max, String status) {
        currentStage = Math.max(1, Math.min(stage, max));
        currentStatus = status != null ? status : "Loading...";
        render();
    }

    public static void setProgress(int stage, String status) {
        setProgress(stage, MAX_STAGES, status);
    }

    public static void render() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.displayWidth <= 0 || mc.displayHeight <= 0) {
            return;
        }

        // Ensure OpenGL texture is loaded
        ensureSplashTexture();

        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();

        // Setup 2D Orthographic projection
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);

        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        // 1. Draw Splash Artwork Background
        if (splashTextureId != -1) {
            Render2DHelper.drawTexturedRect(0, 0, width, height, splashTextureId);
        } else {
            // Fallback dark gradient background
            Render2DHelper.drawGradientRect(0, 0, width, height,
                    Render2DHelper.rgba(18, 18, 24, 255),
                    Render2DHelper.rgba(10, 10, 14, 255), false);
        }

        // Subtle gradient vignette across the bottom to ground the card
        Render2DHelper.drawGradientRect(0, height - 160, width, 160,
                Render2DHelper.rgba(0, 0, 0, 0),
                Render2DHelper.rgba(0, 0, 0, 150), false);

        // 2. Glassmorphism Bottom Card
        float cardW = Math.min(520, width - 40);
        float cardH = 68;
        float cardX = (width - cardW) / 2f;
        float cardY = height - cardH - 26;
        float radius = 12f;

        // Frosted dark translucent glass panel with subtle glowing white edge
        int glassBg = Render2DHelper.rgba(14, 14, 20, 205); // 80% opacity dark glass
        int glassBorder = Render2DHelper.rgba(255, 255, 255, 30); // 12% opacity subtle glow
        Render2DHelper.drawGlassPanel(cardX, cardY, cardW, cardH, radius, glassBg, glassBorder, 1.2f);

        // 3. Inside Content: Status Text & Loading Bar
        float padX = 18f;
        float textY = cardY + 14f;
        float pct = ((float) currentStage / (float) MAX_STAGES) * 100f;
        String pctStr = ((int) pct) + "%";

        FontManager fontMgr = ZygotClient.INSTANCE != null ? ZygotClient.INSTANCE.getFontManager() : null;

        if (fontMgr != null && fontMgr.semiBold16 != null && fontMgr.bold16 != null) {
            // Render Status text in Comfortaa SemiBold
            fontMgr.semiBold16.drawString(currentStatus, cardX + padX, textY, 0xFFFFFFFF);

            // Render Percentage in Comfortaa Bold with emerald green accent
            float pctW = fontMgr.bold16.getStringWidth(pctStr);
            fontMgr.bold16.drawString(pctStr, cardX + cardW - padX - pctW, textY, 0xFF22C55E);
        } else if (mc.fontRendererObj != null) {
            // Safe fallback
            mc.fontRendererObj.drawString(currentStatus, (int) (cardX + padX), (int) textY, 0xFFFFFFFF);
            int pctW = mc.fontRendererObj.getStringWidth(pctStr);
            mc.fontRendererObj.drawString(pctStr, (int) (cardX + cardW - padX - pctW), (int) textY, 0xFF22C55E);
        }

        // 4. Loading Bar
        float barX = cardX + padX;
        float barY = cardY + 40f;
        float barW = cardW - (padX * 2);
        float barH = 8f;
        float barRadius = 4f;

        // Dark track background
        int trackColor = Render2DHelper.rgba(28, 28, 38, 240);
        Render2DHelper.drawRoundedRect(barX, barY, barW, barH, barRadius, trackColor);

        // Filled green progress portion
        float fillW = Math.max(barH, barW * (pct / 100f));
        int greenColor = Render2DHelper.rgba(34, 197, 94, 255); // Emerald Green #22C55E
        Render2DHelper.drawRoundedRect(barX, barY, fillW, barH, barRadius, greenColor);

        // Push frame to display
        Display.update();

        // Brief delay to make progressive stages visually perceptible and smooth
        try {
            Thread.sleep(150);
        } catch (InterruptedException ignored) {
        }
    }

    private static void ensureSplashTexture() {
        if (splashTextureId != -1) return;

        try (InputStream is = ZygotSplashScreen.class.getResourceAsStream("/assets/zygot/textures/splash.png")) {
            if (is != null) {
                BufferedImage image = ImageIO.read(is);
                int w = image.getWidth();
                int h = image.getHeight();
                int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);

                ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
                for (int pixel : pixels) {
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                    buffer.put((byte) (pixel & 0xFF));         // B
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
                buffer.flip();

                splashTextureId = GL11.glGenTextures();
                GlStateManager.bindTexture(splashTextureId);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                GlStateManager.bindTexture(0);
                ZygotClient.LOGGER.info("[Zygot Client] Splash screen texture loaded successfully (" + w + "x" + h + ").");
            } else {
                ZygotClient.LOGGER.warn("[Zygot Client] splash.png not found in classpath assets.");
            }
        } catch (Throwable t) {
            ZygotClient.LOGGER.error("[Zygot Client] Failed to load splash screen texture", t);
        }
    }

    public static void finish() {
        if (splashTextureId != -1) {
            GL11.glDeleteTextures(splashTextureId);
            splashTextureId = -1;
        }
    }
}
