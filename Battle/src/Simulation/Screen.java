package Simulation;

import javax.swing.*;
import java.awt.*;

import Army.*;
import Fleet.*;
import Map.*;

public class Screen extends JPanel {

    private Graphics g;
    public Terrain mapa;
    public Village village;
    public Building base;
//    public ArrayList<Obstacle> obstacles;
    public Fleet fleet;
    public Vikings vikings;
    public Villagers villagers;

    public Screen(int rows, int cols, int seeds){
        // Generate map and agents
        mapa = new Terrain(rows,cols,seeds);
        village = new Village(mapa);
        base = new Building(new Point(850,850),40,40,0);
//        obstacles = new ArrayList<Obstacle>();
        fleet = new Fleet(mapa, village);
//        fleet.setTargetLocation(mapa, village);
//        vikings = new Vikings(???);
//        villagers = new Villagers(???);
        setIgnoreRepaint(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        mapa.draw(g);
        village.draw(g);
        base.draw(g);
        fleet.draw(g);
//        vikings.draw(g);
//        villagers.draw(g);
    }
}