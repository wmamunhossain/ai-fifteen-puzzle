/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifteenpuzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This class show highest score in another JFrame.
 *
 */
public class HighestScoreFrame extends JFrame {

    private final Font font = new Font("Monospaced", Font.PLAIN, 18);
    private JButton ok = new JButton("OK");

    /**
     * Constructor.
     */
    HighestScoreFrame() {

        super("Highest Scores");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setSize(450, 400);
        setResizable(false);
        setAlwaysOnTop(true);
        getContentPane().setBackground(Color.WHITE);
        setLocation(dim.width / 2 - 225, dim.height / 2 - 200);
    }

    /**
     * This method show highest scores in a JFrame.
     *
     * @param listInfo List of the scores
     */
    public void display(ArrayList<Info> listInfo) {

        setOKButton();
        setBar();
        for (int i = 0, a = 40; i < 10 && i < listInfo.size(); i++, a += 20) {
            Info t = listInfo.get(i);
            String s = String.format("%2d: %8s  %3.2f   %02d:%02d  %3d", i + 1, t.name, t.penelty, t.time / 60, t.time % 60, t.move);
            JLabel temp = new JLabel(s);
            temp.setBounds(40, a, 1000, 50);
            temp.setFont(font);
            add(temp);
        }
        setVisible(true);
    }

    /**
     * This method sets the OK button.
     *
     */
    private void setOKButton() {

        ok = new JButton("OK");
        ok.addActionListener(new OKHandler());
        ok.setFont(font);
        ok.setBounds(195, 280, 60, 50);
        add(ok);
    }

    /**
     * This method sets dialogue messages.
     *
     */
    private void setBar() {
        JLabel bar = new JLabel("Rank   Name  Penelty  Time   Move");
        bar.setBounds(40, 20, 1000, 50);
        bar.setFont(font);
        bar.setForeground(Color.RED);
        add(bar);
    }

    /**
     * performs relevant work when OK button is pressed through mouse.
     *
     * Disposing The Highest Scores Frame.
     *
     * @param ae ActionEvent relevant to pressed button through mouse
     */
    class OKHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == ok) {
                dispose();
            }
        }
    }
}
