package Army;

import Map.Building;
import Map.Terrain;
import Map.Village;

import java.awt.*;
import java.util.ArrayList;

public class SquadVillagers {
    private ArrayList<Warrior> warriors;
    private Building target;
    private int state;  //0-all_dead, 1-fight, 2-retreat
    private int size;
    private Terrain map;
    private Village village;
    private Color color;

    public SquadVillagers(Terrain map, Village village, Building target, Point location, int size, Color color){
    }

    public void draw(Graphics g) {
        for (Warrior i:warriors) i.draw(g);
    }
}
