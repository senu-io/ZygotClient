package com.zygot.mod.impl.pvp;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.TickEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.BooleanSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class OldAnimationsMod extends Mod {

    public final BooleanSetting blockHit;
    public final BooleanSetting eatingAnimation;
    public final BooleanSetting bowAnimation;
    public final BooleanSetting rodAnimation;
    public final BooleanSetting punchBlock;

    public OldAnimationsMod() {
        super("1.7 Animations", "Brings back authentic Minecraft 1.7 animations and block-hitting.", Category.PVP);

        this.blockHit = new BooleanSetting("Block Hit", true);
        this.eatingAnimation = new BooleanSetting("Eat/Drink Animation", true);
        this.bowAnimation = new BooleanSetting("Bow Animation", true);
        this.rodAnimation = new BooleanSetting("Fishing Rod", true);
        this.punchBlock = new BooleanSetting("Punch on Block", true);

        addSettings(blockHit, eatingAnimation, bowAnimation, rodAnimation, punchBlock);
        setToggled(true);
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!isToggled() || mc.thePlayer == null || mc.theWorld == null) return;
        if (e.getPhase() == TickEvent.Phase.START) return;

        ItemStack held = mc.thePlayer.getHeldItem();
        if (held == null) return;

        if (blockHit.isEnabled() && held.getItemUseAction() == EnumAction.BLOCK) {
            attemptSwing();
        } else if (eatingAnimation.isEnabled() && (held.getItemUseAction() == EnumAction.EAT || held.getItemUseAction() == EnumAction.DRINK)) {
            attemptSwing();
        } else if (bowAnimation.isEnabled() && held.getItemUseAction() == EnumAction.BOW) {
            attemptSwing();
        }
    }

    private void attemptSwing() {
        if (mc.thePlayer.getItemInUseCount() > 0) {
            boolean attackDown = mc.gameSettings.keyBindAttack.isKeyDown();
            boolean useDown = mc.gameSettings.keyBindUseItem.isKeyDown();

            if (attackDown && useDown && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                forceSwingArm();
            }
        }
    }

    public void forceSwingArm() {
        EntityPlayerSP player = mc.thePlayer;
        int swingEnd = player.isPotionActive(Potion.digSpeed) ?
                (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (player.isPotionActive(Potion.digSlowdown) ?
                (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);

        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
            player.swingProgressInt = -1;
            player.isSwingInProgress = true;
        }
    }
}
