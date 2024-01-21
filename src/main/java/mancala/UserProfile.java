package mancala;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private int nKalahPlayed = 0;
    private int nAyoPlayed = 0;
    private int nKalahWon = 0;
    private int nAyoWon = 0;

    public UserProfile() {
    }

    // Getters and setters for all fields
    // Example:
    public String getName() {
        return userName;
    }

    public void setName(String name) {
        userName = name;
    }

    public int getNumberOfKalahGamesPlayed() {
        return nKalahPlayed;
    }

    public int getNumberOfAyoGamesPlayed() {
        return nAyoPlayed;
    }

    public int getNumberOfKalahGamesWon() {
        return nKalahWon;
    }

    public int getNumberOfAyoGamesWon() {
        return nAyoWon;
    }

    public void incrementKalahGamesPlayed() {
        this.nKalahPlayed++;
    }
    
    public void incrementAyoGamesPlayed() {
        this.nAyoPlayed++;
    }
    
    public void incrementKalahGamesWon() {
        this.nKalahWon++;
    }
    
    public void incrementAyoGamesWon() {
        this.nAyoWon++;
    }

    public static void saveProfile(UserProfile profile, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(profile);
        }
    }

    public static UserProfile loadProfile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (UserProfile) in.readObject();
        }
    }

    @Override
    public String toString() {
        return "UserProfile{" 
        +"name='" + userName + '\'' 
        +", numberOfKalahGamesPlayed=" + nKalahPlayed 
        +", numberOfAyoGamesPlayed=" + nAyoPlayed
        +", numberOfKalahGamesWon=" + nKalahWon 
        +", numberOfAyoGamesWon=" + nAyoWon 
        +'}';
    }
}
