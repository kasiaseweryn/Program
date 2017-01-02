package Fleet;

import Map.*;
import static Colision.Distance.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fleet {
    public ArrayList<Boat> boats;
    private ArrayList<Target> targets;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;

    // Constructor
    public Fleet(Terrain map, Village village) {
        // Variables for generating
        Random r = new Random();
        int width = map.numCols / 150;
        int length = 5 * map.numRows / 150;
        int amount = village.buildings.size() * 2;
        int generated = 0;

        // Initialazing
        this.boats = new ArrayList<>();
        this.coastH = map.coastH;
        this.coastP = map.coastP;
        this.targets = new ArrayList<>();

        // Generating boats
        int size = coastH.size(), x, y;
        boolean noColision;

        for (int i = 0; i < size; i++) {
            x = coastH.get(i).x - width;
            y = coastH.get(i).y - length;
            noColision = true;
            for (Boat j : boats) {
                double distance = distanceC((x + (width / 2)), (j.currentLocation.x + (j.width / 2)), (y + (length / 2)), (j.currentLocation.y + (j.length / 2)));
                if (distance < length * 1.1) {
                    noColision = false;
                }
            }
            if (noColision && generated < amount) {
                boats.add(new Boat(x, y, width, length, 5, coastH, coastP));
                generated++;
            }
        }

        // Generating targets for boats
        for (Building i : village.buildings) {
            Point building = new Point(i.location.x, i.location.y);
            Point target = new Point();
            int targeted = 0;
            while (targeted < 2) {
                int min = Integer.MAX_VALUE;
                for (Point j : coastP) {
                    if (distanceC(j.x, building.x, j.y, building.y) < min){
                        noColision = true;
                        for (Target t:targets) {
                            if (distanceC(t.target.x, j.x, t.target.y, j.y) < length*2.5){
                                noColision = false;
                            }
                        }
                        if (noColision) {
                            min = (int) distanceC(j.x, building.x, j.y, building.y);
                            target.x = j.x;
                            target.y = j.y;
                        }
                    }
                }
                targets.add(new Target(target, i.location));
                targeted++;
            }
        }

        // Giving boats targets -- temporary function for checking
            for (int i = 0; i < boats.size(); i++) {
                boats.get(i).target(targets.get(i).target);
                targets.get(i).use();
            }
    }


    // Drawing
    public void draw(Graphics g){
        for (Boat i:boats) i.draw(g);
        for (Target i:targets) i .draw(g);
    }
}