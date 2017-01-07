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

    public SquadVillagers(Terrain map, Village village, Building target){
        // Variables for generation
        Random r = new Random();
        boolean generated, noColision;
        Point location = new Point();
        Color color;
        int size = map.numCols/150;
        double angle = 0;
        double radius = sqrt((target.getWidth()*target.getWidth()) + target.getHeight()*target.getHeight())/2 + size;

        // Initializing
        this.map = map;
        this.village = village;
        this.target = target;
        this.villagers = new ArrayList<>();
        this.size = r.nextInt(3) + 8;

        // Generating squad
        for (int i = 0; i < this.size; i++){
            generated = false;
            while (!generated) {
                // generating random point in radius of a building
                angle += 6.28 / this.size;
                location.x = target.getLocation().x + (int) (radius * cos(angle));
                location.y = target.getLocation().y + (int) (radius * sin(angle));

                // checking for villagers
                noColision = true;
//                double spread = 1.1;
//
//                for (Villager j : villagers) {
//                    double distance = distanceC(location.x, j.getCurrentLocation().x, location.y, j.getCurrentLocation().y); //toDO get whole army not only squad
//                    System.out.println(j.getCurrentLocation().x + ":" + j.getCurrentLocation().y);
//                    if (distance < size * spread) {
//                        noColision = false;
//                    }
//                }

                // adding villager to squad
                if (noColision) {
                    if (villagers.size() == 0) color = Colors.VILLAGER_LEADER;
                    else color = Colors.VILLAGER;
                    villagers.add(new Villager(location, map, village, target, color));
                    generated = true;
                }
            }
        }
    }

    public void draw(Graphics g) {
        for( Villager i : villagers) i.draw(g);

    }
}
