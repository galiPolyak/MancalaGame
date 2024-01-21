package mancala;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MancalaGame implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private GameRules gameRules;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private boolean isGameDone;

    /**
     * Constructor for MancalaGame.
     */
    public MancalaGame() {
        // You should initialize your game rules here (KalahRules or AyoRules)
        // For example, you could decide based on a setting or configuration
        
        this.gameRules = new KalahRules(); // or AyoRules

        // Create players with the profiles
        playerOne = new Player();
        playerTwo = new Player();
        // Register players in the game
        gameRules.registerPlayers(playerOne, playerTwo);
        
        setCurrentPlayer(playerOne);
    }

    public GameRules getGameRules(){
        return gameRules;
    }

    public void setGameRules(final GameRules rules){
        this.gameRules = rules;
    }


    /**
     * Gets the game board.
     *
     * @return The GameRules object representing the board.
     */
    public GameRules getBoard() {
        return gameRules;
    }

    /**
     * Gets the current player.
     *
     * @return The current Player object.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the number of stones in a specific pit.
     *
     * @param pitNum The pit number to check.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameRules.getNumStones(pitNum);
    }

    /**
     * Gets the stone count in the current player's store.
     *
     * @param player The player whose store to check.
     * @return The stone count in the player's store.
     */
    public int getStoreCount(final Player player) {
        // Assuming Player class has a method to get the store number
        final int playerNum = "Player1".equals(currentPlayer.getName()) ? 1 : 2;
        return gameRules.getDataStructure().getStoreCount(playerNum);
    }

    /**
     * Gets the winner of the game.
     *
     * @return The Player who has won the game.
     */
    public boolean isGameOver() {
        // Assuming pits 1-6 belong to player one, and pits 7-12 to player two.
        final boolean sideOneEmpty = gameRules.isSideEmpty(1);
        final boolean sideTwoEmpty = gameRules.isSideEmpty(7);
    
        return sideOneEmpty || sideTwoEmpty;
    }
    
    public Player getWinner() {
        if (!isGameOver()) {
            return null; // The game is still ongoing.
        }
    
        final int stonesPlayerOne = gameRules.getDataStructure().getStoreCount(1);
        final int stonesPlayerTwo = gameRules.getDataStructure().getStoreCount(2);
    
        if (stonesPlayerOne > stonesPlayerTwo) {
            return playerOne;
        } else if (stonesPlayerTwo > stonesPlayerOne) {
            return playerTwo;
        } else {
            return null; // It's a tie.
        }
    }
    

    /**
     * Executes a move from the specified pit.
     *
     * @param startPit The pit number from which to move stones.
     * @return The number of stones that were moved.
     */
    public int move(final int startPit) throws InvalidMoveException {
        if (startPit < 1 || startPit > 12){
            throw new InvalidMoveException();
        }else{
            final int storeIncrease = gameRules.moveStones(startPit, gameRules.getPlayerNum());
            isGameDone = isGameOver();
            if (isGameDone){
                placePitsToStore();
                finalizeGame(gameRules.getGameType());
            }
            if (gameRules.getPlayerNum() == 1){
                setCurrentPlayer(playerOne);
            } else{
                setCurrentPlayer(playerTwo);
            }

            return storeIncrease;
        }  
    }

    private void placePitsToStore(){
        if (gameRules.isSideEmpty(1)) {
            moveRemainingStonesToStore(7, 12, 13);
        }
        if (gameRules.isSideEmpty(7)) {
            moveRemainingStonesToStore(0, 5, 6);
        }
    }

    private void moveRemainingStonesToStore(final int start, final int end, final int storeIndex) {
        int stoneCount = 0;

        for (int i = start; i <= end; i++) {
            stoneCount += gameRules.getDataStructure().getData().get(i).removeStones();
        }
        gameRules.getDataStructure().getData().get(storeIndex).addStones(stoneCount);
    }

    /**
     * Sets the game board.
     *
     * @param theBoard The GameRules object to set as the board.
     */
    public void setBoard(final GameRules theBoard) {
        this.gameRules = theBoard;
    }

    /**
     * Sets the current player.
     *
     * @param player The Player object to set as the current player.
     */
    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    /**
     * Sets the players of the game.
     *
     * @param onePlayer The first player.
     * @param twoPlayer The second player.
     */
    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        this.playerOne = onePlayer;
        this.playerTwo = twoPlayer;
        gameRules.registerPlayers(onePlayer, twoPlayer);
    }

    /**
     * Starts a new game.
     */
    public void startNewGame() {
        // More game setup...
        gameRules.resetBoard();
        setCurrentPlayer(playerOne);
    }

    public Player getPlayerOne(){
        return playerOne;
    }

    public Player getPlayerTwo(){
        return playerTwo;
    }

    public void saveGame(final MancalaGame game, final String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(game);
        }
    }

    public MancalaGame loadGame(final String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (MancalaGame) ois.readObject();
        }
    }

    public void finalizeGame(final String gameType) {
        currentPlayer = getCurrentPlayer();
        final UserProfile userProfile = currentPlayer.getUserProfile();

        if ("Kalah".equals(gameType)) {
            userProfile.incrementKalahGamesPlayed();
            if (currentPlayer.getName().equals(getWinner().getName())) {
                userProfile.incrementKalahGamesWon();
            }
        } else if ("Ayo".equals(gameType)) {
            userProfile.incrementAyoGamesPlayed();
            if (currentPlayer.getName().equals(getWinner().getName())) {
                userProfile.incrementAyoGamesWon();
            }
        }
    }

    @Override
    public String toString() {
        // This method should return a string representation of the current game state
        return "Current player: " + currentPlayer.getName() + "\n" + gameRules.toString();
    }
}
