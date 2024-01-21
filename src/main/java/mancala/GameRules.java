package mancala;
import java.io.Serializable;

public abstract class GameRules implements Serializable{
    private static final long serialVersionUID = 1L;
    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private String gameType;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        this.gameBoard = new MancalaDataStructure();
    }

    public void setPlayer(final int player){
        currentPlayer = player;
    }
    
    public int getPlayerNum(){
        return currentPlayer;
    }

    public void setGameType(final String type){
        gameType = type;
    }

    public String getGameType(){
        return gameType;
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */

     //was protected

    protected MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    public MancalaDataStructure getGameBoard(){
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    public boolean isSideEmpty(final int pitNum) {
        // Implementation depends on the layout of the board.
        // This is a simplified example and needs to be adjusted according to actual data structure.
        if (pitNum <= 6) {
            for (int i = 0; i < 6; i++) {
                if (getDataStructure().getData().get(i).getStoneCount() > 0) {
                    return false;
                }
            }
        } else {
            for (int i = 7; i < 13; i++) {
                if (getDataStructure().getData().get(i).getStoneCount() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    public abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    public abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // Create a store for each player and set the owner
        Player player1 = one;
        Player player2 = two;

        final Store store1 = new Store();
        final Store store2 = new Store();

        player1.setStore(store1);
        player2.setStore(store2);

        store1.setOwner(one);
        store2.setOwner(two);

        gameBoard.setStore(store1, 1);
        gameBoard.setStore(store2, 2);
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        for (int i =0; i < 14;i++){
            gameBoard.getData().get(i).removeStones();
        }
        gameBoard.setUpPits();
    }

    @Override
    public String toString() {
        // Implement toString() method logic here.
        // This should return a string representation of the game board.
        // Here's a placeholder for the actual implementation.
        return gameBoard.toString();
    }
}
