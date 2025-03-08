package org.losttribe;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {
    private org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard scoreBoard = manager.getNewScoreboard();


    private Objective vd = scoreBoard.registerNewObjective("ScoreBoard", "Mining Game");
    private Score timer = vd.getScore(ChatColor.GRAY + "Time Left:   " + ChatColor.RED + " 600 " + ChatColor.GRAY + "seconds");

    private Score blankLine = vd.getScore(" ");
    private Score blankLine2 = vd.getScore(" ");
    private Score blankLine3 = vd.getScore(" ");


    public ScoreboardManager() {
        vd.setDisplaySlot(DisplaySlot.SIDEBAR);
        vd.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Mining Game");
        blankLine.setScore(50);
        timer.setScore(49);
        blankLine2.setScore(48);
        blankLine3.setScore(47);
        // ADD SCORES OF EACH PLAYER TO THE SCORES ARRAY (IF TRACKING PER PLAYER)
    }

    public void setScoreboard(Player player){
        player.setScoreboard(scoreBoard);
    }

    public void updateCollected(int collected2) {
                scoreBoard.resetScores(timer.getEntry());
                timer = vd.getScore(ChatColor.GRAY + "Time Left:   " + ChatColor.RED + collected2 + ChatColor.GRAY + " seconds");
                timer.setScore(49);
    }

    public void clearScoreboard(Player player){
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

}
