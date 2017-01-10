package Simulation;

import javax.swing.*;

public class Simulator {

    public static void main(String[] args) throws InterruptedException {
        //Size od map - based on this everything is generated
        final int size = 1000;
        final int seeds = 900;

        final JFrame frame = new JFrame("Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen screen = new Screen(size, size, seeds);
        frame.setSize(size, size+25);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
        frame.add(screen);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();

        for (int i = 0; i < 300; i++) {
            for(int j = 0; j < screen.fleet.getBoats().size(); j++){
                screen.fleet.getBoats().get(j).move();
            }
            Thread.sleep(1000/size);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        }
    }
}
