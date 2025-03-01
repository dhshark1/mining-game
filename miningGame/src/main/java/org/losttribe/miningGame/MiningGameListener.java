package org.losttribe.miningGame;

import org.losttribe.miningGame.MiningGame;
import org.losttribe.miningGame.PlayerDataManager;
import org.losttribe.miningGame.PlayerFuelData;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class MiningGameListener implements Listener {

    private final MiningGame plugin;
    private final PlayerDataManager playerDataManager;

    public MiningGameListener(MiningGame plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        PlayerFuelData fuelData = playerDataManager.getData(player);

        // If the player's game isn't active, don't track pickups.
        if (!fuelData.isGameActive()) {
            return;
        }

        ItemStack pickedUpItem = event.getItem().getItemStack();
        Material material = pickedUpItem.getType();
        int amount = pickedUpItem.getAmount();

        // Check which material the player picked up
        switch (material) {
            case GLOWSTONE_DUST:
                fuelData.addGlowstone(amount);
                break;
            case REDSTONE:
                fuelData.addRedstone(amount);
                break;
            case AMETHYST_SHARD:
                fuelData.addAmethystShards(amount);
                break;
            default:
                return;
        }

        // After updating, check if the player has met all requirements
        if (fuelData.hasAllRequiredFuel()) {
            player.sendMessage("§a[MiningGame] You have collected enough fuel to get home!");
            plugin.getServer().broadcastMessage(
                    "§6" + player.getName() + " has gathered all required fuel for the MiningGame!"
            );
        }
    }
}

