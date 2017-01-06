package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SquadVikings {
    private ArrayList<Viking> vikings;
    private Building target;
    private int state;  //0-all_dead, 1-fight, 2-retreat
    private int size;
    private Terrain map;
    private Village village;
    private Fleet fleet;

    public SquadVikings(Terrain map, Village village, Fleet fleet, Building target, Point location){
        // Variables for generation
        Random r = new Random();

        // Initializing
        this.map = map;
        this.village = village;
        this.fleet = fleet;
        this.target = target;
        this.size = r.nextInt(3) + 8;
    }

    public void draw(Graphics g) {
        for (Viking i: vikings) i.draw(g);
    }
}
