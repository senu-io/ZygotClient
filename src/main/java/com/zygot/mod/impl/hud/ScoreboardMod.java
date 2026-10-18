package com.zygot.mod.impl.hud;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.Render2DEvent;
import com.zygot.mod.HudMod;
import com.zygot.mod.setting.BooleanSetting;
import com.zygot.mod.setting.ColorSetting;
import com.zygot.theme.Theme;
import com.zygot.util.render.Render2DHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardMod extends HudMod {

    public final BooleanSetting hideRedNumbers;
    public final BooleanSetting useCustomFont;

    public ScoreboardMod() {
        super("Scoreboard", "Custom modern scoreboard styling with movable position.", 0, 100, 130, 140);
        this.hideRedNumbers = new BooleanSetting("Hide Red Numbers", true);
        this.useCustomFont = new BooleanSetting("Comfortaa Font", true);
        addSettings(hideRedNumbers, useCustomFont);
        setToggled(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (!isToggled()) return;
        renderScoreboard(false);
    }

    @Override
    public void render(float partialTicks) {
        renderScoreboard(false);
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        renderScoreboard(true);
    }

    public void renderScoreboard(boolean dummy) {
        if (mc.theWorld == null && !dummy) return;

        Scoreboard scoreboard = mc.theWorld != null ? mc.theWorld.getScoreboard() : null;
        ScoreObjective objective = scoreboard != null ? scoreboard.getObjectiveInDisplaySlot(1) : null;

        if (objective == null && !dummy) return;

        GlStateManager.pushMatrix();
        float s = getScale();

        // Default to right side if not manually positioned
        if (x == 0) {
            ScaledResolution sr = new ScaledResolution(mc);
            x = sr.getScaledWidth() - 140;
        }

        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(s, s, 1.0F);

        List<String> lines = new ArrayList<>();
        List<String> scores = new ArrayList<>();
        String title = dummy || objective == null ? "ZYGOT CLIENT" : objective.getDisplayName();

        if (dummy || scoreboard == null || objective == null) {
            lines.add("09/18/26 m120B");
            lines.add("");
            lines.add("Player: \u00A7aZygotUser");
            lines.add("Rank: \u00A7b[VIP]");
            lines.add("Coins: \u00A7e14,250");
            lines.add("");
            lines.add("zygotclient.com");
            scores.add("6"); scores.add("5"); scores.add("4"); scores.add("3"); scores.add("2"); scores.add("1"); scores.add("0");
        } else {
            Collection<Score> scoreList = scoreboard.getSortedScores(objective);
            List<Score> filtered = new ArrayList<>();
            for (Score score : scoreList) {
                if (score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                    filtered.add(score);
                }
            }
            if (filtered.size() > 15) {
                filtered = filtered.subList(filtered.size() - 15, filtered.size());
            }

            for (Score sc : filtered) {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(sc.getPlayerName());
                String str = ScorePlayerTeam.formatPlayerName(team, sc.getPlayerName());
                lines.add(str);
                scores.add(Integer.toString(sc.getScorePoints()));
            }
        }

        float maxW = Zygot.INSTANCE.fontManager.bold16.getStringWidth(title) + 16;
        for (String line : lines) {
            float w = Zygot.INSTANCE.fontManager.regular14.getStringWidth(line) + 16;
            if (!hideRedNumbers.isEnabled()) w += 20;
            if (w > maxW) maxW = w;
        }

        this.width = maxW;
        this.height = (lines.size() + 1) * 12 + 8;

        drawHudBackground(width, height);

        // Title
        Zygot.INSTANCE.fontManager.bold16.drawCenteredString(title, width / 2f, 4, getTextColor());

        // Scoreboard lines
        float ly = 18;
        for (int i = lines.size() - 1; i >= 0; i--) {
            String l = lines.get(i);
            Zygot.INSTANCE.fontManager.regular14.drawString(l, 6, ly, Theme.rgba(235, 235, 245, 255));
            if (!hideRedNumbers.isEnabled() && i < scores.size()) {
                String scoreStr = scores.get(i);
                float sw = Zygot.INSTANCE.fontManager.regular14.getStringWidth(scoreStr);
                Zygot.INSTANCE.fontManager.regular14.drawString("\u00A7c" + scoreStr, width - sw - 6, ly, 0xFFFF5555);
            }
            ly += 12;
        }

        GlStateManager.popMatrix();
    }
}
