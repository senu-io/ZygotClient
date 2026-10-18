package com.zygot.mod.impl.pvp;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.MouseEvent;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.BooleanSetting;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

public class CpsMod extends HudMod {

    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();

    private final BooleanSetting showRightClick;

    public CpsMod() {
        super("CPS", "Displays clicks per second on screen.", 10, 185, 60, 18);
        this.showRightClick = new BooleanSetting("Show Right Click", true);
        addSettings(showRightClick);
    }

    @EventTarget
    public void onMouse(MouseEvent e) {
        if (e.isState()) {
            long now = System.currentTimeMillis();
            if (e.getButton() == 0) {
                leftClicks.add(now);
            } else if (e.getButton() == 1) {
                rightClicks.add(now);
            }
        }
    }

    public static int getLeftCps() {
        long now = System.currentTimeMillis();
        leftClicks.removeIf(time -> now - time > 1000);
        return leftClicks.size();
    }

    public static int getRightCps() {
        long now = System.currentTimeMillis();
        rightClicks.removeIf(time -> now - time > 1000);
        return rightClicks.size();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderCps();
    }

    @Override
    public void render(float partialTicks) {
        renderCps();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderCps();
    }

    private void renderCps() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text;
        if (showRightClick.isEnabled()) {
            text = getLeftCps() + " | " + getRightCps() + " CPS";
        } else {
            text = getLeftCps() + " CPS";
        }

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(60, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
