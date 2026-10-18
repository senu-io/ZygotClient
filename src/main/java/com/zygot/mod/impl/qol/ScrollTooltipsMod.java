package com.zygot.mod.impl.qol;

import com.zygot.mod.Category;
import com.zygot.mod.Mod;

public class ScrollTooltipsMod extends Mod {

    public ScrollTooltipsMod() {
        super("Scrollable Tooltips", "Allows scrolling long item lore and tooltips that exceed screen height.", Category.QOL);
        setToggled(true);
    }
}
