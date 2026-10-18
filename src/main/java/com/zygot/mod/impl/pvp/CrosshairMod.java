package com.zygot.mod.impl.pvp;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.mod.setting.ModeSetting;
import com.zygot.mod.setting.NumberSetting;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.ScaledResolution;

public class CrosshairMod extends Mod {

    public final ModeSetting style;
    public final ColorSetting crosshairColor;
    public final NumberSetting size;
    public final NumberSetting thickness;
    public final NumberSetting gap;
    public final BooleanSetting dot;

    public CrosshairMod() {
        super("Crosshair", "Customizes size, shape, and color of the crosshair.", Category.PVP);

        this.style = new ModeSetting("Style", "Cross", "Cross", "Dot", "Circle");
        this.crosshairColor = new ColorSetting("Color", Theme.rgba(255, 255, 255, 255));
        this.size = new NumberSetting("Size", 4.0, 1.0, 15.0, 0.5);
        this.thickness = new NumberSetting("Thickness", 1.5, 0.5, 5.0, 0.5);
        this.gap = new NumberSetting("Gap", 2.0, 0.0, 10.0, 0.5);
        this.dot = new BooleanSetting("Center Dot", false);

        addSettings(style, crosshairColor, size, thickness, gap, dot);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled() || mc.gameSettings.showDebugInfo) return;

        ScaledResolution sr = new ScaledResolution(mc);
        float cx = sr.getScaledWidth() / 2f;
        float cy = sr.getScaledHeight() / 2f;

        int col = crosshairColor.getColor();
        float sz = size.getValueFloat();
        float th = thickness.getValueFloat();
        float gp = gap.getValueFloat();

        if (dot.isEnabled() || style.is("Dot")) {
            Render2DHelper.drawCircle(cx, cy, th, col);
            if (style.is("Dot")) return;
        }

        if (style.is("Cross")) {
            // Top
            Render2DHelper.drawRect(cx - th / 2f, cy - gp - sz, th, sz, col);
            // Bottom
            Render2DHelper.drawRect(cx - th / 2f, cy + gp, th, sz, col);
            // Left
            Render2DHelper.drawRect(cx - gp - sz, cy - th / 2f, sz, th, col);
            // Right
            Render2DHelper.drawRect(cx + gp, cy - th / 2f, sz, th, col);
        } else if (style.is("Circle")) {
            Render2DHelper.drawCircleOutline(cx, cy, sz + gp, th, col);
        }
    }
}
