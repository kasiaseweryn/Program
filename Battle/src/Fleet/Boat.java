package Fleet;

import Schemes.*;
import Army.Warior;
import Map.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import static Colision.Distance.*;
import static java.lang.Math.abs;

public class Boat {
    private int size;
    private ArrayList<Warior> seats;
    private Point startLocation;
    public Point currentLocation;
    private Point previousLocation;
    public Point targetLocation;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;
    private int vector;
    public int width;
    public int length;
    public int state;  //0-have space, 1-full

    public Boat(int x , int y, int width, int length, int size, ArrayList<Point> coastH, ArrayList<Point> coastP){
        this.size = size;
        this.seats = new ArrayList<>();
        this.startLocation = new Point(x,y);
        this.targetLocation = new Point(x,y);
        this.previousLocation = new Point(x,y);
        this.currentLocation = new Point(x,y);
        this.coastH = coastH;
        this.coastP = coastP;
        this.vector = Vectors.UP;
        this.width = width;
        this.length = length;
        this.state = 0;
    }

    public void target(Point target) {   // toDo target without colision with land
//        for (int i = target.x; ; i++){
//            for (int j = target.y; ; j++){
//                if(){
//                    targetLocation.x = i;
//                    targetLocation.y = j;
//                }
//            }
//        }
        targetLocation.x = target.x + length;
        targetLocation.y = target.y + length;
        this.vector();
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
        if(currentLocation.x < map.numRows*0.015 || currentLocation.y < map.numCols*0.015 || currentLocation.x > map.numRows*0.985 || currentLocation.y > map.numCols*0.985) return false;
        if(map.terrainGrid[currentLocation.x-length/3][currentLocation.y-length/3] != Colors.OCEAN) return false; //toDo
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