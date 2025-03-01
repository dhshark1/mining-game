package org.losttribe.miningGame;

/**
 * Holds the counts of each type of "fuel" item a player collects,
 * along with whether or not the game is active for them.
 */
public class PlayerFuelData {

    private boolean gameActive = false;

    private int glowstoneCount;
    private int redstoneCount;
    private int amethystShardCount;

    // The goal for each type of fuel
    private final int REQUIRED_COUNT = 2;

    /**
     * Whether this player's MiningGame is currently active.
     */
    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    /**
     * Increase counters for each item.
     */
    public void addGlowstone(int amount) {
        glowstoneCount += amount;
    }

    public void addRedstone(int amount) {
        redstoneCount += amount;
    }

    public void addAmethystShards(int amount) {
        amethystShardCount += amount;
    }

    /**
     * Checks if the player meets the requirement for each item.
     */
    public boolean hasAllRequiredFuel() {
        return glowstoneCount >= REQUIRED_COUNT &&
                redstoneCount >= REQUIRED_COUNT &&
                amethystShardCount >= REQUIRED_COUNT;
    }

    /**
     * Reset all counters and set game to inactive.
     */
    public void reset() {
        glowstoneCount = 0;
        redstoneCount = 0;
        amethystShardCount = 0;
    }

    public int getGlowstoneCount() {
        return glowstoneCount;
    }

    public int getRedstoneCount() {
        return redstoneCount;
    }

    public int getAmethystShardCount() {
        return amethystShardCount;
    }

    public int getRequiredCount() {
        return REQUIRED_COUNT;
    }
}


