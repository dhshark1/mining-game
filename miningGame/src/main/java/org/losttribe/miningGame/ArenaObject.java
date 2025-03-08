package org.losttribe.miningGame;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArenaObject {

    private List<Player> playersList;

    private String arenaName;

    private Integer breakCount;

    private List<Location> blocksBroken;

    private GameState state = GameState.LOBBY;

    private List<ItemStack> artifacts;

    private Boolean status;

    private ScoreboardManager sm;

    private String prefix = ChatColor.DARK_AQUA + "[Mining Game] " + ChatColor.GRAY;

    private List<Player> completedPlayers;

    private Location startLoc;

    private Location lobbyLoc;

    private Integer minPlayers;

    private Material blockToMine;

    private List<ItemStack> playingArtifacts;

    private Location endLoc;

    private Integer maxPlayers;

    private Countdown countdown;

    private MiningGame main;

    public ArenaObject(String arenaName, Boolean status, Location startLoc, Location lobbyLoc, Location endLoc, Integer minPlayers, Integer maxPlayers, List<ItemStack> artifacts, Integer breakCount, Material block, MiningGame main) {
        this.arenaName = arenaName;
        this.status = status;
        this.artifacts = artifacts;
        this.startLoc = startLoc;
        this.lobbyLoc = lobbyLoc;
        this.endLoc = endLoc;
        this.minPlayers = minPlayers;
        this.blockToMine = block;
        this.maxPlayers = maxPlayers;
        this.breakCount = breakCount;
        this.playersList = new ArrayList<>();
        this.completedPlayers = new ArrayList<>();
        this.blocksBroken = new ArrayList<>();
        this.playingArtifacts = new ArrayList<>();
        this.main = main;
        countdown = new Countdown(main, this);

    }

    // ALL NEEDED GETTERS


    public GameState getState() {
        return state;
    }

    public void startGame() {
        sm = new ScoreboardManager();
        playingArtifacts = new ArrayList<>(artifacts);
        Collections.shuffle(playingArtifacts);
        for (Player player : playersList) {
            player.teleport(startLoc);
            sm.setScoreboard(player);
            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
            player.sendMessage(ChatColor.GRAY + "Welcome to " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "Mining Game" + ChatColor.GRAY );
            player.sendMessage(ChatColor.GRAY + "You were sent to dig and discover different artifacts. So don't just stand there, get digging! You'll know you found something when a barrel appears upon block break.");
        }
        // RANDOMIZE PLACEMENT? DOESNT IT GET PLACED BY THE BREAKING?
        state = GameState.IN_GAME;
        countdown.Timer();
    }

    public ScoreboardManager getSm() {
        return sm;
    }

    public void stopGame() {
        state = GameState.LOBBY;
        for (Player player : playersList) {
            player.getInventory().clear();
            player.teleport(endLoc);
            player.sendMessage(ChatColor.YELLOW + "Congratulations! All artifacts have been found, and the game is over.");
            // INPUT END MESSAGE HERE
            sm.clearScoreboard(player);
        }
        playersList.clear();
        for (Location loc : blocksBroken) { // RUN THIS ASYNC
            loc.getBlock().setType(blockToMine);
        }
        playingArtifacts.clear();
        state = GameState.LOBBY;
        blocksBroken.clear();
        // RESET ANY NEEDED ARRAYS/HASHMAPS/VARIABLES TO BE RESET HERE
    }

    public List<ItemStack> getArtifacts() {
        return artifacts;
    }

    public Boolean isPlayerInGame(Player player) {
        if (playersList.contains(player)) {
            return true;
        }
        return false;
    }


    public void joinGame(Player player) {
        if (!isPlayerInGame(player) && playersList.size() <= maxPlayers) {
            if (state == GameState.LOBBY) {
                player.sendMessage(prefix + ChatColor.YELLOW + "You have joined the game.");
                playersList.add(player);
                player.teleport(lobbyLoc);
                player.getInventory().clear();
                announceGame(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has joined the game " + ChatColor.GRAY +  "(" + playersList.size() + "/" + maxPlayers + ").");
            } else {
                player.sendMessage(prefix + "You cannot join as the game is already in session!");
            }
        }
    }

    public List<Player> getPlayersList() {
        return playersList;
    }



    public void leaveGame(Player player) {
        if (isPlayerInGame(player)) {
            playersList.remove(player);
            player.getInventory().clear();
            player.teleport(lobbyLoc);
            if (state == GameState.LOBBY) {
                announceGame(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has left the game " + ChatColor.GRAY +  "(" + playersList.size() + "/" + maxPlayers + ").");
                player.sendMessage(prefix + ChatColor.RED + "You have left the game.");
            } else if (state == GameState.IN_GAME) {
                // MAKE SURE TO REMOVE PLAYER FROM ANY TRACKING HERE
                sm.clearScoreboard(player);
                player.sendMessage(prefix + ChatColor.RED + "You have left the game.");
            }
        }
    }

    public String getArenaName() {
        return arenaName;
    }

    public void announceGame(String message) {
        for (Player player : playersList) {
            player.sendMessage(prefix + ChatColor.YELLOW + message);
        }
    }
}
