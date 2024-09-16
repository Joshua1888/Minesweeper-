package ui;

import model.EventLog;
import model.Event;
import model.Mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;


// Creating a game field initiated by GUI
public class Game extends GUI {
    private JFrame frame = new JFrame();
    private JToggleButton showScore = new JToggleButton();
    private int target;
    private File file;
    private File file2;
    private JWindow win;
    private JWindow los;

    // Effects set up the frame for GUI and add two image path to file and file2
    public Game() {
        super();
        file = new File("./data/boom.png");
        file2 = new File("./data/images.png");
        win = new JWindow();
        los = new JWindow();
    }

    // Effects Constract a game frame and add arbitrary cell into to the frame, as
    // well as add the score , save and quit button to the frame
    @Override
    protected void gameStarter() {
        setup();
        JPanel manueTop = new JPanel();
        manueTop.add(new JButton(new Save()));
        manueTop.add(showScore);
        manueTop.add(new JButton(new Quit()));
        frame.add(manueTop, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(row, col, 0, 0));
        ArrayList<Mine> listm = gameB.getListOfMine();
        for (Mine cell : listm) {
            JToggleButton b = cellToButton(cell);
            gamePanel.add(b);
        }
        frame.add(gamePanel, BorderLayout.CENTER);
        target = cellInTotal - numofBomb;
        updateStatus();
    }

    // Modifies this
    // Effects set up the frame for game board according to the size of cells
    private void setup() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(col * 80, row * 80);
        frame.setMaximumSize(new Dimension(3000, 3000));
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Minesweeper");
        showScore.setEnabled(false);
    }

    // Modifies this
    // Effect Convert each Mine to a Button which can display it's feature
    // when user click the bomb call loose to show the loose message
    private JToggleButton cellToButton(Mine m) {
        boolean isMine = m.getIsBomb();
        JToggleButton cell = new JToggleButton("");

        if (m.getClicked()) {
            cell.setEnabled(false);
            cell.setText(String.valueOf(m.getNumOfBombNear()));
        }
        cell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.isClicked();
                if (cell.isSelected()) {
                    cell.setEnabled(false); // Disable the button once it's selected
                }
                if (isMine) {
                    loose();
                } else {
                    cell.setText(String.valueOf(m.getNumOfBombNear()));
                }
                updateStatus();
            }
        });
        return cell;
    }

    // Modifies this
    // Effects Check game status everytime after use click one cell,
    // if user win the game call winG for win message
    public void updateStatus() {
        this.score = 0;
        this.numOfCellDetected = 0;
        for (ArrayList<Mine> col : gameBoard) {
            for (Mine cell : col) {
                Boolean click = cell.getClicked();
                Boolean bomb = cell.getIsBomb();
                int num = cell.getNumOfBombNear();
                if (!click) {
                    continue;
                } else if (bomb) {
                    legit = false;
                } else {
                    this.score += num;
                    gameB.setScore(num);
                    this.numOfCellDetected += 1;
                }
            }
        }
        showScore.setText(String.valueOf(score));
        if (target == numOfCellDetected) {
            winG();
        }
    }

    // Modifies this
    // Effect when user click the save button, it will save all fields into json
    // library
    protected class Save extends AbstractAction {
        Save() {
            super("SAVE");
        }

        // Effects Save the game information to the library and quit the game
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                saveto();
                jsonWriter.open();
                jsonWriter.write(gameB);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Saved, Thanks for using the Minesweeper Game app!",
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                report();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "Error, Unable to write to file.",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Effects Represents action to be taken when user wants to quit the game
     * promot user to save it before they quit.
     */
    private class Quit extends AbstractAction {
        Quit() {
            super("QUIT");
        }

        // Effects Promote user to save it before quit and give user option to cancel
        // this movement
        @Override
        public void actionPerformed(ActionEvent evt) {
            int result = JOptionPane.showConfirmDialog(null, "Do you want to save it before quit?", "Confirmation",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                Save s = new Save();
                s.actionPerformed(evt);
                JOptionPane.showMessageDialog(null,
                        "Thanks for using the Minesweeper Game app!",
                        "", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else if (result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null,
                        "Thanks for using the Minesweeper Game app!",
                        "", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                report();
            }
        }
    }

    // Timer cited from
    // https://stackoverflow.com/questions/21289140/how-can-i-make-image-appear-randomly-every-x-seconds-in-java-using-timer
    // Effects display an image when user click a boomb and quit the game after
    // wards
    private void loose() {
        try {
            Image img = ImageIO.read(file);
            los.setLayout(new BorderLayout());
            JLabel label = new JLabel(new ImageIcon(img));
            los.add(label, BorderLayout.CENTER);
            los.pack();
            los.setLocationRelativeTo(frame);
            los.setVisible(true);
            // Effect show the win message close the game
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    los.dispose();
                    JOptionPane.showMessageDialog(null, "You Lost! Try Better Next Time :)", "OPS",
                            JOptionPane.ERROR_MESSAGE);
                    frame.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
            report();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Timer cited from
    // https://stackoverflow.com/questions/21289140/how-can-i-make-image-appear-randomly-every-x-seconds-in-java-using-timer
    // Effects display an image when user win the game;

    private void winG() {
        try {
            Image img = ImageIO.read(file2);
            win.setLayout(new BorderLayout());
            JLabel label = new JLabel(new ImageIcon(img));
            win.add(label, BorderLayout.CENTER);
            win.pack();
            win.setLocationRelativeTo(frame);
            win.setVisible(true);
            // Effect show the loose message close the game
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    win.dispose();
                    JOptionPane.showMessageDialog(null,
                            "Wow! You Win!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
            report();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void report() {
        EventLog el = EventLog.getInstance();
        for (Event e : el) {
            System.out.println(e.toString());
        }
    }

}
