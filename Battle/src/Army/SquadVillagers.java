package Army;

import Map.Building;
import Map.Terrain;
import Map.Village;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Colision.Distance.distanceC;
import static java.lang.Math.*;

public class SquadVillagers {
    private ArrayList<Villager> villagers;
    private Building target;
    private int state;  //0-all_dead, 1-fight, 2-retreat
    private int size;
    private Terrain map;
    private Village village;
    private ArrayList<SquadVikings> enemies;
    private ArrayList<SquadVillagers> allies;

    public SquadVillagers(Terrain map, Village village, Building target, ArrayList<SquadVillagers> allies){
        // Variables for generation
        Random r = new Random();
        boolean generated, noColision;
        Point location = new Point();
        Color color;
        int size = map.numCols/120;
        double radius = sqrt((target.getWidth()*target.getWidth()) + target.getHeight()*target.getHeight())/2 + size;

        // Initializing
        this.map = map;
        this.village = village;
        this.target = target;
        this.villagers = new ArrayList<>();
        this.size = r.nextInt(3) + 8;
        this.allies = allies;

        // Generating squad
        for (int i = 0; i < this.size; i++){
            generated = false;
            while (!generated) {

                // generating random point in radius of a building
                double angle = Math.toRadians(Math.random() * 360);
                location.x = target.getLocation().x + (int) (radius * cos(angle));
                location.y = target.getLocation().y + (int) (radius * sin(angle));

                // checking for villagers
                noColision = true;
                double spread = 1.1;
                for (SquadVillagers j : this.allies) {
                    for (Villager k : j.villagers) {
                        double distance = distanceC(location.x, k.getCurrentLocation().x, location.y, k.getCurrentLocation().y);
                        if (distance < size * spread) {
                            noColision = false;
                        }
                    }
                }
                for (Villager m : villagers){
                    double distance = distanceC(location.x, m.getCurrentLocation().x, location.y, m.getCurrentLocation().y);
                    if (distance < size * spread) {
                        noColision = false;
                    }
                }

                // adding villager to squad
                if (noColision) {
                    if (villagers.size() == 0) color = Colors.VILLAGER_LEADER;                              // toDo make leader a boss! good stats ect :)
                    else color = Colors.VILLAGER;
                    villagers.add(new Villager(location, map, village, target, color, size));
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
    public void setEnemies(ArrayList<SquadVikings> enemies) {
        this.enemies = enemies;
    }

    // Getters
    public int getState() {
        return state;
    }

    public void draw(Graphics g) {
        for( Villager i : villagers) i.draw(g);
    }
}
