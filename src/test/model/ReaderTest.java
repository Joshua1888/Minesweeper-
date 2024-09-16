package model;

import persistence.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Board br = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        
        try {
            JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json");
            Board wr = reader.read();
            assertEquals(0, wr.getListOfBombs().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
 
    @Test
    void testReaderGeneralWorkRoom() {
        try {
            Board br = new Board(5,5);
            int num = 8;
            JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
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
            assertEquals(2,br.getListOfBombs().size());
            assertEquals(27,br.getListOfMine().size());
            


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
        
}
