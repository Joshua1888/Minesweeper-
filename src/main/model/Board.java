package model;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Consruct a game board class which can contains arbitrary 
// of Mine class in the listOfMine
// the gameBoardin2D which store the Mine cell in 2D array list
// the numberofMine, listOfBombs, numberOfRow and numberOfColumn.

public class Board implements Writable {
    private ArrayList<Mine> listOfMine;
    private ArrayList<ArrayList<Mine>> gameBoardin2D; 
    private int numberofMine;
    private ArrayList<Mine> listOfBombs;
    private static final Random RND = new Random();
    private int numberOfRow;
    private int numberOfColumn;

    // save for UI class
    private int score;
    private int numOfCellDetected;
    private int cellInTotal;
    private int numofBomb;
    private int row;
    private int col;
    private boolean isProgramRunning;
    private boolean legit;
    private boolean win;

    /*
     * REQUIRES: the given numberOfRow and numberOfColum must be positive integer
     * EFFECTS: Generat a listOfMine as a gameboard, each Mine in the
     *          list has it's original set up value.    
     */

    public Board(int numberOfRow, int numberOfColumn) {
        this.numberOfRow = numberOfRow;
        this.numberOfColumn = numberOfColumn;
        listOfMine = new ArrayList<Mine>();
        numberofMine = numberOfRow * numberOfColumn;
        int i = 0;
        gameBoardin2D = new ArrayList<ArrayList<Mine>>();
        listOfBombs = new ArrayList<Mine>();

        while (i < numberofMine) {
            Mine cell = new Mine();
            cell.encode(i, numberOfColumn);
            listOfMine.add(cell);
            i++;
        }
        EventLog.getInstance().logEvent(new Event("Added " + numberofMine + " cells into the board"));
    }


    /* 
     * Modifies: This.
     * Effects: Convert the all the mine in the list to a 2D arraylist by 
     * using two pointer(two for loop nesting together). 
    */
    public void boardIn2D() {
        for (int y = 0; y < numberOfRow; y++) {
            ArrayList<Mine> row = new ArrayList<Mine>();
            gameBoardin2D.add(row);
            for (int x = 0; x < numberOfColumn; x++) {
                int cell = deCode(x, y);
                Mine elemet = listOfMine.get(cell);
                row.add(elemet);
            }
        }
    }


    /* 
     * REQUIRES: the given integer must greater than 0 and less than numberOfMine
     * MODIFIES: this
     * EFFECTS: randomly generate the numbers of num from 0 to (numberofMine-1) 
     * and assign those mine as bomb. 
    */
    public void bombMaker(int numofBomb) {
        // listOfBombs = new ArrayList<Mine>();

        while (listOfBombs.size() < numofBomb) {
            int num = RND.nextInt(numberofMine);
            if (listOfBombs.contains(listOfMine.get(num))) {
                continue;
            } else {
                Mine m = listOfMine.get(num);   
                m.isBomb();
                EventLog.getInstance().logEvent(new Event("Set this cell as Bomb: " + m + " in the board"));
                listOfBombs.add(m);
            }
        }
    }

    /*
     * MODIFIES: this
     * 
     * EFFECTS Get each index of bombs by getXPos() and getYPos() from mine.
     *         generate the index of 8 mine cells surroudng the bomb in the arrylist of arrylistof integers
     *         (x-1, y-1) , (x, y-1), (x+1, y-1)
     *         (x-1, y) ,           , (x+1, y)
     *         (x-1, y+1) , (x, y+1), (x+1, y+1)
     *         generate the new value from -1 to 1 for y
     *         generate the new value from -1 to 1 for x
     *         get the coordinator by xPos +=  x;
                                      yPos +=  y;
     *         filter the original point when x and y are 0
     *         filter the point which is out of boudray when 
     *         (xPos < 0) || (yPos <0) || (xPos >= numberOfColumn) || (yPos >= numberOfRow . 
     *         
     */
    public void assignNumOfBombSurround() {
        for (Mine m : listOfBombs) {
            int xpos = m.getXPos();
            int ypos = m.getYPos();
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    if (x == 0 && y == 0) {
                        continue;
                    } else {
                        int xnew = xpos + x;
                        int ynew = ypos + y;
                        if ((xnew < 0) || (ynew < 0) || (xnew >= numberOfColumn) || (ynew >= numberOfRow)) {
                            continue;
                        } else {
                            Mine cell = gameBoardin2D.get(ynew).get(xnew);
                            cell.bombAroundPlus1();;
                        }
                    }
                }
            }
            EventLog.getInstance().logEvent(new Event("Detected one Bomb near by " 
                           +  "increase the number of bomb near by for all the cell in the board"));
        }
    }

    /*
     * Requrires the input x needed to be in the range of [0, (numberOfColunm - 1)]
     * Effects transfer the two demension coordinate back to one demensions by
     * value of x plus the value of y times the numberOfvolumn
     * 
     */

    public Mine getCell(int x, int y) {
        Mine cell = gameBoardin2D.get(y).get(x);
        return cell;
    }

    /*
     * Requrires the input x needed to be in the range of [0, (numberOfColunm - 1)]
     * Effects transfer the two demension coordinate back to one demensions by
     * value of x plus the value of y times the numberOfvolumn
     */
    public int deCode(int x, int y) {
        int num = x + y * numberOfColumn;
        return num;
    }

    public void setListOfBombs(Mine m) { 
        listOfBombs.add(m);
    }


    public ArrayList<Mine>  getListOfMine() { 
        return listOfMine;
    }

    public ArrayList<ArrayList<Mine>> getGameBoardin2D() { 
        return gameBoardin2D;
    }

    public int getNumberofMine() { 
        return numberofMine;
    }

    public ArrayList<Mine> getListOfBombs() { 
        return listOfBombs;
    }

    public int getNumberOfRow() { 
        return numberOfRow;
    }

    public int getNumberOfColumn() { 
        return numberOfColumn;
    }

    public void setNumberofMine(int num) { 
        numberofMine = num;
    }

    public void setNumberOfRow(int num) { 
        numberOfRow = num;
    }

    public void setNumberOfColumn(int num) { 
        numberOfColumn = num;
    }

    public void addListOfMine(Mine m) {
        listOfMine.add(m);
    }

    public void addListOfBombs(Mine m) {
        listOfBombs.add(m);
    }


    // For MineSweeper

    public int getScore() {
        return score;
    }

    public int getNumOfCellDetected() {
        return numOfCellDetected;
    }

    public int getCellInTotal() {
        return cellInTotal;
    }

    public int getNumofBomb() {
        return numofBomb;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getIsProgramRunning() {
        return isProgramRunning;
    }
    
    public boolean getLegit() {
        return legit;
    }

    public boolean getWin() {
        return win;
    }

    public void setScore(int num) {
        score = num;
        EventLog.getInstance().logEvent(new Event("Update the score"));
    }

    public void setNumOfCellDetected(int num) {
        numOfCellDetected = num;
    }

    public void setCellInTotal(int num) {
        cellInTotal = num;
    }

    public void setNumOfBomb(int num) {
        numofBomb = num;
    }

    public void setRow(int num) {
        row = num;
    }

    public void setCol(int num) {
        col = num;
    }

    public void setIsProgramRunning(boolean bol) {
        isProgramRunning = bol;
    }

    public void setLegit(boolean bol) {
        legit = bol;
    }
    
    public void setWin(boolean bol) {
        win = bol;
    }


    public void resetListOfBombs() {
        listOfBombs = new ArrayList<>();
    }

    public void resetListOfMine() {
        listOfMine = new ArrayList<>();
    }

     
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("numberofMine", numberofMine);
        json.put("numberOfRow", numberOfRow);
        json.put("numberOfColumn", numberOfColumn);
        json.put("listOfMine", listOfMineToJson());
        json.put("listOfBombs", listOfBombsToJson()); // 
        // json.put("gameBoardin2D", gameBoardToJson());

        json.put("score", score);
        json.put("numOfCellDetected", numOfCellDetected);
        json.put("cellInTotal", cellInTotal);
        json.put("numofBomb", numofBomb);
        json.put("row", row);
        json.put("col", col);
        json.put("isProgramRunning", isProgramRunning);
        json.put("legit", legit);
        json.put("win", win);
        return json;
    }


    


    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray listOfMineToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Mine cell : listOfMine) {
            jsonArray.put(cell.toJson());
        }
        
        return jsonArray;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray listOfBombsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Mine cell : listOfBombs) {
            jsonArray.put(cell.toJson());
        }

        return jsonArray;
    }

/* 
    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray gameBoardToJson() {
        JSONArray jsonArray = new JSONArray();

        for (ArrayList<Mine> row : gameBoardin2D) {
            JSONArray rowJsonArray = new JSONArray();
            for (Mine cell : row) {
                rowJsonArray.put(cell.toJson());
            }
            jsonArray.put(rowJsonArray);
        }
        
        return jsonArray;
    }
*/

}
