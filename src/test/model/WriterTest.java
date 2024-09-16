package model;


import persistence.JsonReader;
import persistence.JsonWriter;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class WriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Board br = new Board(5,5);
            JsonWriter board = new JsonWriter("./data/my\0illegal:fileName.json");
            board.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    void testWriterEmptyWorkroom() {
        try {
            Board br = new Board(5,5);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            int num = 8;
            br.setScore(num);
            Mine m = new Mine();
            br.setNumOfCellDetected(num);
            br.setCellInTotal(num);
            br.setNumOfBomb(num);
            br.setRow(num);
            br.setCol(num);
            br.addListOfBombs(m);
            br.addListOfMine(m);
            br.setIsProgramRunning(true);
            br.setLegit(true);
            br.setWin(true);
            writer.open();
            writer.write(br);
            writer.close();
            testReadExtension();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    public void testReadExtension() throws IOException {
        JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
        Board br = new Board(5,5);
        int num = 8;
        br = reader.read();
        assertEquals(num,br.getNumOfCellDetected());
        assertEquals(num,br.getCellInTotal());
        assertEquals(num,br.getNumofBomb());
        assertEquals(num,br.getRow());
        assertEquals(num,br.getCol());
        assertEquals(num,br.getScore());
        assertTrue(br.getIsProgramRunning());
        assertTrue(br.getLegit());
        assertTrue(br.getWin());
        assertEquals(1,br.getListOfBombs().size());
        assertEquals(26,br.getListOfMine().size());
    }


    @Test
    void testWriterGeneralWorkroom() {
        try {
            Board br = new Board(3,3);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(br);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            br = reader.read();
            assertEquals(9, br.getNumberofMine());
            ArrayList<ArrayList<Mine>> thingies = br.getGameBoardin2D();
            assertEquals(3, thingies.size());
            assertEquals(1, thingies.get(1).get(2).getYPos());
            assertEquals(2, thingies.get(1).get(2).getXPos());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
