/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class makes a subthread to run the time display independently
 *
 */
public class TimeThread extends Thread {

    Thread subThread;
    long milliSecond;
    Date preDate;
    Date curDate;
    GameFrame game;
    boolean run;

    /**
     * initialize the thread and instance variable of the class.
     *
     */
    TimeThread(GameFrame game) {

        subThread = new Thread(this, "timeDisplay");
        this.game = game;
        run = false;
        start();
    }

    @Override
    /**
     * this method creates a loop for regularly Updating the time of the
     * stopwatch.
     *
     */
    public void run() {
        preDate = new Date();
        while (true) {
            if (!run) {
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TimeThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                curDate = new Date();
                game.updateTimeOfFrame(curDate.getTime() - preDate.getTime());
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TimeThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * this method reset the stopwatch.
     *
     */
    public void reset() {
        run = true;
        preDate = new Date();
    }
}
