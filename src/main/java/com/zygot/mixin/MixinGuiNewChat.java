package com.zygot.mixin;

import com.zygot.Zygot;
import com.zygot.event.impl.ChatMessageEvent;
import com.zygot.mod.impl.qol.ChatTweaksMod;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    @Final
    private List<ChatLine> chatLines;

    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    public void onPrintChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId, CallbackInfo ci) {
        if (chatComponent == null) return;

        if (Zygot.INSTANCE != null && Zygot.INSTANCE.eventBus != null) {
            ChatMessageEvent event = new ChatMessageEvent(chatComponent.getUnformattedText());
            Zygot.INSTANCE.eventBus.post(event);
            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), argsOnly = true)
    public IChatComponent modifyChatTimestamps(IChatComponent chatComponent) {
        if (chatComponent == null || Zygot.INSTANCE == null || Zygot.INSTANCE.modManager == null) return chatComponent;
        ChatTweaksMod tweaks = Zygot.INSTANCE.modManager.getMod(ChatTweaksMod.class);
        if (tweaks != null && tweaks.isToggled() && tweaks.timestamps.isEnabled()) {
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            ChatComponentText timeComp = new ChatComponentText(EnumChatFormatting.GRAY + "[" + time + "] " + EnumChatFormatting.RESET);
            timeComp.appendSibling(chatComponent);
            return timeComp;
        }
        return chatComponent;
    }

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    public void onDrawChatBackground(int left, int top, int right, int bottom, int color) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ChatTweaksMod tweaks = Zygot.INSTANCE.modManager.getMod(ChatTweaksMod.class);
            if (tweaks != null && tweaks.isToggled() && tweaks.transparentChat.isEnabled()) {
                return;
            }
        }
        Gui.drawRect(left, top, right, bottom, color);
    }

    @Redirect(method = "setChatLine", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int redirectChatLineSize(List<?> instance) {
        if (Zygot.INSTANCE != null && Zygot.INSTANCE.modManager != null) {
            ChatTweaksMod tweaks = Zygot.INSTANCE.modManager.getMod(ChatTweaksMod.class);
            if (tweaks != null && tweaks.isToggled() && tweaks.unlimitedHistory.isEnabled()) {
                return 0;
            }
        }
        return instance.size();
    }
}
