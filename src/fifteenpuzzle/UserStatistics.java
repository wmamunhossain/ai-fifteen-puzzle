/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * class for ranking distinct user data by sorting a array list. it also
 * represent the rank list by using JOptionOane.
 *
 */
public class UserStatistics {

    private File record;
    private Scanner in;
    private PrintWriter out;
    private ArrayList<Info> infoList;
    private Info temp;

    /**
     * Constructor
     *
     * initialize all the instance member.
     */
    UserStatistics() throws IOException {
        infoList = new ArrayList<Info>();
        record = new File("record.txt");
        record.setWritable(true);
        record.setReadable(true);
        if (!record.exists()) {
            record.createNewFile();
        }
    }

    /**
     * This method insert a single game's data in the rank list.
     *
     * @param name name of the usre
     * @param move move taken to solve
     * @param time time taken to solve
     */
    void insertInfo(String name, int move, long time) throws IOException {
        readData();
        infoList.add(new Info(name, move, time));
        Collections.sort(infoList, new MyIntComparable());
        //showHighestScore();
        writeData();
    }

    /**
     * This method show highest score in another Frame.
     *
     */
    void showHighestScore() {
        HighestScoreFrame HSFrame = new HighestScoreFrame();
        HSFrame.display(infoList);
    }

    /**
     * This method reads data from a text file.
     */
    public void readData() {
        infoList.clear();
        try {
            in = new Scanner(record);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        while (in.hasNext()) {
            String name = in.next();
            double penelty = in.nextDouble();
            int move = in.nextInt();
            long time = in.nextLong();
            infoList.add(new Info(name, move, time));
        }
    }

    /**
     * This method write data into a text file.
     *
     * @throws java.io.IOException
     */
    public void writeData() throws IOException {

        try {
            out = new PrintWriter(new FileWriter(record));
        } catch (IOException ex) {
            System.out.println(ex);
        }

        for (int i = 0; i < 10 && i < infoList.size(); i++) {
            temp = infoList.get(i);
            out.printf("%s %f %d %d\n", temp.name, temp.penelty, temp.move, temp.time);
        }
        infoList.clear();
        out.flush();
        out.close();
        record.setWritable(false);
    }

    /**
     * This method return a string representation of the class.
     *
     * @return ret String representation of the class
     */
    @Override
    public String toString() {
        String huge[] = new String[15];
        for (int i = 0; i < infoList.size() && i < 10; i++) {
            huge[i] = infoList.get(i).toString();
        }
        String ret = new String();
        for (int i = 0; i < infoList.size() && i < 10; i++) {
            ret = String.format("%s%d: %s", ret, i + 1, huge[i]);
        }
        return ret;
    }
}

/**
 * class for storing a single game information
 *
 * @author Mohammad Mamun
 */
class Info {

    String name;
    double penelty;
    long time;
    int move;

    /**
     * Constructor
     *
     * set only one single game information
     *
     * @param name name of the user
     * @param move move taken to solve
     * @param time time taken to solve
     * @author Mohammad Mamun
     */
    public Info(String name, int move, long time) {
        this.name = name;
        this.penelty = ((double) time * 50 + move * 50) / 100;
        this.time = time;
        this.move = move;
    }

    /**
     * This method return a string representation of the class.
     *
     * @return String representation of the class
     */
    @Override
    public String toString() {

        return String.format("%s   %.3f   %d   %d%d:%d%d\n", name, penelty, move, (time / 60) / 10, (time / 60) % 10, (time % 60) / 10, (time % 60) % 10);
    }
}

/**
 * implemented class of Comparator class.
 */
class MyIntComparable implements Comparator<Info> {

    /**
     * this method compare two object and calculate the highest priority at the
     * time to sort.
     *
     * @param o1 one Info Object
     * @param o2 another Info Object
     *
     */
    @Override
    public int compare(Info o1, Info o2) {
        return (o1.penelty > o2.penelty ? +1 : (o1 == o2 ? 0 : -1));
    }
}
