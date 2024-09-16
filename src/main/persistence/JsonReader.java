package persistence;

import model.Board;
import model.Event;
import model.EventLog;
import model.Mine;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Board read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(new Event("Load the data to Game"));
        return parseBoard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
/* 
    // EFFECTS: parses Minesweeper from JSON object and returns it
    private Board parseMinesweeper(JSONObject jsonObject) {
        int score = Integer.parseInt(jsonObject.getString("score"));
        int numOfCellDetected = Integer.parseInt(jsonObject.getString("numOfCellDetected"));
        int cellInTotal = Integer.parseInt(jsonObject.getString("cellInTotal"));
        int numofBomb = Integer.parseInt(jsonObject.getString("numofBomb"));
        int row = Integer.parseInt(jsonObject.getString("row"));
        int col = Integer.parseInt(jsonObject.getString("col"));
        boolean isProgramRunning = Boolean.parseBoolean(jsonObject.getString("isProgramRunning"));
        JSONObject gb = jsonObject.getJSONObject("gameB");
        Board gameB = parseBoard(gb);
        ArrayList<ArrayList<Mine>> gameBoard = gameB.getGameBoardin2D();
        boolean legit = Boolean.parseBoolean(jsonObject.getString("legit"));
        boolean win = Boolean.parseBoolean(jsonObject.getString("win"));
        ms.setScore(score);
        ms.setNumOfCellDetected(numOfCellDetected);
        ms.setCellInTotal(cellInTotal);
        ms.setNumOfBomb(numofBomb);
        ms.setRow(row);
        ms.setCol(col);
        ms.setIsProgramRunning(isProgramRunning);
        ms.setGameB(gameB);
        ms.setGameBoard(gameBoard);
        ms.setLegit(legit);
        ms.setWin(win);
        return ms;
    }
*/
    // Modifies This
    // EFFECTS: parses Board from JSON object and returns it
    
    private Board parseBoard(JSONObject jsonObject) {
        int row = jsonObject.getInt("numberOfRow");
        int col = jsonObject.getInt("numberOfColumn");
        Board b = new Board(row, col);
        b.resetListOfBombs();
        b.resetListOfMine();
        parseCellsOfM(b, jsonObject.getJSONArray("listOfMine"));
        // System.out.println(b.getListOfMine().size());
        // System.out.println("hahah");
        parseCellsOfB(b, jsonObject.getJSONArray("listOfBombs"));

        b.boardIn2D();
        parseBoardStep2(b, jsonObject);

        return b;
    }

    // Modifies This
    // Effects get all the needed fileds for Minesweeper class in UI
    private void parseBoardStep2(Board b, JSONObject jsonObject) {
        int score = jsonObject.getInt("score");
        int numOfCellDetected = jsonObject.getInt("numOfCellDetected");
        int cellInTotal = jsonObject.getInt("cellInTotal");
        int numofBomb = jsonObject.getInt("numofBomb");
        int row = jsonObject.getInt("row");
        int col = jsonObject.getInt("col");
        boolean isProgramRunning = jsonObject.getBoolean("isProgramRunning");
        boolean legit = jsonObject.getBoolean("legit");
        boolean win = jsonObject.getBoolean("win");
        b.setScore(score);
        b.setNumOfCellDetected(numOfCellDetected);
        b.setCellInTotal(cellInTotal);
        b.setNumOfBomb(numofBomb);
        b.setRow(row);
        b.setCol(col);
        b.setIsProgramRunning(isProgramRunning);
        b.setLegit(legit);
        b.setWin(win);
    }



    // MODIFIES: b
    // EFFECTS: parses thingies from JSON object and adds them to Board
    private void parseCellsOfM(Board b, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            Mine cell = parseMine(nextThingy);
            b.addListOfMine(cell);
        }
    }

    // MODIFIES: b
    // EFFECTS: parses thingies from JSON object and adds them to Board
    private void parseCellsOfB(Board b, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            Mine cell = parseMine(nextThingy);
            b.addListOfBombs(cell);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private Mine parseMine(JSONObject jsonObject) {
        Mine cell = new Mine();
        boolean isBomb = jsonObject.getBoolean("isBomb"); 
        int numOfBombNear = jsonObject.getInt("numOfBombNear");
        boolean clicked = jsonObject.getBoolean("clicked");
        int xpos = jsonObject.getInt("xpos");
        int ypos = jsonObject.getInt("ypos");

        cell.setIsBomb(isBomb);
        cell.setNumOfBombNear(numOfBombNear);
        cell.setClicked(clicked);
        cell.setXPos(xpos);
        cell.setYPos(ypos);

        return cell;
    }
}
