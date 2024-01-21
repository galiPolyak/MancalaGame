package mancala;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Mancala data structure for the Mancala game.
 * Do not change the signature of any of the methods provided.
 * You may add methods if you need them.
 * Do not add game logic to this class
 */
public class MancalaDataStructure implements Serializable{
    private static final long serialVersionUID = 1L;

    private final int playerOneM = 6;
    private final int playerTwoM = 13;
    private  int startStonesM = 4;  //not final because we might want a different size board in the future

    private List<Countable> data = new ArrayList<>();
    private int iteratorPos = 0;
    private int playerSkip = playerTwoM;
    private int pitSkip = -1; // will never match the iteratorPos unless set specifically
    private int skip = -1;
    /**
     * Constructor to initialize the MancalaDataStructure.
     * 
     * @param startStones The number of stones to place in pits at the start of the game. Default values is 4.
     */
    public MancalaDataStructure(int startStones){
        startStonesM = startStones;
        for (int i = 0; i < playerOneM; i++) {
            data.add(new Pit());
        }
        data.add(new Store());
        for (int i = 7; i < playerTwoM; i++) {
            data.add(new Pit());
        }
        data.add(new Store());

        setUpPits();
    }


    /**
     * Constructor to initialize the MancalaDataStructure.
     */
    public MancalaDataStructure() {
        this(4);
    }

    /**
     * Adds stones to a pit.
     *
     * @param pitNum   The number of the pit.
     * @param numToAdd The number of stones to add.
     * @return The current number of stones in the pit.
     */
    public int addStones(int pitNum, int numToAdd) {
        Countable pit = data.get(pitPos(pitNum));
        pit.addStones(numToAdd);
        return pit.getStoneCount();
    }

    public int getIteratorPosition() {
        return iteratorPos;
    }

    /**
     * Removes stones from a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones removed.
     */
    public int removeStones(int pitNum) {
        Countable pit = data.get(pitPos(pitNum));
        return pit.removeStones();
    }

    /**
     * Adds stones to a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @param numToAdd  The number of stones to add to the store.
     * @return The current number of stones in the store.
     */
    public int addToStore(int playerNum, int numToAdd) {
        Countable store = data.get(storePos(playerNum));
        store.addStones(numToAdd);
        return store.getStoneCount();
    }

    /**
     * Gets the stone count in a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @return The stone count in the player's store.
     */
    public int getStoreCount(int playerNum) {
        Countable store = data.get(storePos(playerNum));
        return store.getStoneCount();
    }

    /**
     * Gets the stone count in a given  pit.
     *
     * @param pitNum The number of the pit.
     * @return The stone count in the pit.
     */
    public int getNumStones(int pitNum) {
        Countable pit = data.get(pitPos(pitNum));
        return pit.getStoneCount();
    }    

    /*helper method to convert 1 based pit numbers into array positions*/
    private int pitPos(int pitNum) {
        /*Runtime execeptions don't need to be declared and are
        automatically passed up the chain until caught. This can
        replace the PitNotFoundException*/
        if(pitNum<1 || pitNum > 12){
            throw new RuntimeException("Pit Number Out of Range");
        }
        int pos = pitNum;
        if (pos <= playerOneM) {
            pos--;
        }
        return pos;
    }

    /*helper method to convert player number to an array position*/
    private int storePos(int playerNum) {
        if(playerNum <1 || playerNum > 2){
            throw new RuntimeException("Invalid Player Position");
        }

        int pos = playerOneM;
        if (playerNum == 2) {
            pos = playerTwoM;
        }
        return pos;
    }

    /**
     * Empties both players' stores.
     */
    public void emptyStores() {
        data.set(storePos(1), new Store());
        data.set(storePos(2), new Store());
    }

    /**
     * Sets up pits with a specified number of starting stones.
     *
     * @param startingStonesNum The number of starting stones for each pit.
     */
    public void setUpPits() {
        for (int i = 0; i < playerOneM; i++) {
            data.get(i).addStones(startStonesM);
        }

        for (int i = 7; i < playerTwoM; i++) {
            data.get(i).addStones(startStonesM);
        }

        //data.get(12).addStones(1);
    }

    /**
     * Adds a store that is already connected to a Player.
     *
     * @param store     The store to set.
     * @param playerNum The player number (1 or 2).
     */
    public void setStore(Countable store, int playerNum) {
        data.set(storePos(playerNum), store);
    }
    /*helper method for wrapping the iterator around to the beginning again*/
    private void loopIterator() {
        if (iteratorPos == playerTwoM + 1) {
            iteratorPos = 0;
        }
    }

    private void skipPosition() {
        while (iteratorPos == playerSkip || iteratorPos == pitSkip) {
            iteratorPos++;
            loopIterator();
        }
    }

    private void setSkipPlayer(int playerNum) {
        //sets the skip store to be the opposite player
        playerSkip = playerTwoM;
        if (playerNum == 2) {
            playerSkip = playerOneM;
        }
    }

    private void setSkipPit(int pitNum) {
        pitSkip = pitPos(pitNum);
    }

    public void setSkip(int num){
        this.skip = num;
    }

    public int getSkip(){
        return skip;
    }

    /**
     * Sets the iterator position and positions to skip when iterating.
     *
     * @param startPos       The starting position for the iterator.
     * @param playerNum      The player number (1 or 2).
     * @param skipStartPit   Whether to skip the starting pit.
     */
    public void setIterator(int startPos, int playerNum, boolean skipStartPit) {
        iteratorPos = pitPos(startPos);
        setSkipPlayer(playerNum);
        if (skipStartPit) {
            setSkipPit(startPos);
        }
    }

    public List<Countable> getData(){
        return data;
    }

    /**
     * Moves the iterator to the next position.
     *
     * @return The countable object at the next position.
     */
    public Countable next() {
        iteratorPos++;
        loopIterator(); // in case we've run off the end
        skipPosition(); // skip store and start position if necessary
        return data.get(iteratorPos);
    }




    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();

        // Assuming player two's store is at the end of the list (index 13)
        boardString.append("|Store (Player 2): ").append(getStoreCount(2)).append("|  ");

        // Player two's pits (pits 7 to 12)
        for (int i = 12; i >= 7; i--) {
            boardString.append("[").append(getNumStones(i)).append("] ");
        }
        boardString.append("\n");

        // Spacer for alignment
        boardString.append("                       ");

        // Player one's pits (pits 1 to 6)
        for (int i = 1; i <= 6; i++) {
            boardString.append("[").append(getNumStones(i)).append("] ");
        }

        // Player one's store (at the start of the list, index 6)
        boardString.append("  |Store (Player 1): ").append(getStoreCount(1)).append("|");
        boardString.append("\n");

        return boardString.toString();
    }

}
