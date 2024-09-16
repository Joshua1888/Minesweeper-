package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MineTest {
    private Mine cell;

    @BeforeEach
    void setUP() {
        cell = new Mine();
    }


    @Test
    void encodeTest() {
        cell.encode(5, 4);
        int x = cell.getXPos();
        int y = cell.getYPos();
        assertEquals(1, x);
        assertEquals(1, y);
    }


    @Test
    void isBombTest() {
        cell.isBomb();
        boolean x = cell.getIsBomb();
        assertTrue(x);
    }


    @Test
    void isClickedTest() {
        cell.isClicked();
        boolean x = cell.getClicked();
        assertTrue(x);
    }

    @Test
    void bombAroundPlus1Test() {
        cell.bombAroundPlus1();
        int x = cell.getNumOfBombNear();
        assertEquals(1, x);
    }

    @Test
    void testForSetter() {
        boolean b = true;
        int num = 8;
        cell.setIsBomb(b);
        cell.setNumOfBombNear(num);
        cell.setClicked(b);
        cell.setXPos(8);
        cell.setYPos(8);

        assertTrue(cell.getClicked());
        assertTrue(cell.getIsBomb());
        assertEquals(8, cell.getXPos());
        assertEquals(8, cell.getYPos());




    }

}
