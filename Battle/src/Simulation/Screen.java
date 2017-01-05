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
        base = new Building(new Point((int)(rows*0.9), (int)(cols*0.9)),rows/20,cols/20,0);
//        obstacles = new ArrayList<Obstacle>();
        fleet = new Fleet(mapa, village);
        vikings = new Vikings(mapa, village, fleet, base);
        villagers = new Villagers(mapa ,village, village.getCenter());
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        mapa.draw(g);
        village.draw(g);
        base.draw(g);
//        obstacles.draw(g);
        fleet.draw(g);
//        vikings.draw(g);
//        villagers.draw(g);
    }
}