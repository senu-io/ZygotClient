package com.zygot.mod.impl.qol;

import com.zygot.event.EventTarget;
import com.zygot.event.impl.ChatMessageEvent;
import com.zygot.mod.Category;
import com.zygot.mod.Mod;
import com.zygot.mod.setting.NumberSetting;

public class AutoGGMod extends Mod {

    private final NumberSetting delay;
    private long triggerTime = 0;

    public AutoGGMod() {
        super("Auto GG", "Automatically says GG in chat when a game ends on Hypixel.", Category.QOL);
        this.delay = new NumberSetting("Delay (ms)", 500, 0, 3000, 100);
        addSettings(delay);
        setToggled(true);
    }

    @EventTarget
    public void onChat(ChatMessageEvent e) {
        if (!isToggled() || mc.thePlayer == null) return;

        String msg = e.getMessage().toLowerCase();
        if (msg.contains("1st killer -") || msg.contains("winner -") || msg.contains("victor -") ||
                msg.contains("1st place -") || msg.contains("game over") || msg.contains("you died")) {
            new Thread(() -> {
                try {
                    Thread.sleep((long) delay.getValue());
                    if (mc.thePlayer != null) {
                        mc.thePlayer.sendChatMessage("/achat gg");
                    }
                } catch (InterruptedException ignored) {}
            }).start();
        }
    }
}
