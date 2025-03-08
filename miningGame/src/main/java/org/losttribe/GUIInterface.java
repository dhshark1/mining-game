package org.losttribe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.losttribe.miningGame.MiningGame;

import java.util.Arrays;
import java.util.Collection;

public class GUIInterface {

    private MiningGame main;

    public GUIInterface(MiningGame main) {
        this.main = main;
    }

    // yes I know the I already stands for interface

    public ItemStack getPlayerHead(Player p, String lore) {
        ItemStack personHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta personMeta = (SkullMeta) personHead.getItemMeta();
        personMeta.setOwningPlayer(p);
        personMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + p.getName());
        personMeta.setLore(Arrays.asList(ChatColor.GRAY + lore));
        personHead.setItemMeta(personMeta);
        return personHead;
    }

    public void kickPlayerGUI(Player player, String arenaName) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.RED.toString() + ChatColor.BOLD + main.getAbbrev() + " Kick Player: " + arenaName);
        Integer count = 0;

        ArenaObject arena = main.getArenaByName(arenaName);

        for (Player p : arena.getPlayersList()) {
            ItemStack personHead = getPlayerHead(p, "Select to kick from game.");
            inv.setItem(count, personHead);
            count++;
        }
        player.openInventory(inv);

    }

    public void forceJoinPlayerGUI(Player player, String arenaName) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.GREEN.toString() + ChatColor.BOLD + main.getAbbrev() + " Force Join Player: " + arenaName);
        Integer count = 0;

        ArenaObject arena = main.getArenaByName(arenaName);

        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();

        for (Player p : players) {
            if (!arena.getPlayersList().contains(p)) {
                ItemStack personHead = getPlayerHead(p, "Select to force the player to join game.");
                inv.setItem(count, personHead);
                count++;
            }
        }
        player.openInventory(inv);

    }

}
