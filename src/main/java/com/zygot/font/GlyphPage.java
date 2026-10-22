package com.zygot.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Generates an OpenGL texture atlas of glyphs for a specific java.awt.Font.
 * Positions glyphs strictly on a unified typographic baseline and tracks exact character advances.
 */
public class GlyphPage {

    private final Font font;
    private final boolean antiAliasing;
    private final boolean fractionalMetrics;
    private final HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();

    private int imgSize = 512;
    private int fontAscent = 0;
    private int fontDescent = 0;
    private int fontHeight = 16;
    private BufferedImage bufferedImage;
    private int loadedTexture = -1;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars) {
        FontRenderContext renderContext = new FontRenderContext(
                null,
                antiAliasing,
                fractionalMetrics
        );

        BufferedImage probeImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D probeG = probeImg.createGraphics();
        probeG.setFont(font);
        FontMetrics fontMetrics = probeG.getFontMetrics();
        this.fontAscent = fontMetrics.getAscent();
        this.fontDescent = fontMetrics.getDescent();
        this.fontHeight = fontMetrics.getHeight();
        probeG.dispose();

        // Calculate appropriate power-of-two texture atlas dimension
        int estimatedDim = (int) Math.ceil(fontHeight * Math.sqrt(chars.length) * 1.3);
        imgSize = 256;
        while (imgSize < estimatedDim || imgSize < 512) {
            imgSize *= 2;
        }

        bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

        int posX = 2;
        int posY = 2;
        int maxRowHeight = 0;

        for (char ch : chars) {
            Glyph glyph = new Glyph();
            GlyphVector gv = font.createGlyphVector(renderContext, Character.toString(ch));
            Rectangle pixelBounds = gv.getPixelBounds(renderContext, 0, 0);
            float advance = (float) gv.getGlyphMetrics(0).getAdvanceX();

            glyph.advance = advance;

            if (pixelBounds.width > 0 && pixelBounds.height > 0) {
                glyph.width = pixelBounds.width + 1;
                glyph.height = pixelBounds.height + 1;
                glyph.xOffset = pixelBounds.x;
                glyph.yOffset = pixelBounds.y;

                if (posX + glyph.width >= imgSize - 2) {
                    posX = 2;
                    posY += maxRowHeight + 2;
                    maxRowHeight = 0;
                }

                if (glyph.height > maxRowHeight) {
                    maxRowHeight = glyph.height;
                }

                glyph.x = posX;
                glyph.y = posY;

                g.drawString(Character.toString(ch), posX - pixelBounds.x, posY - pixelBounds.y);

                posX += glyph.width + 2;
            } else {
                glyph.width = 0;
                glyph.height = 0;
                glyph.xOffset = 0;
                glyph.yOffset = 0;
            }

            glyphCharacterMap.put(ch, glyph);
        }

        g.dispose();
    }

    public void setupTexture() {
        if (loadedTexture != -1) return;
        loadedTexture = GL11.glGenTextures();
        GlStateManager.bindTexture(loadedTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        int[] pixels = new int[imgSize * imgSize];
        bufferedImage.getRGB(0, 0, imgSize, imgSize, pixels, 0, imgSize);

        ByteBuffer buffer = BufferUtils.createByteBuffer(imgSize * imgSize * 4);
        for (int y = 0; y < imgSize; y++) {
            for (int x = 0; x < imgSize; x++) {
                int pixel = pixels[y * imgSize + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, imgSize, imgSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GlStateManager.bindTexture(0);
    }

    public void bindTexture() {
        if (loadedTexture == -1) setupTexture();
        GlStateManager.bindTexture(loadedTexture);
    }

    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

    public float drawChar(char ch, float x, float y) {
        Glyph glyph = glyphCharacterMap.get(ch);
        if (glyph == null) return 0;

        if (glyph.width > 0 && glyph.height > 0) {
            float u1 = (float) glyph.x / imgSize;
            float v1 = (float) glyph.y / imgSize;
            float u2 = (float) (glyph.x + glyph.width) / imgSize;
            float v2 = (float) (glyph.y + glyph.height) / imgSize;

            float drawX = x + glyph.xOffset;
            float drawY = y + fontAscent + glyph.yOffset;

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f(drawX, drawY);
            GL11.glTexCoord2f(u1, v2);
            GL11.glVertex2f(drawX, drawY + glyph.height);
            GL11.glTexCoord2f(u2, v2);
            GL11.glVertex2f(drawX + glyph.width, drawY + glyph.height);
            GL11.glTexCoord2f(u2, v1);
            GL11.glVertex2f(drawX + glyph.width, drawY);
            GL11.glEnd();
        }

        return glyph.advance;
    }

    public float getWidth(char ch) {
        Glyph glyph = glyphCharacterMap.get(ch);
        return glyph != null ? glyph.advance : 0;
    }

    public int getMaxFontHeight() {
        return fontHeight;
    }

    static class Glyph {
        int x;
        int y;
        int width;
        int height;
        float xOffset;
        float yOffset;
        float advance;
    }
}
