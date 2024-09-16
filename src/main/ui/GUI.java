package ui;

import model.Mine;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.Board;
import model.EventLog;
import model.Event;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;


// UI class which calls all the method from model and persistence
// it also prints the lines and graphics.
public abstract class GUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final File file = new File("./data/gamestart.png");
    public static final File file2 = new File("./data/back2.png");

    protected static final String JSON_STORE = "./data/gameBoard.json";
    protected int score;
    protected int numOfCellDetected;
    protected int cellInTotal;
    protected int numofBomb;
    protected int row;
    protected int col;
    protected boolean isProgramRunning;
    protected ArrayList<ArrayList<Mine>> gameBoard;
    protected Board gameB;
    protected boolean legit;
    protected boolean win;
    protected JsonWriter jsonWriter;
    protected JsonReader jsonReader;
    protected Scanner scanner;
    protected Boolean save;

    // Effects set up the frame
    public GUI() {
        super("Mine Sweeper");
        initializeGraphics();
        init();
    }

    // MODIFIES: this
    // EFFECTS: draws the JFrame window where this DrawingEditor will operate, and
    // populates the tools to be used
    // to manipulate this drawing
    // set background from https://stackoverflow.com/questions/22162398/how-to-set-a-background-picture-in-jpanel.
    protected void initializeGraphics() {
        setLayout(new BorderLayout());
        // setMinimumSize(new Dimension(WIDTH, WIDTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        
        try {
            Image img = ImageIO.read(file2);
            JLabel l = new JLabel(new ImageIcon(img));
            setContentPane(l);
            setLayout(new BorderLayout());
            addButtonPanel();
            l.revalidate();
            l.repaint();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the application with the starting values
    protected void init() {
        score = 0;
        row = 0;
        col = 0;
        legit = true;
        numOfCellDetected = 0;
        numofBomb = 0;
        cellInTotal = 0;
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
        gameBoard = new ArrayList<ArrayList<Mine>>();
        win = false;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // Modifies this
    // Effect: Helper to add control buttons.
    // sturcture from AlarmClock
    private void addButtonPanel() {
        JPanel bp = new JPanel(new GridLayout(3, 2, 5, 5));
        bp.setOpaque(false);
        JButton s = new JButton(new Start());
        s.setPreferredSize(new Dimension(150, 80));
        JButton l = new JButton(new Load());
        s.setPreferredSize(new Dimension(150, 80));
        JButton r = new JButton(new Row());
        s.setPreferredSize(new Dimension(150, 80));
        JButton c = new JButton(new Column());
        s.setPreferredSize(new Dimension(150, 80));
        JButton q = new JButton(new Quit());
        s.setPreferredSize(new Dimension(150, 80));
        JButton b = new JButton(new SetUpB());
        s.setPreferredSize(new Dimension(150, 80));
        bp.add(s);
        bp.add(l);
        bp.add(r);
        bp.add(c);
        bp.add(q);
        bp.add(b);
        add(bp, BorderLayout.PAGE_END);
    }

    /**
     * Effects Represents action to be taken when user wants to start the game.
     * // sturcture from AlarmClock
     */
    private class Start extends AbstractAction {
        Start() {
            super("Start The Game");
        }

        // Effects if user hasnot set up the col, row and number of bomb
        // show the invalid message
        // else set up the GameB initiate the gameboard
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (col == 0 || row == 0 || numofBomb == 0) {
                JOptionPane.showMessageDialog(null,
                        "Please Enter the Value For Row, Column and Bomb",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else {
                GUI.this.dispose();
                cellInTotal = row * col;
                System.out.println(cellInTotal);
                displayVisual();
            }
        }
    }

    // Effect display the start game image before initiate the game board.
    private void displayVisual() {
        try {
            Image img = ImageIO.read(file);
            JWindow win = new JWindow();
            win.setLayout(new BorderLayout());
            JLabel label = new JLabel(new ImageIcon(img));
            win.add(label, BorderLayout.CENTER);
            win.pack();
            win.setLocationRelativeTo(this);
            win.setVisible(true);
            Timer timer = new Timer(1000, new ActionListener() {
                // Effects close the image and start the game
                @Override
                public void actionPerformed(ActionEvent e) {
                    win.dispose();
                    gameStarter();
                }
            });
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Effects Represents action to be taken when user wants to add Bomb to game
     * board
     * // sturcture from AlarmClock
     */
    private class SetUpB extends AbstractAction {
        SetUpB() {
            super("Set Up Number Of Bomb");
        }

        // Effects set up the number of bomb and return errors when user want to input
        // the invalid input
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (col == 0 || row == 0) {
                JOptionPane.showMessageDialog(null, "Please assign value to row and column first", "System Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = JOptionPane.showInputDialog(
                    null, "Enter a number:", "Input", JOptionPane.PLAIN_MESSAGE);
            if (input != null) {
                try {
                    int num = Integer.parseInt(input);
                    int limit = row * col;
                    generatGameB();
                    if (num > 0 && num < limit) {
                        extension(num);
                        JOptionPane.showMessageDialog(null, "SuccessLoaded", "SUCCESS",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Input", "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid Input", "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Effects extend to SaveB
    // generate the gameboard
    private void generatGameB() {
        gameB = new Board(row, col);
        gameB.boardIn2D();
        gameBoard = gameB.getGameBoardin2D();
    }

    // Effects exten to SaveB
    // generate the Bomb 
    private void extension(int num) {
        GUI.this.numofBomb = num;
        gameB.bombMaker(numofBomb);
        gameB.assignNumOfBombSurround();
    }

    /**
     * Effects Represents action to be taken when user wants to load the game
     * from jsonlibrary.
     * // sturcture from AlarmClock
     */
    private class Load extends AbstractAction {
        Load() {
            super("Load the Saved Game");
        }

        // Effects load the game from library and show the message
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                GUI.this.dispose();
                GUI.this.gameB = jsonReader.read();
                loadTo();
                displayVisual();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Unable to read from file",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Effects Represents action to be taken when user wants to quit the game
     * // sturcture from AlarmClock
     */
    private class Quit extends AbstractAction {
        Quit() {
            super("QUIT");
        }

        // Quit the game and print the play again message
        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null,
                    "Thanks for using the Minesweeper Game app!",
                    "", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            report();
        }
    }

    /**
     * Effects Represents action to be taken when user wants to quit the game
     * // sturcture from AlarmClock
     */
    private class Row extends AbstractAction {
        Row() {
            super("Add a Row");
        }

        // Effects Add number of row to game board and return invalid massage when
        // having invalid input
        @Override
        public void actionPerformed(ActionEvent evt) {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter a number:", "Input",
                    JOptionPane.PLAIN_MESSAGE);
            if (input != null) {
                try {
                    int num = Integer.parseInt(input);

                    if (num > 0) {
                        GUI.this.row = num;
                        JOptionPane.showMessageDialog(null,
                                "Success Loaded",
                                "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Input",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid Input",
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Effects Represents action to be taken when user wants to add Column to game
     * board
     * // sturcture from AlarmClock
     */
    private class Column extends AbstractAction {
        Column() {
            super("Add a Column");
        }

        // Add number of column to game board and return invalid massage when having
        // invalid input
        @Override
        public void actionPerformed(ActionEvent evt) {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter a number:", "Input",
                    JOptionPane.PLAIN_MESSAGE);
            if (input != null) {
                try {
                    int num = Integer.parseInt(input);
                    if (num > 0) {
                        GUI.this.col = num;
                        JOptionPane.showMessageDialog(null,
                                "Success Loaded",
                                "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Input",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid Input",
                            "System Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }

    protected abstract void gameStarter();

    // Modifies This.
    // Effects Save all the fields to game board.
    protected void saveto() {
        gameB.setScore(score);
        gameB.setNumOfCellDetected(numOfCellDetected);
        gameB.setCellInTotal(cellInTotal);
        gameB.setNumOfBomb(numofBomb);
        gameB.setRow(row);
        gameB.setCol(col);
        gameB.setIsProgramRunning(isProgramRunning);
        gameB.setLegit(legit);
        gameB.setWin(win);
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadTo() {
        this.score = gameB.getScore();
        this.numOfCellDetected = gameB.getNumOfCellDetected();
        this.cellInTotal = gameB.getCellInTotal();
        this.numofBomb = gameB.getNumofBomb();
        this.row = gameB.getRow();
        this.col = gameB.getCol();
        this.isProgramRunning = gameB.getIsProgramRunning();
        this.gameBoard = gameB.getGameBoardin2D();
        this.legit = gameB.getLegit();
        this.win = gameB.getWin();
    }

    // Effect print all the event log to terminal
    private void report() {
        EventLog el = EventLog.getInstance();
        for (Event e : el) {
            System.out.println(e.toString());
        }
    }

}
