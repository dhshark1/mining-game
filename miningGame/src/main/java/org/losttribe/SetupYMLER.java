package org.losttribe;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.losttribe.miningGame.MiningGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetupYMLER {

    private MiningGame main;

    private YamlConfiguration gameData;


    public SetupYMLER(MiningGame main) {
        this.main = main;
        gameData = getGameData();
    }

    public YamlConfiguration getGameData() {
        File file = new File(main.getDataFolder(), "game_data.yml");
        if (!file.exists()) {
            Bukkit.getLogger().info("No game_data.yml created. This means there are no activities.");
        }
        YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);
        return modifyFile;
    }

    public void save() {
        File file = new File(main.getDataFolder(), "game_data.yml");
        if (!file.exists()) {
            Bukkit.getLogger().info("No game_data.yml created. This means there are no activities.");
        }
        try {
            gameData.save(file);
            gameData = getGameData();
        } catch (IOException e) {
            Bukkit.getLogger().info("Error saving location to disk.");
            throw new RuntimeException(e);
        }
    }

    // SETTER METHODS

    public void deleteArena(String arenaName) {
        String prefix = "arenas." + arenaName;
        if (gameData.isConfigurationSection(prefix)) {
            prefix = prefix + ".";
            gameData.set(prefix + "min_players", null);
            gameData.set(prefix + "max_players", null);
            gameData.set(prefix + "status", null);
            gameData.set(prefix + "lobby_loc", null);
            gameData.set(prefix + "start_loc", null);
            gameData.set(prefix + "artifacts", null);
            gameData.set(prefix + "break_count", null);
        }
        save();
    }

    // artifacts: list of items

    public void createArena(String arenaName) throws Exception {
        String prefix = "arenas." + arenaName;
        if (!gameData.isConfigurationSection(prefix)) {
            gameData.createSection(prefix);
            prefix = prefix + ".";
            gameData.set(prefix + "min_players", 2);
            gameData.set(prefix + "max_players", 12);
            gameData.set(prefix + "status", false);
            gameData.set(prefix + "break_count", 30);
            gameData.set(prefix + "block_type", Material.SANDSTONE.toString());
        } else {
            throw new Exception("This arena already exists.");
        }
        save();
    }

    public void setBlockType(String arenaName, Material item) {
        String prefix = "arenas." + arenaName + ".block_type";
        gameData.set(prefix, item.toString());
        save();
    }

    public void addArtifact(String arenaName, ItemStack item) {
        List<ItemStack> test;
        String prefix = "arenas." + arenaName + ".artifacts";
        if (gameData.get(prefix) != null) {
            test = (List<ItemStack>) gameData.getList(prefix);
        } else {
            test = new ArrayList<>();
        }
        test.add(item);
        gameData.set(prefix, test);
        save();
    }

    public void removeArtifact(String arenaName, Integer num) throws Exception {
        String prefix = "arenas." + arenaName + ".artifacts";
        if (gameData.isConfigurationSection(prefix)) {
            List<ItemStack> items = (List<ItemStack>) gameData.getList(prefix);
            if (items.size() >= num - 1) {
                items.remove(num - 1);
                gameData.set(prefix, items);
                save();
            } else {
                throw new Exception("This are no artifact of this number.");
            }
        } else {
            throw new Exception("This are no artifacts currently saved.");
        }
    }

    public void clearArtifacts(String arenaName) {
        String prefix = "arenas." + arenaName + ".artifacts";
        gameData.set(prefix, null);
        save();
    }

    public void setArenaPlayerCount(String arenaName, Integer number, String type) {
        String prefix = "arenas." + arenaName + ".";
        if (type.equals("MIN")) {
            prefix = prefix + "min_players";
        } else if (type.equals("MAX")) {
            prefix = prefix + "max_players";
        }
        gameData.set(prefix, number);
        save();
    }

    public void setBreakCount(String arenaName, Integer num) {
        String prefix = "arenas." + arenaName + ".break_count";
        gameData.set(prefix, num);
        save();
    }

    public Integer getBreakCount(String arenaName) {
        String prefix = "arenas." + arenaName + ".break_count";
        if (gameData.contains(prefix)) {
            return gameData.getInt(prefix);
        } else {
            return 30;
        }
    }

    public void setArenaLocation(String arenaName, Location loc, String type) {
        String prefix = "arenas." + arenaName + ".";
        if (type.equals("LOBBY")) {
            prefix = prefix + "lobby_loc";
        } else if (type.equals("START")) {
            prefix = prefix + "start_loc";
        } else if (type.equals("END")) {
            prefix = prefix + "end_loc";
        }
        gameData.set(prefix, loc);
        save();
    }

    public void disableArena(String arenaName) {
        if (doesArenaExist(arenaName)) {
            gameData.set("arenas." + arenaName + ".status", false);
            save();
        }
    }

    public void enableArena(String arenaName) throws Exception {
        String prefix = "arenas." + arenaName + ".";
        if (gameData.contains("arenas." + arenaName)) {
            if (gameData.contains(prefix + "lobby_loc")) {
                if (gameData.contains(prefix + "start_loc")) {
                    if (gameData.contains(prefix + "end_loc")) {
                        if (gameData.contains(prefix + "artifacts")) {
                            gameData.set(prefix + "status", true);
                        } else {
                            throw new Exception("You have not set any artifacts.");
                        }
                    } else {
                        throw new Exception("You have not set the ending location.");
                    }
                } else {
                    throw new Exception("You have not set the starting location.");
                }
            } else {
                throw  new Exception("You have not set the lobby location.");
            }
        } else {
            throw new Exception("This arena does not exist.");
        }
    }

    // GETTER METHODS


    public List<String> getWhatNeedsToBeSet(String arenaName) {
        String prefix = "arenas." + arenaName + ".";
        List<String> set = new ArrayList<>();
        if (gameData.contains("arenas." + arenaName)) {
            if (!gameData.contains(prefix + "lobby_loc")) {
                set.add("Lobby Location");
            }
            if (!gameData.contains(prefix + "start_loc")) {
                set.add("Start location");
            }
            if (!gameData.contains(prefix + "end_loc")) {
                set.add("End location");
            }
            if (!gameData.contains(prefix + "artifacts")) {
                set.add("Artifacts");
            }
        } else {
            set.add("This arena does not exist.");
        }
        return set;
    }

    public Integer getArenaPlayerCount(String arenaName, String type) {
        if (doesArenaExist(arenaName)) {
            String prefix = "arenas." + arenaName + ".";
            if (type.equals("MIN")) {
                return (gameData.getInt(prefix + "min_players"));
            } else if (type.equals("MAX")) {
                return (gameData.getInt(prefix + "max_players"));
            }
            return 0;
        }
        return null;
    }


    public Boolean doesArenaExist(String arenaName) {
        if (gameData.contains("arenas." + arenaName)) {
            return true;
        }
        return false;
    }

    public Location getArenaLocation(String arenaName, String type) {
        if (doesArenaExist(arenaName)) {
            String prefix = "arenas." + arenaName + ".";
            if (type.equals("LOBBY")) {
                return (gameData.getLocation(prefix + "lobby_loc"));
            } else if (type.equals("START")) {
                return (gameData.getLocation(prefix + "start_loc"));
            } else if (type.equals("END")) {
                return (gameData.getLocation(prefix + "end_loc"));
            }
        }
        return null;
    }

    public Boolean getArenaStatus(String arenaName) {
        if (doesArenaExist(arenaName)) {
            String prefix = "arenas." + arenaName + ".status";
            return (gameData.getBoolean(prefix));
        }
        return false;
    }

    public List<ItemStack> getArenaArtifacts(String arenaName) {
        List<ItemStack> artifacts = new ArrayList<>();
        String prefix = "arenas." + arenaName + ".artifacts";
        if (doesArenaExist(arenaName)) {
            if (gameData.contains(prefix)) {
                artifacts = (List<ItemStack>) gameData.getList(prefix);
            }
        }
        return artifacts;
        }

        public Material getBlockType(String arenaName) {
            String prefix = "arenas." + arenaName + ".block_type";
            if (doesArenaExist(arenaName)) {
                return Material.valueOf(gameData.getString(prefix));
            }
            return Material.SANDSTONE;
        }

    public ArenaObject getArena(String arenaName) throws Exception {
        if (doesArenaExist(arenaName)) {
            Boolean status = getArenaStatus(arenaName);
            try {
                enableArena(arenaName);
            } catch(Exception e) {
                Bukkit.getLogger().info("Received error " + e.getMessage() + " when attempting to enable arena named " + arenaName);
            }
            if (status) {
                Integer minPlayers = getArenaPlayerCount(arenaName, "MIN");
                Integer maxPlayers = getArenaPlayerCount(arenaName, "MAX");
                Location startLoc = getArenaLocation(arenaName, "START");
                Location lobbyLoc = getArenaLocation(arenaName, "LOBBY");
                Location endLoc = getArenaLocation(arenaName, "END");
                Material block = getBlockType(arenaName);
                Integer breakNum = getBreakCount(arenaName);
                List<ItemStack> artifacts = getArenaArtifacts(arenaName);
                // MAKE SURE TO ADD ANY DATA YOU NEED TO ADD TO THE ARENA HERE
                ArenaObject arena = new ArenaObject(arenaName, true, startLoc, lobbyLoc, endLoc, minPlayers, maxPlayers, artifacts, breakNum, block, main);
                return arena;
            } else {
                throw new Exception("Status error on getArena");
            }
        }
        throw new Exception("Arena does not exist on getArena.");
    }

    public List<ArenaObject> getAllArenas() {
        List<ArenaObject> arenas = new ArrayList<>();
        if (gameData.contains("arenas.")) {
            for (String str : gameData.getConfigurationSection("arenas.").getKeys(false)) {
                try {
                    ArenaObject arena = getArena(str);
                    arenas.add(arena);
                } catch (Exception e) {
                    Bukkit.getLogger().info(e.getMessage());
                }
            }
        }
        return arenas;
    }




}
