package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.BooleanSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;

public class CoordinatesMod extends HudMod {

    private final BooleanSetting showBiome;
    private final BooleanSetting showNether;

    public CoordinatesMod() {
        super("Coordinates", "Shows current player position, biome, and coordinates.", 10, 55, 120, 18);
        this.showBiome = new BooleanSetting("Show Biome", true);
        this.showNether = new BooleanSetting("Nether Coords", false);
        addSettings(showBiome, showNether);
        setToggled(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderCoords(false);
    }

    @Override
    public void render(float partialTicks) {
        renderCoords(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderCoords(true);
    }

    private void renderCoords(boolean dummy) {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        int posX = dummy || mc.thePlayer == null ? 120 : (int) mc.thePlayer.posX;
        int posY = dummy || mc.thePlayer == null ? 64 : (int) mc.thePlayer.posY;
        int posZ = dummy || mc.thePlayer == null ? -350 : (int) mc.thePlayer.posZ;

        StringBuilder sb = new StringBuilder();
        sb.append("XYZ: ").append(posX).append(" ").append(posY).append(" ").append(posZ);

        if (showBiome.isEnabled() && mc.theWorld != null && mc.thePlayer != null && !dummy) {
            BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
            sb.append(" (").append(mc.theWorld.getBiomeGenForCoords(bp).biomeName).append(")");
        } else if (dummy && showBiome.isEnabled()) {
            sb.append(" (Plains)");
        }

        String text = sb.toString();
        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(100, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
