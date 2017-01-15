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

public class Simulator extends JPanel {
    private Generator generator;
    private int rows, cols;
    private boolean state;

    public Simulator(int rows, int cols, int seeds){
        // Generate map and agents
//        for (int i = 0; i < 10; i++)
        this.rows = rows;
        this.cols = cols;
        this.state = false;
        //this.setLayout(null);

        this.generator = new Generator(rows,cols,seeds);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setPreferredSize(new Dimension(rows,cols));

        final JButton startButton = new JButton("Start");
        //startButton.setLayout(null);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (state == false){
                    state = true;
                    startButton.setText("Stop");
                }
                else {
                    state = false;
                    startButton.setText("Start");
                }

            }
        });
        startButton.setPreferredSize(new Dimension(140, 40));
        add(startButton);
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