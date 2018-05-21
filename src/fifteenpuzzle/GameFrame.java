/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class Creates user interface.
 *
 */
public class GameFrame extends JFrame {

    public SButton[][] button;
    private SButton restart;
    private SButton highestScore;
    public int blankRow, blankColumn;
    private int actionCount;
    private final Font font = new Font("Monospaced", Font.PLAIN, 30);
    private String name;
    private SButton moveCounter;
    private SButton timeDisplay;
    private int[] pressed;
    private long secDur;
    private String timeString;
    private TimeThread timeThread;
    private SolutionThread solutionThread;
    private long lastUpdate;
    private File board;
    private FileWriter out;

    /**
     * Constructor.
     */
    public GameFrame() {
        super("Fifteen Puzzle");
        setLayout(null);
        setSize(1248, 655);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - 624, dim.height / 2 - 312 - 45);
        getContentPane().setBackground(Color.GRAY);
        addKeyListener(new HandlerClass());
        timeThread = new TimeThread((this));
        initialize();
    }

    /**
     * This method initialize all the instance variable.
     */
    private void initialize() {
        button = new SButton[4][4];
        HandlerClass handler = new HandlerClass();
        actionCount = 0;
        int t = 1;
        for (int i = 0, y = 4; i < 4; i++, y += 155) {
            for (int j = 0, x = 4; j < 4; j++, x += 155) {
                if (t != 16) {
                    setNumberedButton(i, j, x, y, t, handler);
                    t++;
                } else if (t == 16) {
                    setBlankButton(i, j, x, y, t, handler);
                }
            }
        }
        setRestartButton(handler);
        setHighestScoreButton(handler);
        setMoveCounter();
        setTimeDisplay();
        randomization();
        setVisible(true);
        setLastUpdate();
    }

    /**
     * Initializing the time display.
     */
    private void setTimeDisplay() {
        timeDisplay = new SButton("Time: 00:00");
        timeDisplay.setBounds(786, 427, 300, 100);
        timeDisplay.setBackground(Color.BLACK);
        timeDisplay.setFont(font);
        timeDisplay.setEnabled(false);
        add(timeDisplay);
    }

    /**
     * Initializing the move counter display.
     */
    private void setMoveCounter() {
        moveCounter = new SButton("Move: 0");
        moveCounter.setBounds(786, 97, 300, 100);
        moveCounter.setBackground(Color.BLACK);
        moveCounter.setFont(font);
        moveCounter.setEnabled(false);
        add(moveCounter);
    }

    /**
     * Initializing a numbered button.
     *
     * @param i row wise position of the button
     * @param j Column wise position of the button
     * @param x the horizontal coordinate where the button should placed
     * @param y the vertical coordinate where the button should placed
     * @param t counter of initialized button
     * @param handler action handler for the button
     */
    private void setNumberedButton(int i, int j, int x, int y, int t, HandlerClass handler) {
        button[i][j] = new SButton(String.format("%d", t++));
        button[i][j].setCoordinate(x, y);
        button[i][j].setBounds(x, y, 150, 150);
        button[i][j].addActionListener(handler);
        button[i][j].setFont(font);
        button[i][j].setBackground(Color.WHITE);
        button[i][j].setForeground(Color.BLACK);
        add(button[i][j]);
    }

    /**
     * Initializing a blank button.
     *
     * @param i row wise position of the button
     * @param j Column wise position of the button
     * @param x the horizontal coordinate where the button should placed
     * @param y the vertical coordinate where the button should placed
     * @param t counter of initialized button
     * @param handler action handler for the button
     */
    private void setBlankButton(int i, int j, int x, int y, int t, HandlerClass handler) {
        button[i][j] = new SButton("");
        blankRow = i;
        blankColumn = j;
        button[i][j].setCoordinate(x, y);
        button[i][j].setBounds(x, y, 150, 150);
        button[i][j].addActionListener(handler);
        button[i][j].setFont(font);
        button[i][j].setBackground(Color.LIGHT_GRAY);
        button[i][j].setEnabled(false);
        add(button[i][j]);
    }

    /**
     * Initializing the restart button.
     *
     * @param handler action handler for the button
     */
    private void setRestartButton(HandlerClass handler) {
        restart = new SButton("Restart");
        restart.setBounds(786, 207, 300, 100);
        restart.setBackground(Color.WHITE);
        restart.setForeground(Color.BLACK);
        restart.setFont(font);
        restart.addActionListener(handler);
        add(restart);
    }

    /**
     * Initializing the highest score button.
     *
     * @param handler action handler for the button
     */
    private void setHighestScoreButton(HandlerClass handler) {
        highestScore = new SButton("Highest Scores");
        highestScore.setBounds(786, 317, 300, 100);
        highestScore.setBackground(Color.WHITE);
        highestScore.setForeground(Color.BLACK);
        highestScore.setFont(font);
        highestScore.addActionListener(handler);
        add(highestScore);
    }

    /**
     * This method updates the move counter display at runtime.
     *
     * @param num number of action performed
     */
    private void updateCounterDisplay(int num) {
        moveCounter.setText(String.format("Move: %d", num));
    }

    /**
     * Randomizing the buttons.
     */
    private void randomization() {

        int tot = 0;
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int a = random.nextInt(4);
            int b = random.nextInt(4);
            if (button[a][b].checkAdjacent(button[blankRow][blankColumn])) {
                updateFrame(a, b);
                tot++;
            } else if (button[b][a].checkAdjacent(button[blankRow][blankColumn])) {
                updateFrame(b, a);
                tot++;
            }

            if (tot > 40) {
                break;
            }
        }

        createSolution();
        setLastUpdate();
        SolutionThread st = new SolutionThread(this);
    }

    /**
     * This method checks the game over situation
     *
     * @sturn true if the game is solved, false if not
     */
    public boolean isGameOver() {
        for (int i = 0, y = 4; i < 4; i++, y += 155) {
            for (int j = 0, x = 4; j < 4; j++, x += 155) {
                if (button[i][j].getVerticalCoordinate() == x && button[i][j].getHorizontalCoordinate() == y) {
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method updates the cartesian coordinate of clicked button. moves the
     * clicked button to the blank button's position and the blank button to the
     * clicked button's position by updating the cartesian coordinate of the
     * buttons.
     *
     * @param i row wise position of the clicked button in the array of button
     * @param j Column wise position of the clicked button in the array of
     * button
     */
    private void updateFrame(int i, int j) {
        int x, y;
        x = button[blankRow][blankColumn].getVerticalCoordinate();
        y = button[blankRow][blankColumn].getHorizontalCoordinate();
        button[blankRow][blankColumn].setBounds(button[i][j].getVerticalCoordinate(), button[i][j].getHorizontalCoordinate(), 150, 150);
        button[i][j].setBounds(button[blankRow][blankColumn].getVerticalCoordinate(), button[blankRow][blankColumn].getHorizontalCoordinate(), 150, 150);
        button[blankRow][blankColumn].setCoordinate(button[i][j].getVerticalCoordinate(), button[i][j].getHorizontalCoordinate());
        button[i][j].setCoordinate(x, y);
        add(button[blankRow][blankColumn]);
        add(button[i][j]);
    }

    public int[][] printCurrentBoard() {

        int output[][] = new int[4][4];
        for (int i = 0, y = 4; i < 4; i++, y += 155) {
            for (int j = 0, x = 4; j < 4; j++, x += 155) {

                SButton ss = button[i][j];
                int r = (ss.getHorizontalCoordinate() - 4) / 155;
                int c = (ss.getVerticalCoordinate() - 4) / 155;
//                System.out.print(" ("+r+","+c+")");
                output[r][c] = (ss.getText().equals("") ? 16 : Integer.parseInt(ss.getText()));
            }
//            System.out.println("");
        }

//        for(int i=0;i<4;++i){
//            for(int j=0;j<4;++j){
//                System.out.print(output[i][j]+" ");
//            }
//            System.out.println("");
//        }
        return output;

    }

    public void setLastUpdate() {
        lastUpdate = System.currentTimeMillis();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void createSolution() {
        board = new File("board.txt");

        try {
            out = new FileWriter(board);
        } catch (IOException ex) {
            Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        int[][] arr = printCurrentBoard();

        try {
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    out.write(String.format("%d ", arr[i][j]));
                }
                out.write("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        String path = "AI_Test.exe";
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("The file " + path + " does not exist");
        }
        try {
            Process p = Runtime.getRuntime().exec(file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method updates time display in runtime.
     *
     * takes passed time in millisecond and convert it to "minute:second" format
     * and updates the time display.
     *
     * @param milli time passed after the game starts in millisecond
     */
    public void updateTimeOfFrame(long milli) {
        secDur = (milli / 1000);
        long sec = secDur % 60;
        long min = secDur / 60;
        long fdigit = sec % 10;
        long sdigit = sec / 10;
        long tdigit = min % 10;
        long frtdigit = min / 10;
        timeString = new String(String.format("%d%d:%d%d", frtdigit, tdigit, sdigit, fdigit));
        timeDisplay.setText(String.format("Time: %d%d:%d%d", frtdigit, tdigit, sdigit, fdigit));
    }

    /**
     * The keyboard and mouse action handler class.
     */
    private class HandlerClass implements ActionListener, KeyListener {

        /**
         * Constructor.
         *
         * sets the keylistener focusable and the traversal key (like tab)
         * disabled.
         */
        public HandlerClass() {
            super();
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
        }

        /**
         * ignored.
         */
        @Override
        public void keyTyped(KeyEvent ke) {
        }

        /**
         * performs relevant work when a key pressed.
         *
         * takes the blank button down when up arrow key pressed, takes the
         * blank button up when down arrow key pressed, takes the blank button
         * right when left arrow key pressed, takes the blank button left when
         * right arrow key pressed by selecting the button and updating its
         * cartesian coordinate.
         *
         * @param ke KeyEvent relevant to the pressed key
         */
        @Override
        public void keyPressed(KeyEvent ke) {

            int blnkVer = button[blankRow][blankColumn].getVerticalCoordinate();
            int blnkHor = button[blankRow][blankColumn].getHorizontalCoordinate();

            int key = ke.getKeyCode();

            switch (key) {
                case KeyEvent.VK_UP:
                    pressed = pressedButtonThrougKeyBoard(blnkVer, blnkHor + 155);
                    break;
                case KeyEvent.VK_DOWN:
                    pressed = pressedButtonThrougKeyBoard(blnkVer, blnkHor - 155);
                    break;
                case KeyEvent.VK_RIGHT:
                    pressed = pressedButtonThrougKeyBoard(blnkVer - 155, blnkHor);
                    break;
                case KeyEvent.VK_LEFT:
                    pressed = pressedButtonThrougKeyBoard(blnkVer + 155, blnkHor);
                    break;
                default:
                //do nothing
            }
            if (pressed != null) {
                if (actionCount == 0) {
                    timeThread.reset();
                }
                updateCounterDisplay(++actionCount);
                updateFrame(pressed[0], pressed[1]);

                setLastUpdate();

                /**
                 *
                 * call for solution for current state.
                 *
                 */
            }

            if (isGameOver()) {
                timeThread.run = false;
                try {
                    getUserInfo();
                    timeDisplay.setText("Time: 00:00");
                    actionCount = 0;
                    updateCounterDisplay(0);
                } catch (Exception ex) {
                    Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                randomization();
            } else {
                createSolution();
            }

        }

        /**
         * ignored.
         */
        @Override
        public void keyReleased(KeyEvent ke) {
        }

        /**
         * searching the button which is located with specific vertical and
         * horizontal cartesian coordinate.
         *
         * @param v = vertical coordinate of the desired button
         * @param h = horizontal coordinate of the desired button
         * @return pos[] two int first one is the row wise position of the
         * desired button and second one is column wise position of the desired
         * button in the array of buttons.
         */
        private int[] pressedButtonThrougKeyBoard(int v, int h) {
            int[] pos = new int[2];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (button[i][j].getVerticalCoordinate() == v && button[i][j].getHorizontalCoordinate() == h) {
                        pos[0] = i;
                        pos[1] = j;
                        return pos;
                    }
                }
            }
            return null;
        }

        /**
         * performs relevant work when a button is pressed through mouse.
         *
         * takes the clicked button to the blank button's position and the blank
         * button to the clicked button's position by updating the cartesian
         * coordinate of the buttons. also takes action when the restart and
         * highest score buttons are clicked.
         *
         * @param ae ActionEvent relevant to pressed button through mouse
         */
        @Override
        public void actionPerformed(ActionEvent ae) {

            if (ae.getSource() == restart) {
                timeThread.run = false;
                timeDisplay.setText("Time: 00:00");
                randomization();
                actionCount = 0;
                updateCounterDisplay(actionCount);
            }

            if (ae.getSource() == highestScore) {
                UserStatistics temp = null;
                try {
                    temp = new UserStatistics();
                } catch (IOException ex) {
                }

                temp.readData();
                temp.showHighestScore();
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (ae.getSource() == button[i][j] && button[blankRow][blankColumn].checkAdjacent(button[i][j])) {
                        if (actionCount == 0) {
                            timeThread.reset();
                        }
                        updateCounterDisplay(++actionCount);
                        updateFrame(i, j);

                        setLastUpdate();

                        /**
                         *
                         * call for solution for current state.
                         *
                         */
                        if (isGameOver()) {
                            timeThread.run = false;
                            try {
                                getUserInfo();
                                timeDisplay.setText("Time: 00:00");
                                actionCount = 0;
                                moveCounter.setText("Move:0");
                            } catch (Exception ex) {
                                Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            randomization();
                        } else {
                            createSolution();
                        }
                    }
                }
            }
            requestFocus();
        }
    }
    

    /**
     * takes user information form user, shows the congratulation massage and
     * inserts the score to the highest scores list.
     *
     * @throws IOException text file input or output error
     * @see IOException
     */
    private void getUserInfo() throws IOException {
        name = JOptionPane.showInputDialog("Please enter your name\n");
        JOptionPane.showMessageDialog(null, "Congratulation MR. " + name + "\nYou have taken  " + actionCount + "   move\nAnd  " + timeString + "  time to solve.", "GMAE OVER", JOptionPane.PLAIN_MESSAGE);
        UserStatistics userStatistic = new UserStatistics();
        userStatistic.insertInfo(name, actionCount, secDur);
    }
}
