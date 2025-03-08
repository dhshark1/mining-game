package org.losttribe.miningGame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Commands implements CommandExecutor {

    private MiningGame main;
    private SetupYMLER ymler;

    private GUIInterface gui;

    public Commands(MiningGame main, SetupYMLER ymler) {
        this.main = main;
        this.ymler = ymler;
        this.gui = new GUIInterface(main);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length == 1) {
            if (strings[0].equals("help")) {
                player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "~~~~ " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "lunar Help" + ChatColor.AQUA + ChatColor.BOLD.toString() + " ~~~~");
                player.sendMessage(ChatColor.AQUA + "/shipfuel" + ChatColor.YELLOW + " join " + ChatColor.GREEN + "(Arena Name)");
                player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "leave");
                if (player.hasPermission("lt.instructor")) {
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "kick " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "forcejoin " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "stop " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "start " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "list " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "groupjoin " + ChatColor.GREEN + "(Arena Name) " + ChatColor.GREEN + "(Group Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel " + ChatColor.YELLOW + "listarenas");
                }
                if (player.hasPermission("lt.admin")) {
                    player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "~~~~ " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "lunar Setup Help" + ChatColor.AQUA + ChatColor.BOLD.toString() + " ~~~~");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.YELLOW + "create " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.YELLOW + "delete " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.GREEN + "(Arena Name) " + ChatColor.YELLOW + "enable");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.GREEN + "(Arena Name) " + ChatColor.YELLOW + "disable");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.GREEN + "(Arena Name) " + ChatColor.YELLOW + "setloc start/lobby/end");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.GREEN + "(Arena Name) " + ChatColor.YELLOW + "status");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.YELLOW + "reloadall");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.YELLOW + "reload " + ChatColor.GREEN + "(Arena Name)");
                    player.sendMessage(ChatColor.AQUA + "/shipfuel setup " + ChatColor.GREEN + "(Arena Name) " + ChatColor.YELLOW + "minplayers/maxplayers " + ChatColor.GREEN + "(number)");
                }
            } else if (strings[0].equals("leave")) {
                if (main.isPlayerInAGame(player)) {
                    ArenaObject ar = main.getArenaByPlayer(player);
                    if (ar != null) {
                        ar.leaveGame(player);
                    }
                }
            } else if (strings[0].equals("join")) {
                if (main.getAllArenaNames().size() == 1) {
                    ArenaObject ar = main.getArenaByName(main.getAllArenaNames().get(0));
                    if (ar != null) {
                        ar.joinGame(player);
                    }
                }
            } else if (strings[0].equals("listarenas") && player.hasPermission("lt.instructor")) {
                if (!main.getAllArenaNames().isEmpty()) {
                    player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "~~~~ " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "lunar Arena List" + ChatColor.AQUA + ChatColor.BOLD.toString() + " ~~~~");
                    for (String i : main.getAllArenaNames()) {
                        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + i);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "There are currently no arenas set up.");
                }
            }
            if (main.isPlayerInAGame(player) && player.hasPermission("lt.instructor")) {
                ArenaObject arena = main.getArenaByPlayer(player);
                if (strings[0].equals("kick")) {
                    gui.kickPlayerGUI(player, arena.getArenaName());
                } else if (strings[0].equals("forcejoin")) {
                    gui.forceJoinPlayerGUI(player, arena.getArenaName());
                } else if (strings[0].equals("start")) {
                    arena.startGame();
                    player.sendMessage(ChatColor.GREEN + "Starting the game.");
                } else if (strings[0].equals("stop")) {
                    arena.stopGame();
                    player.sendMessage(ChatColor.RED + "Stopping the game.");
                }
            }
        } else if (strings.length == 2) {
            if (strings[0].equals("join")) {
                if (main.isAnActiveArena(strings[1])) {
                    ArenaObject ar = main.getArenaByName(strings[1]);
                    ar.joinGame(player);
                } else {
                    player.sendMessage(ChatColor.RED + "This is not a valid arena name.");
                }
            } else if (strings[0].equals("groupjoin") && player.hasPermission("lt.instructor")) {
                if (main.isPlayerInAGame(player)) {
                    ArenaObject ar = main.getArenaByPlayer(player);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("group." + strings[1])) {
                            ar.joinGame(p);
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "Added all online players in group"  + strings[2] + " to game.");
                } else {
                    player.sendMessage(ChatColor.RED + "You must specify an arena.");
                }
            } else if (strings[0].equals("start") && player.hasPermission("lt.instructor")) {
                if (main.isPlayerInAGame(player)) {
                    ArenaObject ar = main.getArenaByPlayer(player);
                    ar.startGame();
                    player.sendMessage(ChatColor.GREEN + "Starting the game you are in.");
                } else {
                    if (main.getArenaByName(strings[1]) != null) {
                        ArenaObject ar = main.getArenaByName(strings[1]);
                        ar.startGame();
                        player.sendMessage(ChatColor.GREEN + "Starting the game.");
                    }
                }
            } else if (strings[0].equals("stop") && player.hasPermission("lt.instructor")) {
                if (main.isPlayerInAGame(player)) {
                    ArenaObject ar = main.getArenaByPlayer(player);
                    ar.stopGame();
                    player.sendMessage(ChatColor.RED + "Stopping the game you are in.");
                } else {
                    if (main.getArenaByName(strings[1]) != null) {
                        ArenaObject ar = main.getArenaByName(strings[1]);
                        ar.stopGame();
                    }
                }
            } else if (strings[0].equals("list") && player.hasPermission("lt.instructor")) {
                if (main.isAnActiveArena(strings[1])) {
                    ArenaObject ar = main.getArenaByName(strings[1]);
                    player.sendMessage(ChatColor.YELLOW + "Players in " + ChatColor.LIGHT_PURPLE + strings[1] + ChatColor.YELLOW + ":");
                    player.sendMessage(ChatColor.GRAY + "" + ar.getPlayersList());
                }
            } else if (strings[0].equals("setup")) {
                if (player.hasPermission("lt.admin")) {
                    if (strings[1].equals("reloadall")) {
                        main.reloadAllArenas();
                        player.sendMessage(ChatColor.YELLOW + "Reloaded all arenas.");
                    }
                }
            } else if (strings[0].equals("forcejoin") && player.hasPermission("lt.instructor")) {
                if (main.isAnActiveArena(strings[1])) {
                    gui.forceJoinPlayerGUI(player, strings[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "This is not a valid arena.");
                }
            } else if (strings[0].equals("kick") && player.hasPermission("lt.instructor")) {
                if (main.isAnActiveArena(strings[1])) {
                    gui.kickPlayerGUI(player, strings[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "This is not a valid arena.");
                }
            }
        } else if (strings.length >= 3) {
            if (strings[0].equals("groupjoin") && player.hasPermission("lt.instructor")) {
                if (main.isAnActiveArena(strings[1])) {
                    ArenaObject ar = main.getArenaByName(strings[1]);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("group." + strings[2])) {
                            ar.joinGame(p);
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "Added all online players in group"  + strings[2] + " to game.");
                }
            } else if (strings[0].equals("setup")) {
                if (player.hasPermission("lt.admin")) {
                    if (strings[1].equalsIgnoreCase("create")) {
                        if (!ymler.doesArenaExist(strings[2])) {
                            try {
                                ymler.createArena(strings[2]);
                                player.sendMessage(ChatColor.GREEN + "Arena created by the name " + ChatColor.YELLOW + strings[2] + ChatColor.GREEN + ".");
                            } catch (Exception e) {
                                player.sendMessage(ChatColor.RED + e.getMessage());
                            }
                        }
                    } else if (strings[1].equalsIgnoreCase("delete")) {
                        if (ymler.doesArenaExist(strings[2])) {
                            ymler.deleteArena(strings[2]);
                            player.sendMessage(ChatColor.RED + "Deleted the arena.");
                            if (main.isAnActiveArena(strings[2])) {
                                main.removeArena(strings[2]);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "This arena does not exist.");
                        }
                    } else {
                        if (ymler.doesArenaExist(strings[1])) {
                            String arenaName = strings[1];
                            if (strings[2].equalsIgnoreCase("enable")) {
                                try {
                                    ymler.enableArena(arenaName);
                                    player.sendMessage(ChatColor.GREEN + "Successfully enabled the arena. Please reload all arenas for this to take effect.");
                                    main.updateArena(ymler.getArena(arenaName));
                                } catch (Exception e) {
                                    player.sendMessage(ChatColor.RED + e.getMessage());
                                }
                            } else if (strings[2].equalsIgnoreCase("disable")) {
                                ymler.disableArena(arenaName);
                                player.sendMessage(ChatColor.YELLOW + "Disabled the arena. Please reload all arenas for this to take effect.");
                                main.removeArena(arenaName);
                            } else if (strings[2].equalsIgnoreCase("setloc")) {
                                String type = "";
                                if (strings[3].equalsIgnoreCase("lobby")) {
                                    type = "LOBBY";
                                } else if (strings[3].equalsIgnoreCase("start")) {
                                    type = "START";
                                } else if (strings[3].equals("end")) {
                                    type = "END";
                                }
                                ymler.setArenaLocation(arenaName, player.getLocation(), type);
                                player.sendMessage("Set the location for " + strings[3] + " to your current location.");
                            } else if (strings[2].equalsIgnoreCase("status")) {
                                List<String> statusInfo = ymler.getWhatNeedsToBeSet(arenaName);
                                player.sendMessage(ChatColor.DARK_AQUA + "Needs to Be Set:");
                                if (!statusInfo.isEmpty()) {
                                    for (String str : statusInfo) {
                                        player.sendMessage(ChatColor.RED + str);
                                    }
                                } else {
                                    player.sendMessage(ChatColor.GOLD + "It is all set!");
                                }
                            } else if (strings[2].equalsIgnoreCase("reload")) {
                                try {
                                    main.updateArena(ymler.getArena(arenaName));
                                } catch (Exception e) {
                                    player.sendMessage(ChatColor.RED + e.getMessage());
                                }
                            } else if (strings[2].equalsIgnoreCase("minplayers")) {
                                if (Integer.valueOf(strings[3]) != null && Integer.valueOf(strings[3]) > 0) {
                                    ymler.setArenaPlayerCount(arenaName, Integer.valueOf(strings[3]), "MIN");
                                    player.sendMessage(ChatColor.GREEN + "The minimum player count for arena " + ChatColor.YELLOW + strings[1] + ChatColor.GREEN + " has been set to " + ChatColor.YELLOW + strings[3] + ChatColor.GREEN + ".");
                                } else {
                                    player.sendMessage(ChatColor.RED + "This is not a valid number.");
                                }
                            } else if (strings[2].equalsIgnoreCase("maxplayers") && Integer.valueOf(strings[3]) <= 48) {
                                if (Integer.valueOf(strings[3]) != null) {
                                    ymler.setArenaPlayerCount(arenaName, Integer.valueOf(strings[3]), "MAX");
                                    player.sendMessage(ChatColor.GREEN + "The maximum player count for arena " + ChatColor.YELLOW + strings[1] + ChatColor.GREEN + " has been set to " + ChatColor.YELLOW + strings[3] + ChatColor.GREEN + ".");
                                } else {
                                    player.sendMessage(ChatColor.RED + "This is not a valid number.");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "This is not a valid arena name.");
                        }
                    }
                }
            }
        }
        return false;
    }
}
