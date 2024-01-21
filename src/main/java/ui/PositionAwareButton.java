package ui;

import javax.swing.JButton;

/**
 * Represents a GUI button component that knows its position in a grid.
 */
public class PositionAwareButton extends JButton {
    private int xPos;
    private int yPos;

    /**
     * Constructs a new PositionAwareButton.
     */
    public PositionAwareButton() {
        super();
    }

    /**
     * Constructs a new PositionAwareButton with the specified text.
     *
     * @param text The text to display on the button.
     */
    public PositionAwareButton(String text) {
        super(text);
    }

    /**
     * Gets the horizontal position (across) of the button in a grid.
     *
     * @return The horizontal position of the button.
     */
    public int getAcross() {
        return xPos;
    }

    /**
     * Sets the horizontal position (across) of the button in a grid.
     *
     * @param x The horizontal position to set.
     */
    public void setAcross(int x) {
        this.xPos = x;
    }

    /**
     * Gets the vertical position (down) of the button in a grid.
     *
     * @return The vertical position of the button.
     */
    public int getDown() {
        return yPos;
    }

    /**
     * Sets the vertical position (down) of the button in a grid.
     *
     * @param y The vertical position to set.
     */
    public void setDown(int y) {
        this.yPos = y;
    }
}

