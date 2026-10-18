package com.zygot.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * High quality 2D rendering utility for Zygot Client.
 * Implements anti-aliased rounded rectangles, translucent glassmorphism surfaces,
 * smooth gradient fills, circular nodes, and scissor clipping.
 */
public class Render2DHelper {

    public static void color(int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, a);
    }

    public static void drawRect(float x, float y, float width, float height, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        color(color);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x, y + height, 0.0D).endVertex();
        wr.pos(x + width, y + height, 0.0D).endVertex();
        wr.pos(x + width, y, 0.0D).endVertex();
        wr.pos(x, y, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws an anti-aliased smooth rounded rectangle.
     */
    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        if (radius <= 0) {
            drawRect(x, y, width, height, color);
            return;
        }

        radius = Math.min(radius, Math.min(width / 2f, height / 2f));

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        color(color);

        GL11.glBegin(GL11.GL_POLYGON);

        // Top-right corner (270 to 360 deg)
        for (int i = 270; i <= 360; i += 6) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + width - radius + Math.cos(rad) * radius, y + radius + Math.sin(rad) * radius);
        }

        // Bottom-right corner (0 to 90 deg)
        for (int i = 0; i <= 90; i += 6) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + width - radius + Math.cos(rad) * radius, y + height - radius + Math.sin(rad) * radius);
        }

        // Bottom-left corner (90 to 180 deg)
        for (int i = 90; i <= 180; i += 6) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + radius + Math.cos(rad) * radius, y + height - radius + Math.sin(rad) * radius);
        }

        // Top-left corner (180 to 270 deg)
        for (int i = 180; i <= 270; i += 6) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + radius + Math.cos(rad) * radius, y + radius + Math.sin(rad) * radius);
        }

        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws an outlined rounded rectangle.
     */
    public static void drawRoundedOutline(float x, float y, float width, float height, float radius, float lineWidth, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);
        color(color);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        // Top edge and top-right corner
        for (int i = 270; i <= 360; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + width - radius + Math.cos(rad) * radius, y + radius + Math.sin(rad) * radius);
        }
        // Right edge and bottom-right corner
        for (int i = 0; i <= 90; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + width - radius + Math.cos(rad) * radius, y + height - radius + Math.sin(rad) * radius);
        }
        // Bottom edge and bottom-left corner
        for (int i = 90; i <= 180; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + radius + Math.cos(rad) * radius, y + height - radius + Math.sin(rad) * radius);
        }
        // Left edge and top-left corner
        for (int i = 180; i <= 270; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(x + radius + Math.cos(rad) * radius, y + radius + Math.sin(rad) * radius);
        }
        GL11.glEnd();

        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a Feather/Lunar Client style frosted glass surface with glowing border.
     */
    public static void drawGlassPanel(float x, float y, float width, float height, float radius, int bgColor, int borderColor, float borderWidth) {
        drawRoundedRect(x, y, width, height, radius, bgColor);
        if (borderWidth > 0 && ((borderColor >> 24) & 0xFF) > 0) {
            drawRoundedOutline(x, y, width, height, radius, borderWidth, borderColor);
        }
    }

    public static void drawArc(float cx, float cy, float r, int startAngle, int endAngle, int color) {
        color(color);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy);
        for (int i = startAngle; i <= endAngle; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(cx + Math.cos(rad) * r, cy + Math.sin(rad) * r);
        }
        GL11.glEnd();
    }

    public static void drawCircle(float cx, float cy, float radius, int color) {
        drawArc(cx, cy, radius, 0, 360, color);
    }

    public static void drawCircleOutline(float cx, float cy, float radius, float lineWidth, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);
        color(color);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < 360; i += 5) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(cx + Math.cos(rad) * radius, cy + Math.sin(rad) * radius);
        }
        GL11.glEnd();

        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float x, float y, float width, float height, int startColor, int endColor, boolean horizontal) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        float a1 = (float) (startColor >> 24 & 255) / 255.0F;
        float r1 = (float) (startColor >> 16 & 255) / 255.0F;
        float g1 = (float) (startColor >> 8 & 255) / 255.0F;
        float b1 = (float) (startColor & 255) / 255.0F;

        float a2 = (float) (endColor >> 24 & 255) / 255.0F;
        float r2 = (float) (endColor >> 16 & 255) / 255.0F;
        float g2 = (float) (endColor >> 8 & 255) / 255.0F;
        float b2 = (float) (endColor & 255) / 255.0F;

        if (horizontal) {
            wr.pos(x, y + height, 0.0D).color(r1, g1, b1, a1).endVertex();
            wr.pos(x + width, y + height, 0.0D).color(r2, g2, b2, a2).endVertex();
            wr.pos(x + width, y, 0.0D).color(r2, g2, b2, a2).endVertex();
            wr.pos(x, y, 0.0D).color(r1, g1, b1, a1).endVertex();
        } else {
            wr.pos(x, y + height, 0.0D).color(r2, g2, b2, a2).endVertex();
            wr.pos(x + width, y + height, 0.0D).color(r2, g2, b2, a2).endVertex();
            wr.pos(x + width, y, 0.0D).color(r1, g1, b1, a1).endVertex();
            wr.pos(x, y, 0.0D).color(r1, g1, b1, a1).endVertex();
        }

        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawTexturedRect(float x, float y, float width, float height, ResourceLocation texture) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).endVertex();
        wr.pos(x + width, y + height, 0.0D).tex(1.0D, 1.0D).endVertex();
        wr.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).endVertex();
        wr.pos(x, y, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
    }

    /**
     * Stencil-based Scissor clipping for modern scrolling containers.
     */
    public static void startScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int scale = sr.getScaleFactor();

        int scissorY = mc.displayHeight - (y + height) * scale;
        int scissorX = x * scale;
        int scissorWidth = width * scale;
        int scissorHeight = height * scale;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    public static void startScissor(float x, float y, float width, float height) {
        startScissor((int) x, (int) y, (int) width, (int) height);
    }

    public static void stopScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void pushScissor(float x, float y, float width, float height) {
        startScissor(x, y, width, height);
    }

    public static void popScissor() {
        stopScissor();
    }
}
