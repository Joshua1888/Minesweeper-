package model;

import org.json.JSONObject;

import persistence.Writable;

// the class of Mine is the class for every single cell on the game board.
// it has isBomb which shows it's a bomb or it's a regular cell
// the numOfBombNear shows how many bomb around
// the clicked shows whether it's hidden
// and the coordinate xPos and yPos

public class Mine implements Writable {
    private boolean isBomb; // determine whether it's a bomb or not.
    private int numOfBombNear; // determine how many bomb aroung it.
    private boolean clicked; // wheter is the mine has been clickered or not.
    private int xpos;
    private int ypos;

/*
 *  Requires index greater than and equal to 0, column greater than 0;
 * EFFECTS: Generates a piece of Mince and set it's original 
 * value as 0 and it's not a bomb.Based on scope of the board generate the x and y 
 * coordinates from its index in the list.
 */
    public Mine() {
        isBomb = false;
        clicked = false;
        numOfBombNear = 0;   
    }

    /*
     * Requires: index, column must be greater than 0.
     * Modifies: this
     * Effects: Conver the index of element into x and y positions.
     * the xPos is index modulo column
     * the yPos is the integer devision from index devides column
     */
    public void encode(int index, int column) {
        xpos = index % column;
        ypos = index / column;
    }

    /*
     * Modifies: This
     * Effects: change the status of isBomb to true.
     */
    public void isBomb() {
        isBomb = true;
    }

    /*
     * Modifies: This
     * Effects: change clicked status to true
     */

    public void isClicked() {
        clicked = true;
    }

    /*
     * Modifies: This
     * Effects: add 1 to the numOfBombNear
     */
    public void bombAroundPlus1() {
        numOfBombNear += 1;
    }

    public boolean getIsBomb() {
        return isBomb;
    }

    public int getNumOfBombNear() {
        return numOfBombNear;
    }

    public boolean getClicked() {
        return clicked;
    }

    public int getXPos() {
        return xpos;
    }

    public int getYPos() {
        return ypos;
    }

    public void setIsBomb(boolean b) {
        isBomb = b;
    }

    public void setNumOfBombNear(int num) {
        numOfBombNear = num;
    }

    public void setClicked(boolean b) {
        clicked = b;
    }

    public void setXPos(int x) {
        xpos = x;
    }

    public void setYPos(int y) {
        ypos = y;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("isBomb", isBomb);
        json.put("numOfBombNear", numOfBombNear);
        json.put("clicked", clicked);
        json.put("xpos", xpos);
        json.put("ypos", ypos);
        return json;
    }
}
