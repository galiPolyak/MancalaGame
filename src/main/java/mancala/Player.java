package mancala;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private UserProfile userProfile;
    private Store store;

    public Player() {
        // Initialize with a default UserProfile
        userProfile = new UserProfile();
        store = new Store();
        store.setOwner(this); // Set the owner of the store to this player
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(final UserProfile profile) {
        userProfile = profile;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(final Store newStore) {
        this.store = newStore;
    }

    public int getStoreCount() {
        if (store != null) {
            return store.getStoneCount();
        } else {
            return 0;
        }
    }

    public String getName() {
        if (userProfile != null) {
            return userProfile.getName();
        } else {
            return "Unknown";
        }
    }

    public void setName(final String newName) {
        if (userProfile != null) {
            userProfile.setName(newName);
        }
    }

    @Override
    public String toString() {
        return "Player: " + getName();
    }
}
