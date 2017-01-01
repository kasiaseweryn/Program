package Battle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Screen extends JPanel {

    private Graphics g;
    public Map mapa;
    public Village village;
    public Building base;
//    public ArrayList<Obstacle> obstacles;
    public Fleet fleet;
    public Vikings vikings;
    public Villagers villagers;

    public Screen(int rows, int cols, int seeds){
        // Generate map and agents
        mapa = new Map(rows,cols,seeds);
        village = new Village(mapa);
        base = new Building(800,800,40,40,0);
//        obstacles = new ArrayList<Obstacle>();
        fleet = new Fleet(mapa, village);
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