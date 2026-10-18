package com.zygot.mod;

import com.zygot.Zygot;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.mod.setting.NumberSetting;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Base class for all draggable HUD widgets in Zygot Client.
 * Handles drag-and-drop, scaling, magnetic snapping, and Feather/Lunar glass styling.
 */
public abstract class HudMod extends Mod {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    private boolean dragging;
    private float dragOffsetX;
    private float dragOffsetY;

    // Built-in customizable HUD settings
    protected final BooleanSetting backgroundSetting;
    protected final BooleanSetting borderSetting;
    protected final BooleanSetting roundedSetting;
    protected final NumberSetting scaleSetting;
    protected final ColorSetting textColorSetting;
    protected final ColorSetting bgColorSetting;
    protected final ColorSetting borderColorSetting;

    public HudMod(String name, String description, float defaultX, float defaultY, float defaultW, float defaultH) {
        super(name, description, Category.HUD);
        this.x = defaultX;
        this.y = defaultY;
        this.width = defaultW;
        this.height = defaultH;

        this.backgroundSetting = new BooleanSetting("Background", true);
        this.borderSetting = new BooleanSetting("Border", true);
        this.roundedSetting = new BooleanSetting("Rounded", true);
        this.scaleSetting = new NumberSetting("Scale", 1.0, 0.5, 2.0, 0.05);
        this.textColorSetting = new ColorSetting("Text Color", Theme.rgba(255, 255, 255, 255));
        this.bgColorSetting = new ColorSetting("Background Color", Theme.rgba(17, 17, 22, 170));
        this.borderColorSetting = new ColorSetting("Border Color", Theme.rgba(139, 92, 246, 80));

        addSettings(
                backgroundSetting,
                borderSetting,
                roundedSetting,
                scaleSetting,
                textColorSetting,
                bgColorSetting,
                borderColorSetting
        );
    }

    /**
     * Draws the widget's background panel according to theme and module settings.
     */
    protected void drawHudBackground(float w, float h) {
        if (!backgroundSetting.isEnabled()) return;

        int bgCol = bgColorSetting.getColor();
        int borderCol = borderSetting.isEnabled() ? borderColorSetting.getColor() : 0;
        float radius = roundedSetting.isEnabled() ? 4f : 0f;

        Render2DHelper.drawGlassPanel(x, y, w, h, radius, bgCol, borderCol, borderSetting.isEnabled() ? 1f : 0f);
    }

    /**
     * Called every 2D overlay render tick in-game.
     */
    public abstract void render(float partialTicks);

    /**
     * Called in the HUD Editor for preview and positioning.
     */
    public abstract void renderDummy(int mouseX, int mouseY);

    public boolean isHovered(int mouseX, int mouseY) {
        float s = getScale();
        return mouseX >= x && mouseX <= x + width * s && mouseY >= y && mouseY <= y + height * s;
    }

    public void startDragging(int mouseX, int mouseY) {
        this.dragging = true;
        this.dragOffsetX = mouseX - x;
        this.dragOffsetY = mouseY - y;
    }

    public void updateDrag(int mouseX, int mouseY) {
        if (dragging) {
            this.x = mouseX - dragOffsetX;
            this.y = mouseY - dragOffsetY;
        }
    }

    public void stopDragging() {
        this.dragging = false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getScale() {
        return scaleSetting != null ? scaleSetting.getValueFloat() : 1.0f;
    }

    public boolean isDragging() {
        return dragging;
    }

    public int getTextColor() {
        return textColorSetting != null ? textColorSetting.getColor() : 0xFFFFFFFF;
    }
}
