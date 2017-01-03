package Fleet;

import Schemes.*;
import Army.*;
import Map.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Colision.Distance.*;
import static java.lang.Math.abs;

public class Boat {
    private int size;
    private ArrayList<Warior> seats;
    private Point startLocation;
    public Point currentLocation;
    private Point previousLocation;
    public Point targetLocation;
    private int vector;
    public int width;
    public int length;
    public int state;  //0-have space, 1-full

    public Boat(int x , int y, int width, int length, int size){
        this.size = size;
        this.seats = new ArrayList<>();
        this.startLocation = new Point(x,y);
        this.targetLocation = new Point(x,y);
        this.previousLocation = new Point(x,y);
        this.currentLocation = new Point(x,y);
        this.vector = Vectors.UP;
        this.width = width;
        this.length = length;
        this.state = 0;
    }

    public void target(Point target, Terrain map) {   // toDo better but still my be better
        double[] divider = {1.8,1.5,1,0.8,0.5};
        int tx, ty;
        for (int i = 0; i < divider.length; i++) {
            int[] x = {0, (int) (length / divider[i]), (int) (length / divider[i])};
            int[] y = {(int) (length / divider[i]), 0, (int) (length / divider[i])};
            for (int j = 0; j < 3; j++) {
                tx = target.x + x[j];
                ty = target.y + y[j];
                if (tx - length/1.8 > 0 && ty-length/1.8 > 0 && tx < map.numRows && ty < map.numCols)
                    if (map.terrainGrid[(int) (tx - length / 1.8)][ty] == Colors.OCEAN && map.terrainGrid[tx][(int) (ty - length /1.8)] == Colors.OCEAN) {
                            targetLocation.x = tx;
                            targetLocation.y = ty;
                            this.vector();
                            return;
                    }
            }
        }
        return;
    }

    public void clearTarget(){
        targetLocation.x = currentLocation.x;
        targetLocation.y = currentLocation.y;
    }

    public void addWarior(Warior warior){
        if (state == 0) {
            seats.add(warior);
            if (size == seats.size()) state = 1;
        }
    }

    public void removeWarior(Warior warior){
        if (seats.size() > 0) {
            seats.remove(warior);
            state = 0;
        }
    }

    public void move(Terrain map, ArrayList<Boat> boats) {
        if (currentLocation.x != targetLocation.x || currentLocation.y != targetLocation.y) {
            this.vector();
            if (targetLocation.x >= targetLocation.y) {
                if (currentLocation.y - targetLocation.y > 0) Up(map, boats);
                else {
                    if (currentLocation.y == targetLocation.y && currentLocation.x > targetLocation.x) Left(map, boats);
                    else Right(map, boats);
                }
                if (currentLocation.y - targetLocation.y < 0) Down(map, boats);
            }
            if (targetLocation.x < targetLocation.y){
                if (currentLocation.x - targetLocation.x > 0) Left(map, boats);
                else {
                    if (currentLocation.x == targetLocation.x && currentLocation.y > currentLocation.x) Up(map, boats);
                    else Down(map, boats);
                }
                if (currentLocation.x - targetLocation.x < 0) Right(map, boats);
            }
        }
    }
    private void vector(){
        if (currentLocation.y == targetLocation.y && currentLocation.x != targetLocation.x) {
            if (currentLocation.x > targetLocation.x) {
                vector = Vectors.LEFT;
                return;
            } else {
                vector = Vectors.RIGHT;
                return;
            }
        }
        if (currentLocation.x == targetLocation.x && currentLocation.y != targetLocation.y) {
            if (currentLocation.y > targetLocation.y) {
                vector = Vectors.UP;
                return;
            } else {
                vector = Vectors.DOWN;
                return;
            }
        }
        if(currentLocation.x != targetLocation.x || currentLocation.y != targetLocation.y) {
            if (currentLocation.x > targetLocation.x){
                if (currentLocation.y > targetLocation.y) {
                    vector = Vectors.LEFT_UP;
                    return;
                }
                else {
                    vector = Vectors.LEFT_DOWN;
                    return;
                }
            }
            if (currentLocation.x < targetLocation.x) {
                if (currentLocation.y > targetLocation.y) {
                    vector = Vectors.RIGHT_UP;
                    return;
                }
                else {
                    vector = Vectors.RIGHT_DOWN;
                    return;
                }
            }
        }
    }

    private boolean checkB(ArrayList<Boat> boats) {

        for (Boat i:boats) {
            if (i != this) {
                double distance = distanceC((currentLocation.x +(width/2)),(i.currentLocation.x +(i.width/2)), (currentLocation.y +(this.length/2)), (i.currentLocation.y +(i.length/2)));
                if (distance < length/2 + i.length/2) return false;
            }
        }
        return true;
    }

    private boolean checkM(Terrain map){
        if(currentLocation.x == previousLocation.x && currentLocation.y == previousLocation.y) return false;
        if(currentLocation.x < map.numRows*0.02 || currentLocation.y < map.numCols*0.02 || currentLocation.x > map.numRows*0.98 || currentLocation.y > map.numCols*0.98) return false;
        if(map.terrainGrid[(int) (currentLocation.x-length/3.4)][(int) (currentLocation.y-length/3.4)] != Colors.OCEAN) return false; //toDo
        return true;

    }

    private void Left(Terrain map, ArrayList<Boat> boats) {
        //System.out.println("L");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveLeft();
        if (!checkB(boats)) {
            moveRight();
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveDown();
        if (!checkB(boats)) {
            moveUp();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveUp();
        if (!checkB(boats)) {
            moveDown();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();
    }

    private void Right(Terrain map, ArrayList<Boat> boats) {
        //System.out.println("R");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveRight();
        if (!checkB(boats)) {
            moveLeft();
            moveLeft();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveUp();
        if (!checkB(boats)) {
            moveDown();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

        moveDown();
        if (!checkB(boats)) {
            moveUp();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

    }

    private void Up(Terrain map, ArrayList<Boat> boats) {
        //System.out.println("U");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveUp();
        if (!checkB(boats)) {
            moveDown();
            moveDown();
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

        moveRight();
        if (!checkB(boats)) {
            moveLeft();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveLeft();
        if (!checkB(boats)) {
            moveRight();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();
    }

    private void Down(Terrain map, ArrayList<Boat> boats) {
        //System.out.println("D");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveDown();
        if (!checkB(boats)) {
            moveUp();
            moveUp();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveLeft();
        if (!checkB(boats)) {
            moveRight();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveRight();
        if (!checkB(boats)) {
            moveLeft();
            return;
        }
        if (checkM(map)) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

    }

    private void moveUp(){
        currentLocation.y--;
    }
    private void moveDown(){
        currentLocation.y++;
    }

    private void moveRight(){
        currentLocation.x++;
    }

    private void moveLeft(){
        currentLocation.x--;
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Boat
        g2d.setColor(Colors.BOAT);
        g2d.rotate(Math.toRadians(vector), currentLocation.x, currentLocation.y);
        g2d.fillRect(currentLocation.x - width/2, currentLocation.y - length/2, width, length);
        // Center
        g.setColor(Colors.LOCATION);
        g.fillRect(currentLocation.x, currentLocation.y, 1, 1);
        // Target
        g.fillRect(targetLocation.x, targetLocation.y, 3, 3);
        // Start
        g.fillRect(startLocation.x, startLocation.y, 3, 3);
    }
}