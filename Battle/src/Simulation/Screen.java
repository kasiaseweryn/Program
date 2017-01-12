package Simulation;

import javax.swing.*;
import java.awt.*;

import Army.*;
import Fleet.*;
import Map.*;
import Schemes.Colors;

import static java.lang.Math.toRadians;

public class Screen extends JPanel {

    private Graphics g;
    private Generator generator;

    public Screen(int rows, int cols, int seeds){
        // Generate map and agents
        generator = new Generator(rows,cols,seeds);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        generator.draw(g);
    }

    public Generator getGenerator() {
        return generator;
    }
}