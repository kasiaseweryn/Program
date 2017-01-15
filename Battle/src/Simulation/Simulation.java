package Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Army.*;
import Fleet.*;
import Map.*;
import Schemes.Colors;

import static java.lang.Math.toRadians;

public class Simulation extends JPanel {
    private Generator generator;
    private int rows, cols;
    private boolean state;

    public Simulation(int rows, int cols, int seeds){
        // Generate map and agents
//        for (int i = 0; i < 10; i++)
        this.rows = rows;
        this.cols = cols;
        this.state = false;

        this.generator = new Generator(rows,cols,seeds);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setPreferredSize(new Dimension(rows,cols));

        JButton startButton = new JButton("Start");
        //startButton.setLayout(null);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state = true;
            }
        });
        startButton.setPreferredSize(new Dimension(140, 40));
        add(startButton);

        JButton stopButton = new JButton("Stop");
        //startButton.setLayout(null);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state = false;
            }
        });
        stopButton.setPreferredSize(new Dimension(140, 40));
        add(stopButton);
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

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}