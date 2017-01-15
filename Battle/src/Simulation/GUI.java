package Simulation;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * Created by anka on 15.01.17.
 */
public class GUI extends JFrame {
    Screen screen;

    public GUI (int rows, int cols, int seeds){
        super("Battle");
        super.frameInit();
        setLayout(null);

        screen = new Screen(rows, cols, seeds);
        setContentPane(screen);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
        setDefaultLookAndFeelDecorated(true);
        setSize(rows + 200, cols+22);

    }

    public void simulation(){
        screen.getGenerator().getFleet().setState(1);
            screen.getGenerator().getFleet().move();
            screen.getGenerator().getVikings().action();
    }

    public void GameTimer(int timeTime) {
        TimerTask task = new TimerTask() {
            public void run() {
                simulation();
            }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(task,0,timeTime);
    }


    public void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
            System.out.print("\n\nQuitting the application!\n");
            System.exit(0);
        }
    }
}



