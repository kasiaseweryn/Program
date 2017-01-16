package Simulation;

import javax.swing.*;
import java.awt.*;

public class Simulator extends JPanel {
    JPanel simulator;

    private Generator generator;
    private Stats stats;
    private int rows, cols;
    private boolean state;

    public Simulator(int rows, int cols, int seeds){
        this.rows = rows;
        this.cols = cols;
        this.state = false;
        //this.setPreferredSize(new Dimension(1200,1022));
        this.simulator = new JPanel();
        this.generator = new Generator(rows,cols,seeds);
        this.stats = new Stats(generator);
    }

    public void simulation(){
        if (state) {
            generator.getFleet().setState(1);
            generator.getFleet().move();
            generator.getVikings().action();

            //!!!!!!!!!!HERE PUT WHAT YOU WANT TO SIMULATE!!!!!!!!!!!!!!//

            stats.estimate();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        generator.draw(g);
        stats.draw(g);
        repaint();
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}