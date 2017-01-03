package Fleet;

import Map.*;
import Schemes.Colors;

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
        int amount = village.buildings.size()*2;
        int generated = 0;

        // Initialazing
        this.boats = new ArrayList<>();
        this.coastH = map.coastH;
        this.coastP = map.coastP;
        this.targets = new ArrayList<>();

        // Generating boats
        int size = coastH.size(), x, y;
        boolean noColision;

        for (int i = 0; i < size; i++) {  // toDo better x and y
            x = coastH.get(i).x;
            y = coastH.get(i).y;
            noColision = false;

            // distacne from coast
            double[] divider = {1.8,1.5,1,0.8,0.5,0.1};
            int tx, ty;
            Point temp = new Point(x,y);

            for (int k = 0; k < divider.length; k++) {
                int[] cx = {0, (int) (length / divider[k]), (int) (length / divider[k])};
                int[] cy = {(int) (length / divider[k]), 0, (int) (length / divider[k])};
                for (int l = 0; l < 3; l++) {
                    tx = temp.x - cx[l];
                    ty = temp.y - cy[l];
                    if (tx > 0 && ty > 0 && tx + length/1.8 < map.numRows && ty + length/1.8 < map.numCols)
                        if (map.terrainGrid[(int) (tx + length / 1.8)][ty] == Colors.OCEAN && map.terrainGrid[tx][(int) (ty + length /1.8)] == Colors.OCEAN) {
                            x = tx;
                            y = ty;
                            noColision = true;
                        }
                    if (noColision) break;
                }
                if (noColision) break;
            }

            // distance from boats
            noColision = true;
            for (Boat j : boats) {
                double distance = distanceC((x + (width / 2)), (j.currentLocation.x + (j.width / 2)), (y + (length / 2)), (j.currentLocation.y + (j.length / 2)));
                if (distance < length * 1.1) {
                    noColision = false;
                }
            }
            if (noColision && generated < amount) {
                boats.add(new Boat(x, y, width, length, 5));
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
                        // distance from boats
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

        // Giving boats targets -- temporary function for checking -- may leave it not so bad :)
            for (int i = 0; i < boats.size(); i++) {
                boats.get(i).target(targets.get(i).target, map);
                targets.get(i).use();
            }
    }


    // Drawing
    public void draw(Graphics g){
        for (Boat i:boats) i.draw(g);
        for (Target i:targets) i .draw(g);
    }
}