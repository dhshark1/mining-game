package org.losttribe.miningGame;

import org.losttribe.miningGame.PlayerDataManager;
import org.losttribe.miningGame.PlayerFuelData;
import org.losttribe.miningGame.MiningGameListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MiningGame extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        // Initialize data manager
        playerDataManager = new PlayerDataManager();

        // Register event listener
        getServer().getPluginManager().registerEvents(
                new MiningGameListener(this, playerDataManager),
                this
        );

        getLogger().info("MiningGame plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cleanup or data-saving logic if needed
        getLogger().info("MiningGame plugin has been disabled!");
    }

    /**
     * Simple onCommand override to handle /mininggame subcommands.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // We only have one command: /mininggame
        if (!command.getName().equalsIgnoreCase("mininggame")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        PlayerFuelData data = playerDataManager.getData(player);

        if (args.length == 0) {
            // No subcommand given
            player.sendMessage("§eUsage: /mininggame <start|progress|reset>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "start":
                // Reset counters to 0 and activate the game for this player
                data.reset();
                data.setGameActive(true);
                player.sendMessage("§aYou have started the MiningGame! Begin collecting fuel items!");
                return true;

            case "progress":
                if (!data.isGameActive()) {
                    player.sendMessage("§cYou haven't started the MiningGame yet! Use §e/mininggame start§c.");
                    return true;
                }
                // Show how many items are still needed
                int neededGlowstone = data.getRequiredCount() - data.getGlowstoneCount();
                int neededRedstoneDust = data.getRequiredCount() - data.getRedstoneCount();
                int neededAmethyst = data.getRequiredCount() - data.getAmethystShardCount();

                player.sendMessage("§eMiningGame Progress:");
                player.sendMessage("  §6Glowstone Dust: §f" + data.getGlowstoneCount()
                        + "§7 / " + data.getRequiredCount()
                        + " §8(need " + Math.max(0, neededGlowstone) + " more)");
                player.sendMessage("  §cRedstone Dust: §f" + data.getRedstoneCount()
                        + "§7 / " + data.getRequiredCount()
                        + " §8(need " + Math.max(0, neededRedstoneDust) + " more)");
                player.sendMessage("  §dAmethyst Shards: §f" + data.getAmethystShardCount()
                        + "§7 / " + data.getRequiredCount()
                        + " §8(need " + Math.max(0, neededAmethyst) + " more)");

                // If the player has completed the requirements:
                if (data.hasAllRequiredFuel()) {
                    player.sendMessage("§aGreat job! You've collected enough fuel to get home!");
                }
                return true;

            case "reset":
                data.reset();
                data.setGameActive(false);
                player.sendMessage("§aYour MiningGame progress has been reset. Use /mininggame start to begin again!");
                return true;

            default:
                player.sendMessage("§eUsage: /mininggame <start|progress|reset>");
                return true;
        }
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
