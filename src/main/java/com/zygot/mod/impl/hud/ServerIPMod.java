package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import net.minecraft.client.renderer.GlStateManager;

public class ServerIPMod extends HudMod {

    public ServerIPMod() {
        super("Server IP", "Displays current connected server address.", 10, 460, 90, 18);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderServer();
    }

    @Override
    public void render(float partialTicks) {
        renderServer();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderServer();
    }

    private void renderServer() {
        GlStateManager.pushMatrix();
        float s = getScale();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        String text = mc.isSingleplayer() ? "Singleplayer" : (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "hypixel.net");

        float textWidth = Zygot.INSTANCE.fontManager.regular16.getStringWidth(text);
        this.width = Math.max(80, textWidth + 14);
        this.height = 18;

        drawHudBackground(width, height);
        Zygot.INSTANCE.fontManager.regular16.drawCenteredString(text, width / 2f, 4, getTextColor());

        GlStateManager.popMatrix();
    }
}
