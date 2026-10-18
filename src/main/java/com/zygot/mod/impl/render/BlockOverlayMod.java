package com.zygot.mod.impl.render;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.mod.setting.NumberSetting;
import com.zygot.theme.Theme;

public class BlockOverlayMod extends Mod {

    public final ColorSetting outlineColor;
    public final ColorSetting fillColor;
    public final BooleanSetting showFill;
    public final NumberSetting lineWidth;

    public BlockOverlayMod() {
        super("Block Overlay", "Customizes the outline and fill color of targeted blocks.", Category.RENDER);

        this.outlineColor = new ColorSetting("Outline Color", Theme.rgba(139, 92, 246, 255));
        this.fillColor = new ColorSetting("Fill Color", Theme.rgba(139, 92, 246, 40));
        this.showFill = new BooleanSetting("Render Fill", true);
        this.lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0, 0.5);

        addSettings(outlineColor, fillColor, showFill, lineWidth);
    }
}
