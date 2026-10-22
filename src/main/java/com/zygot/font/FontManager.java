package com.zygot.font;

import com.zygot.ZygotClient;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Central Typography Manager for Zygot Client.
 * Automatically loads, rasterizes, and caches the Comfortaa typography family.
 * Pre-bakes essential sizes for instantaneous UI rendering.
 */
public class FontManager {

    private final Map<String, Font> rawFonts = new HashMap<>();
    private final Map<String, GlyphPageFontRenderer> fontRenderers = new HashMap<>();

    // Comfortaa Regular
    public GlyphPageFontRenderer regular14;
    public GlyphPageFontRenderer regular16;
    public GlyphPageFontRenderer regular18;
    public GlyphPageFontRenderer regular20;
    public GlyphPageFontRenderer regular24;
    public GlyphPageFontRenderer regular32;

    // Comfortaa Bold
    public GlyphPageFontRenderer bold14;
    public GlyphPageFontRenderer bold16;
    public GlyphPageFontRenderer bold18;
    public GlyphPageFontRenderer bold20;
    public GlyphPageFontRenderer bold24;
    public GlyphPageFontRenderer bold32;
    public GlyphPageFontRenderer bold48;

    // Comfortaa SemiBold
    public GlyphPageFontRenderer semiBold14;
    public GlyphPageFontRenderer semiBold16;
    public GlyphPageFontRenderer semiBold18;
    public GlyphPageFontRenderer semiBold20;

    // Comfortaa Light
    public GlyphPageFontRenderer light14;
    public GlyphPageFontRenderer light16;

    public void init() {
        ZygotClient.LOGGER.info("[Zygot Client] Initializing Comfortaa Font Engine...");

        // Load Comfortaa Fonts from resources
        loadRawFont("Comfortaa-Regular", "/assets/zygot/fonts/Comfortaa-Regular.ttf");
        loadRawFont("Comfortaa-Bold", "/assets/zygot/fonts/Comfortaa-Bold.ttf");
        loadRawFont("Comfortaa-SemiBold", "/assets/zygot/fonts/Comfortaa-SemiBold.ttf");
        loadRawFont("Comfortaa-Medium", "/assets/zygot/fonts/Comfortaa-Medium.ttf");
        loadRawFont("Comfortaa-Light", "/assets/zygot/fonts/Comfortaa-Light.ttf");

        // Pre-bake regular typography
        regular14 = getRenderer("Comfortaa-Regular", 14);
        regular16 = getRenderer("Comfortaa-Regular", 16);
        regular18 = getRenderer("Comfortaa-Regular", 18);
        regular20 = getRenderer("Comfortaa-Regular", 20);
        regular24 = getRenderer("Comfortaa-Regular", 24);
        regular32 = getRenderer("Comfortaa-Regular", 32);

        // Pre-bake bold typography
        bold14 = getRenderer("Comfortaa-Bold", 14);
        bold16 = getRenderer("Comfortaa-Bold", 16);
        bold18 = getRenderer("Comfortaa-Bold", 18);
        bold20 = getRenderer("Comfortaa-Bold", 20);
        bold24 = getRenderer("Comfortaa-Bold", 24);
        bold32 = getRenderer("Comfortaa-Bold", 32);
        bold48 = getRenderer("Comfortaa-Bold", 48);

        // Pre-bake semi-bold typography
        semiBold14 = getRenderer("Comfortaa-SemiBold", 14);
        semiBold16 = getRenderer("Comfortaa-SemiBold", 16);
        semiBold18 = getRenderer("Comfortaa-SemiBold", 18);
        semiBold20 = getRenderer("Comfortaa-SemiBold", 20);

        // Pre-bake light typography
        light14 = getRenderer("Comfortaa-Light", 14);
        light16 = getRenderer("Comfortaa-Light", 16);

        ZygotClient.LOGGER.info("[Zygot Client] Comfortaa Font Engine successfully initialized.");
    }

    private void loadRawFont(String name, String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                rawFonts.put(name.toLowerCase(), font);
            } else {
                ZygotClient.LOGGER.warn("[Zygot Client] Font not found at " + path + ", using SansSerif fallback.");
                rawFonts.put(name.toLowerCase(), new Font("SansSerif", Font.PLAIN, 18));
            }
        } catch (Exception e) {
            ZygotClient.LOGGER.warn("[Zygot Client] Failed to load font " + path + ": " + e.getMessage() + ", using SansSerif fallback.");
            rawFonts.put(name.toLowerCase(), new Font("SansSerif", Font.PLAIN, 18));
        }
    }

    public GlyphPageFontRenderer getRenderer(String fontName, int size) {
        String key = fontName.toLowerCase() + "_" + size;
        if (fontRenderers.containsKey(key)) {
            return fontRenderers.get(key);
        }

        Font baseFont = rawFonts.get(fontName.toLowerCase());
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, size);
        }

        Font derived = baseFont.deriveFont((float) size);
        GlyphPageFontRenderer renderer = GlyphPageFontRenderer.create(derived, true, true);
        fontRenderers.put(key, renderer);
        return renderer;
    }
}
