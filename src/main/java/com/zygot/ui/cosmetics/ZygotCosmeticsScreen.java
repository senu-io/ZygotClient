package com.zygot.ui.cosmetics;

import com.zygot.Zygot;
import com.zygot.cosmetics.Cosmetic;
import com.zygot.cosmetics.CosmeticType;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class ZygotCosmeticsScreen extends GuiScreen {

    private final GuiScreen parent;
    private CosmeticType activeType = CosmeticType.CAPE;

    private float playerYaw = 0.0F;
    private float playerPitch = 0.0F;
    private boolean isDraggingPlayer = false;
    private int prevMouseX;
    private int prevMouseY;
    private DummyPlayer dummyPlayer;

    public ZygotCosmeticsScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        if (mc.thePlayer == null && dummyPlayer == null) {
            dummyPlayer = new DummyPlayer(mc.getSession() != null ? mc.getSession().getProfile() : null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(mc);
        float pw = sr.getScaledWidth() * 0.85F;
        float ph = sr.getScaledHeight() * 0.85F;
        float px = (sr.getScaledWidth() - pw) / 2F;
        float py = (sr.getScaledHeight() - ph) / 2F;

        Theme theme = Zygot.INSTANCE.themeManager.getCurrentTheme();

        // Main modal frosted glass window
        Render2DHelper.drawGlassPanel(px, py, pw, ph, 10, theme.background, theme.surfaceBorder, 1.5F);

        // Header Title
        Zygot.INSTANCE.fontManager.bold24.drawString("3D Cosmetics Wardrobe", px + 24, py + 18, theme.textPrimary);
        Zygot.INSTANCE.fontManager.regular14.drawString("Equip custom capes, animated wings, halos, and hats", px + 24, py + 38, theme.textSecondary);

        // Category Tabs
        float tabX = px + 24;
        float tabY = py + 56;
        for (CosmeticType type : CosmeticType.values()) {
            boolean active = (type == activeType);
            float tw = Zygot.INSTANCE.fontManager.bold16.getStringWidth(type.getDisplayName()) + 24;
            int tabBg = active ? theme.accent : theme.surface;
            int tabBorder = active ? theme.accentHover : theme.cardBorder;
            Render2DHelper.drawGlassPanel(tabX, tabY, tw, 22, 6, tabBg, tabBorder, 1f);

            int textCol = active ? 0xFFFFFFFF : theme.textSecondary;
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(type.getDisplayName(), tabX + tw / 2f, tabY + 4, textCol);

            tabX += tw + 8;
        }

        // Left Panel: 3D Player Preview Window
        float previewX = px + 24;
        float previewY = py + 88;
        float previewW = pw * 0.35F;
        float previewH = ph - 110;
        Render2DHelper.drawGlassPanel(previewX, previewY, previewW, previewH, 8, theme.surface, theme.cardBorder, 1f);
        Zygot.INSTANCE.fontManager.light14.drawCenteredString("Click & drag to rotate preview", previewX + previewW / 2f, previewY + previewH - 16, theme.textMuted);

        // Render 3D Entity
        EntityLivingBase entityToRender = mc.thePlayer != null ? mc.thePlayer : dummyPlayer;
        if (entityToRender != null) {
            drawEntityOnScreen((int) (previewX + previewW / 2f), (int) (previewY + previewH - 26), 65, playerYaw, playerPitch, entityToRender);
        }

        // Right Panel: Cosmetic Items Grid
        float gridX = previewX + previewW + 16;
        float gridY = py + 88;
        float gridW = pw - previewW - 56;
        float gridH = previewH;
        Render2DHelper.drawGlassPanel(gridX, gridY, gridW, gridH, 8, theme.surface, theme.cardBorder, 1f);

        List<Cosmetic> list = Zygot.INSTANCE.cosmeticManager.getCosmeticsByType(activeType);
        float itemW = (gridW - 32) / 2f;
        float itemH = 46;
        int col = 0;
        float iy = gridY + 14;

        for (Cosmetic c : list) {
            float ix = gridX + 14 + col * (itemW + 8);
            boolean equipped = Zygot.INSTANCE.cosmeticManager.isEquipped(c.getId());
            boolean hovered = mouseX >= ix && mouseX <= ix + itemW && mouseY >= iy && mouseY <= iy + itemH;

            int cardBg = equipped ? Theme.withAlpha(theme.accent, 40) : (hovered ? theme.surfaceHover : theme.cardBg);
            int cardBorder = equipped ? theme.accent : (hovered ? theme.accentHover : theme.cardBorder);
            Render2DHelper.drawGlassPanel(ix, iy, itemW, itemH, 6, cardBg, cardBorder, 1f);

            // Icon / Indicator Pip
            int pipCol = equipped ? theme.accent : theme.textMuted;
            Render2DHelper.drawRoundedRect(ix + 8, iy + 8, 4, itemH - 16, 2, pipCol);

            // Item Name
            Zygot.INSTANCE.fontManager.bold16.drawString(c.getName(), ix + 18, iy + 10, theme.textPrimary);

            // Status Badge
            String statusText = equipped ? "EQUIPPED" : "EQUIP";
            int badgeBg = equipped ? theme.accent : theme.surface;
            int badgeBorder = equipped ? theme.accentHover : theme.cardBorder;
            float bw = 65;
            float bx = ix + itemW - bw - 10;
            float by = iy + 11;
            Render2DHelper.drawGlassPanel(bx, by, bw, 22, 4, badgeBg, badgeBorder, 1f);
            Zygot.INSTANCE.fontManager.bold16.drawCenteredString(statusText, bx + bw / 2f, by + 4, equipped ? 0xFFFFFFFF : theme.textSecondary);

            col++;
            if (col >= 2) {
                col = 0;
                iy += itemH + 10;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        float pw = sr.getScaledWidth() * 0.85F;
        float ph = sr.getScaledHeight() * 0.85F;
        float px = (sr.getScaledWidth() - pw) / 2F;
        float py = (sr.getScaledHeight() - ph) / 2F;

        // Check Category Tabs
        float tabX = px + 24;
        float tabY = py + 56;
        for (CosmeticType type : CosmeticType.values()) {
            float tw = Zygot.INSTANCE.fontManager.bold16.getStringWidth(type.getDisplayName()) + 24;
            if (mouseX >= tabX && mouseX <= tabX + tw && mouseY >= tabY && mouseY <= tabY + 22) {
                this.activeType = type;
                return;
            }
            tabX += tw + 8;
        }

        // Check 3D Player Drag Start
        float previewX = px + 24;
        float previewY = py + 88;
        float previewW = pw * 0.35F;
        float previewH = ph - 110;
        if (mouseX >= previewX && mouseX <= previewX + previewW && mouseY >= previewY && mouseY <= previewY + previewH) {
            isDraggingPlayer = true;
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }

        // Check Cosmetic Items Grid Click
        float gridX = previewX + previewW + 16;
        float gridY = py + 88;
        float gridW = pw - previewW - 56;
        List<Cosmetic> list = Zygot.INSTANCE.cosmeticManager.getCosmeticsByType(activeType);
        float itemW = (gridW - 32) / 2f;
        float itemH = 46;
        int col = 0;
        float iy = gridY + 14;

        for (Cosmetic c : list) {
            float ix = gridX + 14 + col * (itemW + 8);
            if (mouseX >= ix && mouseX <= ix + itemW && mouseY >= iy && mouseY <= iy + itemH) {
                Zygot.INSTANCE.cosmeticManager.toggleEquip(c.getId());
                return;
            }
            col++;
            if (col >= 2) {
                col = 0;
                iy += itemH + 10;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (isDraggingPlayer) {
            playerYaw += (mouseX - prevMouseX) * 1.5F;
            playerPitch = Math.max(-45F, Math.min(45F, playerPitch - (mouseY - prevMouseY) * 1.0F));
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        isDraggingPlayer = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Renders an interactive 3D entity on screen.
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float yaw, float pitch, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 100.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        float f2 = ent.renderYawOffset;
        float f3 = ent.rotationYaw;
        float f4 = ent.rotationPitch;
        float f5 = ent.prevRotationYawHead;
        float f6 = ent.rotationYawHead;

        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);

        ent.renderYawOffset = yaw;
        ent.rotationYaw = yaw;
        ent.rotationPitch = pitch;
        ent.rotationYawHead = yaw;
        ent.prevRotationYawHead = yaw;

        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = net.minecraft.client.Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f2;
        ent.rotationYaw = f3;
        ent.rotationPitch = f4;
        ent.prevRotationYawHead = f5;
        ent.rotationYawHead = f6;

        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
