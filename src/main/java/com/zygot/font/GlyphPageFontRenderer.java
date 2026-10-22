package com.zygot.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Locale;

/**
 * High performance TrueType Font Renderer using pre-baked OpenGL GlyphPages.
 * Supports standard Minecraft formatting codes (§0-§f, §r, §l, §o),
 * drop shadows, centered text, and custom RGB/RGBA colors.
 */
public class GlyphPageFontRenderer {

    private final GlyphPage regularGlyphPage;
    private final GlyphPage boldGlyphPage;
    private final GlyphPage italicGlyphPage;
    private final GlyphPage boldItalicGlyphPage;
    private final int[] colorCode = new int[32];

    public GlyphPageFontRenderer(GlyphPage regular, GlyphPage bold, GlyphPage italic, GlyphPage boldItalic) {
        this.regularGlyphPage = regular;
        this.boldGlyphPage = bold;
        this.italicGlyphPage = italic;
        this.boldItalicGlyphPage = boldItalic;

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i & 1) * 170 + j;

            if (i == 6) {
                r += 85;
            }

            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }

            this.colorCode[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }

    public static GlyphPageFontRenderer create(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        char[] chars = new char[256];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphPage regular = new GlyphPage(font, antiAliasing, fractionalMetrics);
        regular.generateGlyphPage(chars);
        regular.setupTexture();

        GlyphPage bold = new GlyphPage(font.deriveFont(Font.BOLD), antiAliasing, fractionalMetrics);
        bold.generateGlyphPage(chars);
        bold.setupTexture();

        GlyphPage italic = new GlyphPage(font.deriveFont(Font.ITALIC), antiAliasing, fractionalMetrics);
        italic.generateGlyphPage(chars);
        italic.setupTexture();

        GlyphPage boldItalic = new GlyphPage(font.deriveFont(Font.BOLD | Font.ITALIC), antiAliasing, fractionalMetrics);
        boldItalic.generateGlyphPage(chars);
        boldItalic.setupTexture();

        return new GlyphPageFontRenderer(regular, bold, italic, boldItalic);
    }

    public int drawString(String text, float x, float y, int color) {
        return renderString(text, x, y, color, false);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        GlStateManager.pushMatrix();
        renderString(text, x + 0.6f, y + 0.6f, (color & 0xFCFCFC) >> 2 | (color & 0xFF000000), true);
        int result = renderString(text, x, y, color, false);
        GlStateManager.popMatrix();
        return result;
    }

    public int drawCenteredString(String text, float x, float y, int color) {
        return drawString(text, x - getStringWidth(text) / 2f, y, color);
    }

    public int drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return drawStringWithShadow(text, x - getStringWidth(text) / 2f, y, color);
    }

    private int renderString(String text, float x, float y, int color, boolean isShadow) {
        if (text == null || text.isEmpty()) return 0;

        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        float currentX = x;
        float currentY = y;

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        if (alpha < 0.05F) alpha = 1.0F;

        GlStateManager.color(red, green, blue, alpha);

        boolean bold = false;
        boolean italic = false;

        GlyphPage currentPage = regularGlyphPage;
        currentPage.bindTexture();

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == '\u00A7' && i + 1 < text.length()) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    if (colorIndex < 0) colorIndex = 15;
                    if (isShadow) colorIndex += 16;
                    int col = this.colorCode[colorIndex];
                    GlStateManager.color((float) (col >> 16 & 255) / 255.0F,
                            (float) (col >> 8 & 255) / 255.0F,
                            (float) (col & 255) / 255.0F, alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                } else if (colorIndex == 20) {
                    italic = true;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    GlStateManager.color(red, green, blue, alpha);
                }

                i++;
            } else {
                GlyphPage targetPage = bold && italic ? boldItalicGlyphPage :
                        bold ? boldGlyphPage :
                                italic ? italicGlyphPage : regularGlyphPage;

                if (targetPage != currentPage) {
                    currentPage = targetPage;
                    currentPage.bindTexture();
                }

                currentX += currentPage.drawChar(c0, currentX, currentY);
            }
        }

        currentPage.unbindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        return (int) currentX;
    }

    public int getStringWidth(String text) {
        if (text == null || text.isEmpty()) return 0;
        float width = 0;
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            if (c0 == '\u00A7' && i + 1 < text.length()) {
                i++;
            } else {
                width += regularGlyphPage.getWidth(c0);
            }
        }
        return (int) Math.ceil(width);
    }

    public int getFontHeight() {
        return regularGlyphPage.getMaxFontHeight();
    }
}
