/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import javax.swing.JButton;

/**
 * Extended class of JButton with some extra member.
 *
 */
public class SButton extends JButton {

    private int y;
    private int x;

    /**
     * Constructor. sets the text of the button.
     *
     * @param string text of the button
     */
    SButton(String string) {
        super(string);
    }

    /**
     * This method sets the cartesian coordinate of the button.
     *
     * @param y vertical coordinate of the button
     * @param x horizontal coordinate of the button
     */
    void setCoordinate(int y, int x) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method returns the cartesian vertical coordinate of the button.
     *
     * @return y vertical coordinate of the button
     */
    int getVerticalCoordinate() {
        return y;
    }

    /**
     * This method returns the cartesian horizontal coordinate of the button.
     *
     * @return x Horizontal coordinate of the button
     */
    int getHorizontalCoordinate() {
        return x;
    }

    /**
     * This method checks adjacency of another button with it self
     *
     * @param a another button
     */
    boolean checkAdjacent(SButton a) {
        return (x == a.x && y == a.y - 155) || (x == a.x && y == a.y + 155) || (x == a.x - 155 && y == a.y) || (x == a.x + 155 && y == a.y);
    }
}
