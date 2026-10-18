package com.zygot.ui.menu;

import com.zygot.Zygot;
import com.zygot.theme.Theme;
import com.zygot.ui.cosmetics.ZygotCosmeticsScreen;
import com.zygot.ui.hud.ZygotHudEditor;
import com.zygot.ui.modmenu.ZygotModMenu;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.*;

import java.io.IOException;

public class ZygotIngameMenu extends GuiIngameMenu {

    @Override
    public void initGui() {
        super.initGui();

        int centerX = width / 2;
        int btnW = 100;
        int btnH = 20;

        // Add custom Zygot quick buttons
        this.buttonList.add(new GuiButton(100, centerX - 102, height / 4 + 120, btnW, btnH, "Zygot Mods"));
        this.buttonList.add(new GuiButton(101, centerX + 2, height / 4 + 120, btnW, btnH, "HUD Editor"));
        this.buttonList.add(new GuiButton(102, centerX - 102, height / 4 + 144, 204, btnH, "3D Cosmetics"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 100) {
            mc.displayGuiScreen(new ZygotModMenu(this));
            return;
        } else if (button.id == 101) {
            mc.displayGuiScreen(new ZygotHudEditor());
            return;
        } else if (button.id == 102) {
            mc.displayGuiScreen(new ZygotCosmeticsScreen(this));
            return;
        }
        super.actionPerformed(button);
    }
}
