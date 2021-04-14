package com.happyandjust.murdererfinder.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;

public class ScoreboardHandler {
    public static String cleanSB(String scoreboard) {

        try {
            char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
            StringBuilder cleaned = new StringBuilder();

            for (char c : nvString) {
                if ((int) c > 20 && (int) c < 127) {
                    cleaned.append(c);
                }
            }

            return cleaned.toString();
        } catch (Exception e) {

        }
        return "";
    }


    public static String getSidebarDisplayName() {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        for (ScoreObjective sc : scoreboard.getScoreObjectives()) {
            return sc.getDisplayName();
        }
        return "";
    }
}
