package Army;

import Fleet.Fleet;
import Map.*;

import java.awt.*;
import java.util.ArrayList;

public class SquadVikings {
    private ArrayList<Warrior> warriors;
    private Building target;
    private int state;  //0-all_dead, 1-fight, 2-retreat
    private int size;
    private Terrain map;
    private Village village;
    private Fleet fleet;
    private Color color;

    public SquadVikings(Terrain map, Village village, Fleet fleet, Building target, Point location, int size, Color color){

    }

    public void draw(Graphics g) {
        for (Warrior i:warriors) i.draw(g);
    }
}
