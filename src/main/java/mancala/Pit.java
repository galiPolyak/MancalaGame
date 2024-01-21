package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable {

    private static final long serialVersionUID = 1L;
    private int stoneCount;

    public Pit() {
        // Default constructor initializes with no stones in the pit.
        this.stoneCount = 0;
    }

    @Override
    public int getStoneCount() {
        return stoneCount;
    }

    @Override
    public void addStone() {
        stoneCount++;
    }

    @Override
    public void addStones(final int numToAdd) {
        if (numToAdd < 0) {
            throw new IllegalArgumentException("Cannot add a negative number of stones.");
        }
        stoneCount += numToAdd;
    }

    @Override
    public int removeStones() {
        final int removedStones = stoneCount;
        stoneCount = 0; // Reset the count to zero after removing stones.
        return removedStones;
    }

    @Override
    public String toString() {
        return "Pit: " + stoneCount + " stones";
    }
}
