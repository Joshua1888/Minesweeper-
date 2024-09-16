package model;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    private Board gameB;
    
    @BeforeEach
    void runBefore() {
        gameB = new Board(3,3);
    }

    @Test
    void setupTest() {
        assertEquals(3,gameB.getNumberOfColumn());
        assertEquals(3,gameB.getNumberOfRow());
        ArrayList<Mine> list = gameB.getListOfBombs();
        ArrayList<Mine> list2 = gameB.getListOfMine();
        int num = list2.size();
        int num1 = list.size();
        assertEquals(0, num1);
        assertEquals(9, num);
        assertEquals(9, gameB.getNumberofMine());
    }

    @Test
    void boardIn2DTest() {
        gameB.boardIn2D();
        ArrayList<ArrayList<Mine>> board = gameB.getGameBoardin2D();
        int colsize = board.size();
        ArrayList<Mine> row1 = board.get(0);
        ArrayList<Mine> row2 = board.get(1);
        ArrayList<Mine> row3 = board.get(2);
        assertEquals(3, colsize);
        assertEquals(3, row1.size());
        assertEquals(3, row2.size());
        assertEquals(3, row3.size());
    }

    @Test
    void bombMakerTest() {
        gameB.bombMaker(5);
        ArrayList<Mine> list = gameB.getListOfBombs();
        int count = 0;
        for (Mine m : list) {
            if (m.getIsBomb()) {
                count += 1;
            }
        }
        assertEquals(5, count);
    }


    @Test
    void assignNumOfBombSurroundTest() {
        gameB.boardIn2D();
        ArrayList<ArrayList<Mine>> list = gameB.getGameBoardin2D();
        Mine m = list.get(1).get(1);
        m.isBomb();
        gameB.setListOfBombs(m);
        gameB.assignNumOfBombSurround();
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                if (x == 1 && y == 1) {
                    continue;
                } else {
                    Mine cell = list.get(y).get(x);
                    assertEquals(1, cell.getNumOfBombNear());
                }
            }
        }
    }


    @Test
    void assignNumOfBombSurroundTest2() {
        Board gameA = new Board(1, 3);
        gameA.boardIn2D();
        ArrayList<ArrayList<Mine>> list = gameA.getGameBoardin2D();
        Mine m = list.get(0).get(1);
        Mine m1 = list.get(0).get(0);
        Mine m3 = list.get(0).get(2);
        m1.isBomb();
        m3.isBomb();
        gameA.setListOfBombs(m1);
        gameA.setListOfBombs(m3);
        gameA.assignNumOfBombSurround();
        assertEquals(2, m.getNumOfBombNear());
    }

    @Test
    void getCellTest() {
        gameB.boardIn2D();
        Mine cell = gameB.getCell(1, 1);
        assertEquals(1, cell.getXPos());
        assertEquals(1, cell.getYPos());
    }

    @Test
    void testForPl() {
        int num = 10; 
        gameB.setScore(num);
        assertEquals(num,gameB.getScore());
        gameB.setNumOfCellDetected(num);
        assertEquals(num,gameB.getNumOfCellDetected());
        gameB.setCellInTotal(num);
        assertEquals(num,gameB.getCellInTotal());
        gameB.setNumOfBomb(num);
        assertEquals(num,gameB.getNumofBomb());
        gameB.setRow(num);
        assertEquals(num,gameB.getRow());
        gameB.setCol(num);
        assertEquals(num,gameB.getCol());

        boolean bol = true;
        gameB.setIsProgramRunning(bol);
        gameB.setLegit(bol);
        gameB.setWin(bol);
        assertTrue(gameB.getIsProgramRunning());
        assertTrue(gameB.getLegit());
        assertTrue(gameB.getWin());
    }


    @Test
    void testForSetter() {
        int num = 8;
        gameB.setNumberofMine(num);
        gameB.setNumberOfRow(num);
        gameB.setNumberOfColumn(num);
        assertEquals(num,gameB.getNumberofMine());
        assertEquals(num,gameB.getNumberOfRow());
        assertEquals(num,gameB.getNumberOfColumn());

        Mine m = new Mine();
        gameB.resetListOfBombs();
        assertEquals(0,gameB.getListOfBombs().size());
        gameB.resetListOfMine();
        assertEquals(0,gameB.getListOfMine().size());

        gameB.addListOfMine(m);
        gameB.addListOfMine(m);
        gameB.addListOfBombs(m);
        gameB.addListOfBombs(m);
        assertEquals(2,gameB.getListOfBombs().size());
        assertEquals(2,gameB.getListOfMine().size());
    }

    

}
