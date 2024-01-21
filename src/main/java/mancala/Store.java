package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable {

    private static final long serialVersionUID = 1L;
    
    private Player owner;
    private int totalStones;

    public Store() {
        // Default constructor
        this.totalStones = 0;
    }

    public void setOwner(final Player player) {
        this.owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public int getStoneCount() {
        return totalStones;
    }

    @Override
    public void addStone() {
        totalStones++;
    }

    @Override
    public void addStones(final int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add a negative number of stones.");
        }
        this.totalStones += amount;
    }

    @Override
    public int removeStones() {
        final int stonesRemoved = this.totalStones;
        this.totalStones = 0;
        return stonesRemoved;
    }

    @Override
    public String toString() {
        return "Store of " + (owner != null ? owner.getName() : "unknown") + ": " + totalStones + " stones";
    }
}
