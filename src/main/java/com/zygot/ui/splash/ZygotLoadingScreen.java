package com.zygot.ui.splash;

import com.zygot.Zygot;
import com.zygot.branding.BackgroundMode;
import com.zygot.branding.ClientBranding;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Magnificent modern splash / loading screen for Zygot Client.
 * Displays glowing brand identity, smooth progress track, and status tips.
 */
public class ZygotLoadingScreen {

    private static int progress = 0;
    private static int maxProgress = 10;
    private static String statusText = "Initializing...";
    private static final ResourceLocation SPLASH_BG = new ResourceLocation("zygot/textures/splash.png");
    private static final ResourceLocation LOGO = new ResourceLocation("zygot/textures/logo_white_nobg.png");

    public static void setProgress(int prog, int max, String status) {
        progress = prog;
        maxProgress = max;
        statusText = status;
        renderSplash();
    }

    public static void renderSplash() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.displayWidth <= 0 || mc.displayHeight <= 0) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();

        Framebuffer framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        framebuffer.bindFramebuffer(false);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // 1. Splash Screen Wallpaper Background
        Render2DHelper.drawTexturedRect(0, 0, width, height, SPLASH_BG);
        Render2DHelper.drawRect(0, 0, width, height, Theme.rgba(0, 0, 0, 75));

        // 2. Center Branding Logo
        float cx = width / 2f;
        float cy = height / 2f - 20;

        Render2DHelper.drawTexturedRect(cx - 32, cy - 75, 64, 64, LOGO);

        // 3. Progress Card
        float cardW = Math.min(320, width - 40);
        float cardH = 48;
        float cardX = cx - cardW / 2f;
        float cardY = cy + 20;

        Theme theme = Zygot.INSTANCE != null && Zygot.INSTANCE.themeManager != null ?
                Zygot.INSTANCE.themeManager.getCurrentTheme() : new Theme("Default", true);

        Render2DHelper.drawGlassPanel(cardX, cardY, cardW, cardH, 8, Theme.rgba(20, 20, 26, 210), Theme.rgba(139, 92, 246, 70), 1.2F);

        // Status Text
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.fontManager != null && Zygot.INSTANCE.fontManager.bold16 != null) {
            Zygot.INSTANCE.fontManager.bold16.drawString(statusText, cardX + 14, cardY + 10, 0xFFFFFFFF);
            String pct = (int) (((float) progress / (float) Math.max(1, maxProgress)) * 100) + "%";
            float pw = Zygot.INSTANCE.fontManager.bold16.getStringWidth(pct);
            Zygot.INSTANCE.fontManager.bold16.drawString(pct, cardX + cardW - pw - 14, cardY + 10, theme.accent);
        }

        // Progress Bar
        float barX = cardX + 14;
        float barY = cardY + 28;
        float barW = cardW - 28;
        float barH = 6;
        float fillW = barW * ((float) progress / (float) Math.max(1, maxProgress));

        Render2DHelper.drawRoundedRect(barX, barY, barW, barH, 3, Theme.rgba(35, 35, 45, 255));
        Render2DHelper.drawRoundedRect(barX, barY, fillW, barH, 3, theme.accent != 0 ? theme.accent : Theme.rgba(139, 92, 246, 255));

        // Tip text at bottom
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.fontManager != null && Zygot.INSTANCE.fontManager.regular14 != null) {
            Zygot.INSTANCE.fontManager.regular14.drawCenteredString(ClientBranding.LOADING_TIPS[0], cx, height - 26, Theme.rgba(160, 160, 185, 255));
        }

        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(mc.displayWidth, mc.displayHeight);

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        mc.updateDisplay();
    }
}
