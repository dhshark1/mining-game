package org.losttribe.miningGame;

public class PlayerFuelData {

    private boolean gameActive = false;

    private int glowstoneCount;
    private int redstoneCount;
    private int amethystShardCount;

    private final int REQUIRED_COUNT = 64;

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public void addGlowstone(int amount) {
        glowstoneCount += amount;
    }

    public void addRedstone(int amount) {
        redstoneCount += amount;
    }

    public void addAmethystShards(int amount) {
        amethystShardCount += amount;
    }

    public boolean hasAllRequiredFuel() {
        return glowstoneCount >= REQUIRED_COUNT &&
                redstoneCount >= REQUIRED_COUNT &&
                amethystShardCount >= REQUIRED_COUNT;
    }

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


