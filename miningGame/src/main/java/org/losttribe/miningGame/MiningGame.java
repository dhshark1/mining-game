package org.losttribe.miningGame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MiningGame extends JavaPlugin implements PluginMessageListener {

    private PlayerDataManager playerDataManager;
    private SetupYMLER ymler;
    private List<ArenaObject> arenas;

    private String abbrev = "[MG]";
    private HashMap<UUID, LangEnum> playerAndLang;

    @Override
    public void onEnable() {
        playerDataManager = new PlayerDataManager();

        getServer().getPluginManager().registerEvents(
                new MiningGameListener(this, playerDataManager),
                this
        );

        this.playerAndLang = new HashMap<>();
        this.ymler = new SetupYMLER(this);
        this.playerAndLang = new HashMap<>();

        arenas = new ArrayList<>();
        getCommand("shipfuel").setExecutor(new Commands(this, ymler));
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        reloadAllArenas();


        getLogger().info("MiningGame plugin has been enabled!");
    }

    public void startMiningGame(Player player) {
        PlayerFuelData data = playerDataManager.getData(player);

        if (data.isGameActive()) {
            player.sendMessage("§cYou already have a MiningGame in progress!");
            return;
        }

        data.reset();
        data.setGameActive(true);
        player.sendMessage("§aYou have started the MiningGame! Begin collecting fuel items!");
    }

    public void resetMiningGame(Player player) {
        PlayerFuelData data = playerDataManager.getData(player);
        data.reset();
        data.setGameActive(false);
        player.sendMessage("§aYour MiningGame progress has been reset. Use /mininggame start to begin again!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
            player.sendMessage("§eUsage: /mininggame <start|progress|reset>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "start":
                if (sender instanceof Player) {
                    Player instancePlayer = (Player) sender;
                    startMiningGame(instancePlayer);
                }
                return true;
            case "progress":
                if (!data.isGameActive()) {
                    player.sendMessage("§cYou haven't started the MiningGame yet! Use §e/mininggame start§c.");
                    return true;
                }
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

                if (data.hasAllRequiredFuel()) {
                    player.sendMessage("§aGreat job! You've collected enough fuel to get home!");
                }
                return true;

            case "reset":
                resetMiningGame(player);
                return true;

            default:
                player.sendMessage("§eUsage: /mininggame <start|progress|reset>");
                return true;
        }
    }

    public String getAbbrev() {
        return abbrev;
    }

    public List<String> getAllArenaNames() {
        List<String> l = new ArrayList<>();
        if (!arenas.isEmpty()) {
            for (ArenaObject ar : arenas) {
                l.add(ar.getArenaName());
            }}
        return l;
    }

    public boolean isAnActiveArena(String arenaName) {
        if (!arenas.isEmpty()) {
            for (ArenaObject ar : arenas) {
                if (ar.getArenaName().equals(arenaName)) {
                    return true;
                }
            }}
        return false;
    }

    public void removeArena(String arenaName) {
        if (!arenas.isEmpty()) {
            if (isAnActiveArena(arenaName)) {
                ArenaObject ar = getArenaByName(arenaName);
                arenas.remove(ar);
            }}
    }

    public void updateArena(ArenaObject newArena) {
        if (!arenas.isEmpty()) {

            if (isAnActiveArena(newArena.getArenaName())) {
                ArenaObject oldArena = getArenaByName(newArena.getArenaName());
                arenas.remove(oldArena);
            }
            arenas.add(newArena);
        }
    }

    public void reloadAllArenas() {
        arenas = ymler.getAllArenas();
    }

    public Boolean isPlayerInAGame(Player player) {
        if (!arenas.isEmpty()) {

            for (ArenaObject ar : arenas) {
                if (ar.isPlayerInGame(player)) {
                    return true;
                }
            }
        }
        return false;
    }
    public ArenaObject getArenaByPlayer(Player player) {
        if (!arenas.isEmpty()) {
            if (isPlayerInAGame(player)) {
                for (ArenaObject ar: arenas) {
                    if (ar.isPlayerInGame(player)) {
                        return ar;
                    }
                }
            }}
        return null;
    }

    public ArenaObject getArenaByName(String arenaName) {
        if (!arenas.isEmpty()) {
            for (ArenaObject ar : arenas) {
                if (ar.getArenaName().equals(arenaName)) {
                    return ar;
                }
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
        if (!arenas.isEmpty()) {
            for (ArenaObject ar : arenas) {
                if (ar.getState() == GameState.IN_GAME) {
                    ar.stopGame();
                }
            }
        }
        getLogger().info("MiningGame plugin has been disabled!");
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            Bukkit.getLogger().info("language received");
            String subChannel = in.readUTF();
            if (!subChannel.equals("Languages")) return;
            short length = in.readShort();
            byte[] msgBytes = new byte[length];
            in.readFully(msgBytes);
            Bukkit.getLogger().info("in the language");
            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            String key = msgIn.readUTF();
            String value = msgIn.readUTF();
            if (key.equals("ENGLISH") || key.equals("HEBREW")) {
                playerAndLang.put(UUID.fromString(value), LangEnum.ENGLISH.convert(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
