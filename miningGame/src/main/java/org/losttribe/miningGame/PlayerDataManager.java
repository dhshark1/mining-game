package org.losttribe.miningGame;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the PlayerFuelData for each player.
 */
public class PlayerDataManager {

    private final Map<UUID, PlayerFuelData> playerDataMap = new HashMap<>();

    /**
     * Gets the PlayerFuelData for this player, creating if necessary.
     */
    public PlayerFuelData getData(Player player) {
        return playerDataMap.computeIfAbsent(
                player.getUniqueId(),
                uuid -> new PlayerFuelData()
        );
    }
}
