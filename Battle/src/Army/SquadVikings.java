package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Colision.Distance.distanceC;
import static java.lang.Math.*;

public class SquadVikings {
    private ArrayList<Viking> vikings;
    private Building target;
    private int state;  //0-all_dead, 1-fight, 2-retreat, 3-looting
    private int size;
    private Terrain map;
    private Village village;
    private Fleet fleet;
    private ArrayList <SquadVikings> allies;
    private ArrayList <SquadVillagers> enemies;

    public SquadVikings(Terrain map, Village village, Fleet fleet, Building target, Point center, ArrayList<SquadVikings> allies){
        // Variables for generation
        Random r = new Random();
        boolean generated, noColision;
        Point location = new Point();
        Color color;
        int size = map.numCols/120;

        // Initializing
        this.map = map;
        this.village = village;
        this.fleet = fleet;
        this.target = target;
        this.vikings = new ArrayList<>();
        this.size = r.nextInt(3) + 8;
        this.allies = allies;

        // Generating squad
        for (int i = 0; i < this.size; i++){
            generated = false;
            while (!generated) {

                // generating random point in radius of a building
                double angle = toRadians(random() * 360);
                double radius = r.nextDouble() + r.nextInt(26) + size;
                location.x = center.x + (int) (radius * cos(angle));
                location.y = center.y + (int) (radius * sin(angle));

                // checking for vikings
                noColision = true;
                double spread = 1.1;
                for (SquadVikings j : this.allies) {
                    for (Viking k : j.vikings) {
                        double distance = distanceC(location.x, k.getCurrentLocation().x, location.y, k.getCurrentLocation().y);
                        if (distance < size * spread) {
                            noColision = false;
                        }
                    }
                }
                for (Viking m : vikings){
                    double distance = distanceC(location.x, m.getCurrentLocation().x, location.y, m.getCurrentLocation().y);
                    if (distance < size * spread) {
                        noColision = false;
                    }
                }

                // adding viking to squad
                if (noColision) {
                    if (vikings.size() == 0) color = Colors.VIKING_LEADER;                              // toDo make leader a boss! good stats ect :)
                    else color = Colors.VIKING;
                    vikings.add(new Viking(location, map, village, fleet, target, color, size));
                    generated = true;
                }
            }
        }
    }

    public void estimateState(){

    }

    public void action() {                                  //toDo action functions
    }

    public void celebrate() {
    }

    public void surrender() {
    }

    // Setters
    public void setEnemies(ArrayList<SquadVillagers> enemies) {
        this.enemies = enemies;
    }

    // Getters
    public int getState() {
        return state;
    }

    public void draw(Graphics g) {
        for (Viking i: vikings) i.draw(g);
    }
}
