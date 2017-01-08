package Fleet;

import Map.*;
import Schemes.Colors;

import static Colision.Distance.*;

import java.awt.*;
import java.util.ArrayList;

public class Fleet {
    private ArrayList<Boat> boats;
    private ArrayList<Target> targets;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;

    // Constructor
    public Fleet(Terrain map, Village village) {
        // Variables for generating
        int number = 2;                                         // number of boats per building
        int width = map.numCols / 150;                          // width of a boat
        int length = 5 * map.numCols / 150;                     // length of a boat/number of seats -- now 5
        int amount = village.getBuildings().size() * number;    // amount of boats

        // Initialazing
        this.boats = new ArrayList<>();
        this.coastH = map.getCoastH();
        this.coastP = map.getCoastP();
        this.targets = new ArrayList<>();

        // Generating boats
        Point location = new Point();
        boolean noColision;
        int generated = 0;                                      // number of generated boats

        for (int i = 0; i < coastH.size(); i++) {
            location.x = coastH.get(i).x;
            location.y = coastH.get(i).y;
            noColision = false;

            // distacne from coast
            int tx, ty;                                         //temporary variable for location
            double interval = 1.8;

            for (double k = 1.8; k > 0.4 ; k -= 0.1) {
                int[] x = {(int) (length / k), 0, (int) (length / k), (int) (length / k / 2), (int) (length / k / 2), (int) (length / k)};
                int[] y = {0, (int) (length / k), (int) (length / k / 2), (int) (length / k), (int) (length / k / 2), (int) (length / k)};
                    for (int l = 0; l < 3; l++) {
                        tx = location.x - x[l];
                        ty = location.y - y[l];
                        if (tx > 0 && ty > 0 && tx + length / interval < map.numRows && ty + length / interval < map.numCols)
                            if (map.getTerrainGrid()[(int) (tx - length / interval)][ty] == Colors.OCEAN && map.getTerrainGrid()[tx][(int) (ty - length /interval)] == Colors.OCEAN && map.getTerrainGrid()[(int) (tx + length / interval)][ty] == Colors.OCEAN && map.getTerrainGrid()[tx][(int) (ty + length /interval)] == Colors.OCEAN) {
                                location.x = tx;
                                location.y = ty;
                                noColision = true;
                            }
                        if (noColision) break;
                    }
                if (noColision) break;
            }

            // distance from boats
            noColision = true;
            double spread = 1.1;

            for (Boat j : boats) {
                double distance = distanceC((location.x + (width / 2)), (j.getCurrentLocation().x + (j.getWidth() / 2)), (location.y + (length / 2)), (j.getCurrentLocation().y + (j.getLength() / 2)));
                if (distance < length * spread) {
                    noColision = false;
                }
            }
            // adding boats
            if (noColision && generated < amount) {
                boats.add(new Boat(map, location, width, length, 5));
                generated++;
            }
        }

        // Adding list of boats to every boat
        for (Boat i : boats){
            i.setBoats(boats);
        }

        // Generating targets for boats
        Point target = new Point();
        double spread = 2.5;

        for (Building i : village.getBuildings()) {
            Point building = new Point(i.getLocation().x, i.getLocation().y);
            int targeted = 0;
            while (targeted < 2) {
                int min = Integer.MAX_VALUE;
                for (Point coast : coastP) {
                    // distance from building
                    double distance = distanceC(coast.x, building.x, coast.y, building.y);
                    if (distance < min){
                        // distance from boats
                        noColision = true;
                        for (Target t:targets) {
                            if (distanceC(t.getTarget().x, coast.x, t.getTarget().y, coast.y) < length*spread){
                                noColision = false;
                            }
                        }
                        // setting new mininum and setTarget;
                        if (noColision) {
                            min = (int) distance;
                            target.x = coast.x;
                            target.y = coast.y;
                        }
                    }
                }
                targets.add(new Target(target, i));
                targeted++;
            }
        }

        // Giving boats targets -- temporary function for checking -- may leave it not so bad :)
            for (int i = 0; i < boats.size(); i++) {
                boats.get(i).setTarget(targets.get(i));
                targets.get(i).use();
            }
    }

    //Getters
    public ArrayList<Boat> getBoats() {
        return boats;
    }

    // Drawing
    public void draw(Graphics g){
        for (Boat i:boats) i.draw(g);
        for (Target i:targets) i.draw(g);
    }
}