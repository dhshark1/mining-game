package org.losttribe.miningGame;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerDataManager {

    private final Map<UUID, PlayerFuelData> playerDataMap = new HashMap<>();

    public PlayerFuelData getData(Player player) {
        return playerDataMap.computeIfAbsent(
                player.getUniqueId(),
                uuid -> new PlayerFuelData()
        );
    }
}
