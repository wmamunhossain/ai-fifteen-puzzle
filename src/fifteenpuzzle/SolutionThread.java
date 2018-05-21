/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hossain
 */
public class SolutionThread extends Thread {

    int waitTime = 1000;
    Thread subThread;
    GameFrame game;
    File board;
    FileWriter out;

    SolutionThread(GameFrame game) {
        subThread = new Thread(this, "solutionThread");
        this.game = game;
        start();
    }

    public void suggestSolution() throws IOException {

        File in = new File("next.txt");

        if (!in.exists()) {
            System.out.println("File not found");
            return;
        }

        Scanner cin = new Scanner(in);

        int r = cin.nextInt();
        int c = cin.nextInt();

        notifyUser(r, c);

        /**
         *
         * 0 = left 1 = right 2 = down 3 = up
         *
         */
    }

    public void notifyUser(int r, int c){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                SButton ss = game.button[i][j];
                int rr = (ss.getHorizontalCoordinate() - 4) / 155;
                int cc = (ss.getVerticalCoordinate() - 4) / 155;
                if (rr == r && cc == c) {
                    ss.setBackground(Color.ORANGE);
                    try {
                        sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    game.button[game.blankRow][game.blankColumn].setBackground(Color.gray);
                    ss.setBackground(Color.WHITE);
                    return;
                }
            }
        }
    }

    @Override
    public void run() {

        while (true) {
            
            if(game.isGameOver())continue;
            long lu = game.getLastUpdate();
            long cur = System.currentTimeMillis();

            if (cur - lu > waitTime) {
                try {
                    suggestSolution();
                } catch (IOException ex) {
                    Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (lu == game.getLastUpdate()) {
                    try {
                        sleep(waitTime-500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
