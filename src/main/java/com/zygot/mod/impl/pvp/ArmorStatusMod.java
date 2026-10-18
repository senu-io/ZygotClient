package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ModeSetting;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ArmorStatusMod extends HudMod {

    private final ModeSetting orientation;
    private final BooleanSetting showDamage;
    private final BooleanSetting showDurabilityBar;

    public ArmorStatusMod() {
        super("Armor Status", "Displays equipped armor items and durability bars.", 10, 260, 24, 80);

        this.orientation = new ModeSetting("Orientation", "Vertical", "Vertical", "Horizontal");
        this.showDamage = new BooleanSetting("Show Percentage", true);
        this.showDurabilityBar = new BooleanSetting("Durability Bar", true);

        addSettings(orientation, showDamage, showDurabilityBar);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderArmor(false);
    }

    @Override
    public void render(float partialTicks) {
        renderArmor(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderArmor(true);
    }

    private void renderArmor(boolean dummy) {
        if (mc.thePlayer == null && !dummy) return;

        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        boolean vert = orientation.is("Vertical");
        this.width = vert ? 24 : 80;
        this.height = vert ? 80 : 24;

        drawHudBackground(width, height);

        ItemStack[] armor = new ItemStack[4];
        if (dummy) {
            armor[3] = new ItemStack(Items.diamond_helmet);
            armor[2] = new ItemStack(Items.diamond_chestplate);
            armor[1] = new ItemStack(Items.diamond_leggings);
            armor[0] = new ItemStack(Items.diamond_boots);
        } else {
            for (int i = 0; i < 4; i++) {
                armor[i] = mc.thePlayer.inventory.armorInventory[i];
            }
        }

        RenderHelper.enableGUIStandardItemLighting();

        for (int i = 3; i >= 0; i--) {
            int slotIndex = 3 - i;
            float px = vert ? 4 : slotIndex * 20 + 2;
            float py = vert ? slotIndex * 20 + 2 : 4;

            ItemStack is = armor[i];
            if (is != null) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(is, (int) px, (int) py);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, is, (int) px, (int) py);

                if (showDurabilityBar.isEnabled() && is.isItemStackDamageable()) {
                    float max = is.getMaxDamage();
                    float current = max - is.getItemDamage();
                    float pct = current / max;
                    int barCol = pct > 0.5f ? Theme.rgba(34, 197, 94, 255) : (pct > 0.25f ? Theme.rgba(234, 179, 8, 255) : Theme.rgba(239, 68, 68, 255));
                    Render2DHelper.drawRect(px, py + 14, 16 * pct, 2, barCol);
                }
            }
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
