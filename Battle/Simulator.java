package Battle;

import javax.swing.*;
import java.util.Random;

public class Simulator {

    public static void main(String[] args) throws InterruptedException {
        //Size od map - based on this everything is generated
        final int size = 900;
        final int seeds = 300;

        final JFrame frame = new JFrame("Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen screen = new Screen(size, size, seeds);
        frame.setSize(size+30, size+50);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
        frame.add(screen);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();

        for (Boat i:screen.fleet.boats){
            Random r = new Random();
            i.target(screen.village.buildings.get(r.nextInt(screen.village.buildings.size())));
        }

        for (int i = 0; i < 2000; i++) {
            for(int j = 0; j < screen.fleet.boats.size(); j++){
                screen.fleet.boats.get(j).move(screen.mapa, screen.fleet.boats);
            }
            Thread.sleep(5);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        }
    }
}
