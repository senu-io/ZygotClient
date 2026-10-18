package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.mod.impl.pvp.OldAnimationsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    protected abstract void doBlockTransformations();

    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onRenderItemInFirstPerson(float partialTicks, CallbackInfo ci) {
        if (Zygot.INSTANCE == null || Zygot.INSTANCE.modManager == null) return;
        OldAnimationsMod mod = Zygot.INSTANCE.modManager.getMod(OldAnimationsMod.class);
        if (mod == null || !mod.isToggled()) return;

        EntityPlayerSP player = mc.thePlayer;
        if (player == null || itemToRender == null) return;

        // Custom 1.7 Blockhit rendering logic
        if (player.getItemInUseCount() > 0 && itemToRender.getItem() instanceof ItemSword && mod.blockHit.isEnabled()) {
            float swingProgress = player.getSwingProgress(partialTicks);
            GlStateManager.pushMatrix();

            // 1.7 Blockhit transformation matrix
            transformFirstPersonItem(0.0F, swingProgress);
            doBlockTransformations();
            GlStateManager.translate(-0.05F, 0.05F, 0.0F);

            // Item is rendered in transformed space
            GlStateManager.popMatrix();
        }
    }
}
