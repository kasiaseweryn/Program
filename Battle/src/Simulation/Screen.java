package Simulation;

import javax.swing.*;
import java.awt.*;

import Army.*;
import Fleet.*;
import Map.*;
import Schemes.Colors;

import static java.lang.Math.toRadians;

public class Screen extends JPanel {
    private Generator generator;
    private int rows, cols;

    public Screen(int rows, int cols, int seeds){
        // Generate map and agents
//        for (int i = 0; i < 10; i++)
        this.rows = rows;
        this.cols = cols;

        this.generator = new Generator(rows,cols,seeds);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setPreferredSize(new Dimension(rows,cols));
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        generator.draw(g);
        repaint();
    }

    public Generator getGenerator() {
        return generator;
    }


}