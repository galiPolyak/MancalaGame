package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mancala.AyoRules;
import mancala.Countable;
import mancala.KalahRules;
import mancala.MancalaGame;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;
import java.util.List;

public class MancalaGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private MancalaGame game;
    private JLabel statusLabel;
    private JLabel currPlayer;

    public MancalaGUI() {
        initializeGame();
        initializeGUI();
    }

    private void initializeGame() {
        
        String gameChoice = promptForGameChoice();

        game = new MancalaGame();

        if (gameChoice.equals("Mancala")) {
            game.setGameRules(new KalahRules()); // Assuming you have a class for Mancala rules
        } else {
            game.setGameRules(new AyoRules()); // Assuming you have a class for Ayo rules
        }
        
        game.startNewGame();
    }

    private String promptForGameChoice() {
        String[] options = {"Mancala", "Ayo"};
        int choice = JOptionPane.showOptionDialog(frame,
                                                  "Choose a game to play:",
                                                  "Game Selection",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE,
                                                  null,
                                                  options,
                                                  options[0]);
    
        if (choice == 0) {
            return "Mancala";
        } else {
            return "Ayo";
        }
    }

    private UserProfile promptForPlayerName(String title) {
        String playerName = JOptionPane.showInputDialog(frame, "Enter name for " 
                                                        + title 
                                                        + ":", title, JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = title; // Default name if no input is provided or cancelled
        }
        UserProfile profile = new UserProfile();
        profile.setName(playerName);

        return profile;
    }


    private void initializeGUI() {
        frame = new JFrame("Mancala Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    
        statusLabel = new JLabel("Welcome to Mancala!");
        currPlayer = new JLabel("Setting up players...");
        frame.add(statusLabel, BorderLayout.NORTH);
    
        // Create a panel for the Mancala board with a specific layout
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridBagLayout()); // 2 rows and 8 columns to include the stores

        // Add pits and stores to the board
        addPitButtons();
        frame.add(boardPanel, BorderLayout.CENTER);

        UserProfile player1Profile = promptForPlayerName("Player 1");
        UserProfile player2Profile = promptForPlayerName("Player 2");

        Player player1 = new Player();
        player1.setUserProfile(player1Profile);

        Player player2 = new Player();
        player2.setUserProfile(player2Profile);

        game.setPlayers(player1, player2);
        game.startNewGame();

        game.setPlayers(player1, player2);
    
        // Create and set players with the user inputted names
        game.getPlayerOne().setUserProfile(player1Profile);
        game.getPlayerTwo().setUserProfile(player2Profile);

    
        currPlayer = new JLabel("Current Player: " + player1.getName() + ",  (Pits 1-6)");
        frame.add(currPlayer, BorderLayout.SOUTH);

        // Create a menu bar with options
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
    
        // Set frame size and make it visible
        frame.setSize(800, 400); // You can adjust the size as needed
        frame.setVisible(true);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
    
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> startNewGame());

        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener(e -> {
            saveGame(game, "savedGame.ser"); // 'ser' is a common extension for serialized objects
            JOptionPane.showMessageDialog(frame, "Game saved successfully.", 
                                            "Save Game", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener(e -> {
            loadTheGameItem();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        JMenuItem saveProfileItem = new JMenuItem("Save Profiles");
        saveProfileItem.addActionListener(e -> {
            saveTheProfileItem();
        });

        fileMenu.add(saveProfileItem);

        JMenuItem loadProfileItem = new JMenuItem("Load Profile");
        loadProfileItem.addActionListener(e -> {
            loadTheProfileItem();
        });

        fileMenu.add(loadProfileItem);
        fileMenu.add(loadProfileItem);
        fileMenu.add(newGameItem);
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    private void loadTheGameItem() {
        JFileChooser fileChooser = new JFileChooser("./assets");
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            MancalaGame loadedGame = loadGame(fileToLoad.getAbsolutePath());
            if (loadedGame != null) {
                game = loadedGame;
                updateBoard(); // Update the GUI based on the loaded game state
                JOptionPane.showMessageDialog(frame, "Game loaded successfully.", 
                                            "Load Game", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to load game.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
    }

    private void saveTheProfileItem(){
        UserProfile profileToSave1 = game.getPlayerOne().getUserProfile();
        UserProfile profileToSave2 = game.getPlayerTwo().getUserProfile();
        if (profileToSave1 != null) {
            saveUserProfile(profileToSave1, game.getPlayerOne().getName() 
            + "Profile.ser"); // You can modify the filename as needed
        } 
        if (profileToSave2 != null) {
            saveUserProfile(profileToSave2, game.getPlayerTwo().getName() 
            + "Profile.ser"); // You can modify the filename as needed
        } else {
            JOptionPane.showMessageDialog(frame, "No user profile to save.", 
                                        "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadTheProfileItem(){
        UserProfile loadedProfile = loadUserProfile();
        if (loadedProfile != null) {
            assignProfileToPlayer(loadedProfile);
        } else {
            JOptionPane.showMessageDialog(frame, "Profile loading cancelled or failed.", 
                                        "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void assignProfileToPlayer(UserProfile loadedProfile) {
    Object[] options = {"Player 1", "Player 2"};
    int choice = JOptionPane.showOptionDialog(frame, 
                                              "Assign profile to which player?", 
                                              "Choose Player", 
                                              JOptionPane.YES_NO_OPTION, 
                                              JOptionPane.QUESTION_MESSAGE, 
                                              null, options, options[0]);

    if (choice == JOptionPane.YES_OPTION) { // Player 1
        game.getPlayerOne().setUserProfile(loadedProfile);
    } else if (choice == JOptionPane.NO_OPTION) { // Player 2
        game.getPlayerTwo().setUserProfile(loadedProfile);
    }

    updateBoard(); // Update the board to reflect the new profile
}
    

    private void addPitButtons() {
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH; // Fill the space
        c.weightx = 1.0; // Give extra horizontal space
        c.weighty = 1.0; // Give extra vertical space


        // Add top row pits (pits 7 to 12)
        // Add top row pits (pits 12 to 7) from right to left
        for (int i = 12; i >= 7; i--) {
            c.gridx = 12 - i + 1;
            c.gridy = 0;
            PositionAwareButton pitButton = createPitButton(i);
            boardPanel.add(pitButton, c);
        }

        // Player 1's store (index 6)
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2; // Span two rows vertically
        PositionAwareButton storeButton2 = createStoreButton(2);
        boardPanel.add(storeButton2, c);
    
        // Player 2's store (index 13)
        c.gridx = 7;
        c.gridy = 0;
        c.gridheight = 2; // Span two rows vertically
        PositionAwareButton storeButton1 = createStoreButton(1);
        boardPanel.add(storeButton1, c);

    
        // Add bottom row pits (pits 1 to 6)
        for (int i = 1; i <= 6; i++) {
            c.gridx = i;
            c.gridy = 1;
            c.gridheight = 1; // Reset to default height
            PositionAwareButton pitButton = createPitButton(i);
            boardPanel.add(pitButton, c);
        }

        updateBoard();

    }

    private PositionAwareButton createPitButton(int pitNumber) {
        PositionAwareButton pitButton = new PositionAwareButton("Pit " + pitNumber);
        pitButton.setAcross(pitNumber % 6);  // Assuming these are for positioning
        pitButton.setDown(pitNumber / 6);    // Assuming these are for positioning
        pitButton.setActionCommand(String.valueOf(pitNumber)); // Store the pit number
        pitButton.addActionListener(e -> pitButtonClicked(pitButton));

         // Set button size and font
        pitButton.setMinimumSize(new Dimension(100, 100));

        return pitButton;
    }

    private PositionAwareButton createStoreButton(int playerNum) {
        PositionAwareButton storeButton = new PositionAwareButton("Store " + playerNum);

        storeButton.setMinimumSize(new Dimension(120, 200));
        return storeButton;
    }


    private void pitButtonClicked(PositionAwareButton button) {
        int pitIndex = Integer.parseInt(button.getActionCommand()); // Get the pit number

        try {
            game.move(pitIndex);

            updateBoard();
            checkGameOver();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNewGame() {
        game.startNewGame();
        updateBoard();
        statusLabel.setText("New game started!");
    }

    private void updateBoard() {
        // Retrieve the data from the game's data structure
        List<Countable> data = game.getBoard().getGameBoard().getData();
    
        // Update pits 12 to 7 (top row)
        for (int i = 12; i >= 7; i--) {
            JButton button = (JButton) boardPanel.getComponent(12 - i); // Components 0 to 5 in GridLayout
            button.setText("<html>Pit " + i + ":<br/>" + data.get(i).getStoneCount());
        }
    
        // Update Player 2's store (index 13)
        JButton storeButton2 = (JButton) boardPanel.getComponent(6); // Component 6 in GridLayout
        storeButton2.setText("Store2: " + data.get(13).getStoneCount());

        // Update pits 1 to 6 (bottom row)
        for (int i = 1; i <= 6; i++) {
            JButton button = (JButton) boardPanel.getComponent(i + 7); // Components 8 to 13 in GridLayout
            button.setText("<html>Pit " + i + ":<br/>"  + data.get(i - 1).getStoneCount());
        }
    
        // Update Player 1's store (index 6)
        JButton storeButton1 = (JButton) boardPanel.getComponent(7); // Component 7 in GridLayout
        storeButton1.setText("Store1: " + data.get(6).getStoneCount());

        // Update the status label to display the current player
        Player currentPlayer = game.getCurrentPlayer();
        int playerNum = game.getBoard().getPlayerNum();
        if (currentPlayer != null) {
            if (playerNum == 1){
                 currPlayer.setText("Current Player: " + currentPlayer.getName() + ", (Pits 1-6)");
            }else{
                currPlayer.setText("Current Player: " + currentPlayer.getName() + ", (Pits 7-12)");
            }
        } 
    }

    private void checkGameOver() {
        if (game.isGameOver()) {
            Player winner = game.getWinner();
            String message = (winner != null) ? "Winner: " + winner.getName() : "Game is a tie!";
            statusLabel.setText(message);

            JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveUserProfile(UserProfile profile, String filename) {
        // The path to the assets directory
        String assetsPath = "./assets/";
    
        // Ensure the assets directory exists
        File assetsDir = new File(assetsPath);
        if (!assetsDir.exists()) {
            assetsDir.mkdirs(); // Create the directory if it doesn't exist
        }
    
        try {
            String fullPath = assetsPath + filename;
            System.out.println("Saving profile to: " + fullPath); // Debugging line
            Saver.saveObject(profile, fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving profile: " 
                                        + filename, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private UserProfile loadUserProfile() {
        JFileChooser fileChooser = new JFileChooser("./assets");
        fileChooser.setDialogTitle("Load User Profile");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
        int userSelection = fileChooser.showOpenDialog(frame);
    
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                // Load the user profile from the selected file
                return Saver.loadObject(fileToLoad.getAbsolutePath());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading profile from file: " 
                                            + fileToLoad.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    public void saveGame(MancalaGame mGame, String filename) {
        String filePath = "./assets/" + filename;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(mGame);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving game: " 
                                        + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public MancalaGame loadGame(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (MancalaGame) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading game: " 
                                        + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MancalaGUI::new);
    }

}
