package org.losttribe.miningGame;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {
    private MiningGame main;
    private int countdownSeconds;
    private ArenaObject arena;

    public Countdown(MiningGame main, ArenaObject arena) {
        this.main = main;
        this.arena = arena;
        this.countdownSeconds = 600;
    }

    public void Timer() {
        BukkitTask task = new BukkitRunnable() {
            public void run() {
                if ((countdownSeconds) == 0) {
                    arena.stopGame();
                    countdownSeconds = 600;
                    cancel();
                }

                if(arena.getState() == GameState.LOBBY) {
                    countdownSeconds = 600;
                    cancel();
                }

                if ((countdownSeconds <= 15 || countdownSeconds % 60 == 0) && countdownSeconds != 600) {
                    arena.announceGame(ChatColor.RED + "" + countdownSeconds + ChatColor.YELLOW + " second" + (countdownSeconds == 1 ? "" : "s") + " until the game ends!");
                }
                countdownSeconds--;
                arena.getSm().updateCollected(countdownSeconds);
            }
        }.runTaskTimer(main, 0L, 20L);
    }

}
