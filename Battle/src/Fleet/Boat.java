package Fleet;

import Map.Building;
import Schemes.*;
import Army.*;
import Map.Terrain;

import java.awt.*;
import java.util.ArrayList;

import static Colision.Distance.*;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;

public class Boat {
    private int size;
    private int speed;
    private ArrayList<Warrior> seats;
    private Point startLocation;
    private Point currentLocation;
    private Point previousLocation;
    private Point targetLocation;
    private Building targetBuilding;
    private int vector;
    private int width;
    private int length;
    private Terrain map;
    private ArrayList<Boat> boats;
    private int state;  //0-have space, 1-full

    public Boat(Terrain map, int x , int y, int width, int length, int size){
        this.size = size;
        this.speed = 1;
        this.seats = new ArrayList<>();
        this.startLocation = new Point(x,y);
        this.targetLocation = new Point(x,y);
        this.previousLocation = new Point(x,y);
        this.currentLocation = new Point(x,y);
        this.vector();
        this.width = width;
        this.length = length;
        this.map = map;
        this.boats = new ArrayList<>();
        this.state = 0;
    }

    public void setTarget(Target target) {
        int tx, ty;
        double interval = 1.8;

        for (double i = 1.8; i > 0.3; i -= 0.1) {
            int[] x = {(int) (length / i), 0, (int) (length / i), (int) (length / i / 2), (int) (length / i / 2), (int) (length / i)};
            int[] y = {0, (int) (length / i), (int) (length / i / 2), (int) (length / i), (int) (length / i / 2), (int) (length / i)};
            for (int j = 0; j < x.length; j++) {
                tx = target.getTarget().x + x[j];
                ty = target.getTarget().y + y[j];
                if (tx - length/interval > 0 && ty-length/interval > 0 && tx < map.numRows && ty < map.numCols){
                    if (map.getTerrainGrid()[(int) (tx - length / interval)][ty] == Colors.OCEAN && map.getTerrainGrid()[tx][(int) (ty - length /interval)] == Colors.OCEAN && map.getTerrainGrid()[(int) (tx + length / interval)][ty] == Colors.OCEAN && map.getTerrainGrid()[tx][(int) (ty + length /interval)] == Colors.OCEAN) {
                        this.targetLocation.x = tx;
                        this.targetLocation.y = ty;
                        this.targetBuilding = target.getBuilding();
                        this.vector();
                        return;
                    }
                }
            }
        }
        return;
    }

    public void setBoats(ArrayList<Boat> boats){
        this.boats = boats;
    }

    public void clearTarget(){
        targetLocation.x = currentLocation.x;
        targetLocation.y = currentLocation.y;
    }

    public void addWarior(Warrior warrior){
        if (state == 0) {
            seats.add(warrior);
            if (size == seats.size()) state = 1;
        }
    }

    public void removeWarior(Warrior warrior){
        if (seats.size() > 0) {
            seats.remove(warrior);
            state = 0;
        }
    }

    public void move() {
        if (currentLocation.x != targetLocation.x || currentLocation.y != targetLocation.y) {
            this.vector();
            if (targetLocation.x >= targetLocation.y) {
                if (currentLocation.y - targetLocation.y > 0) Up();
                else {
                    if (currentLocation.y == targetLocation.y && currentLocation.x > targetLocation.x) Left();
                    else Right();
                }
                if (currentLocation.y - targetLocation.y < 0) Down();
            }
            if (targetLocation.x < targetLocation.y){
                if (currentLocation.x - targetLocation.x > 0) Left();
                else {
                    if (currentLocation.x == targetLocation.x && currentLocation.y > currentLocation.x) Up();
                    else Down();
                }
                if (currentLocation.x - targetLocation.x < 0) Right();
            }
        }
    }

    private void vector(){
        vector = -(int) (atan2(currentLocation.x - targetLocation.x, currentLocation.y - targetLocation.y)*(180/PI));
    }

    private boolean checkB() {

        for (Boat i:boats) {
            if (i != this) {
                double distance = distanceC((currentLocation.x +(width/2)),(i.currentLocation.x +(i.width/2)), (currentLocation.y +(this.length/2)), (i.currentLocation.y +(i.length/2)));
                if (distance < length/2 + i.length/2) return false;
            }
        }
        return true;
    }

    private boolean checkM(){
        double interval = 3.5;
        if(currentLocation.x == previousLocation.x && currentLocation.y == previousLocation.y) return false;
        if(currentLocation.x < length/2 || currentLocation.y < length/2 || currentLocation.x > map.numRows - length/2 || currentLocation.y > map.numCols - length/2) return false;
        if(map.getTerrainGrid()[(int) (currentLocation.x - length / interval)][currentLocation.y] != Colors.OCEAN || map.getTerrainGrid()[currentLocation.x][(int) (currentLocation.y - length /interval)] != Colors.OCEAN || map.getTerrainGrid()[(int) (currentLocation.x + length / interval)][currentLocation.y] != Colors.OCEAN || map.getTerrainGrid()[currentLocation.x][(int) (currentLocation.y + length /interval)] != Colors.OCEAN) return false; //toDo
        return true;

    }

    private void Left() {
        //System.out.println("L");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveLeft();
        if (!checkB()) {
            moveRight();
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveDown();
        if (!checkB()) {
            moveUp();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveUp();
        if (!checkB()) {
            moveDown();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();
    }

    private void Right() {
        //System.out.println("R");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveRight();
        if (!checkB()) {
            moveLeft();
            moveLeft();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveUp();
        if (!checkB()) {
            moveDown();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

        moveDown();
        if (!checkB()) {
            moveUp();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

    }

    private void Up() {
        //System.out.println("U");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveUp();
        if (!checkB()) {
            moveDown();
            moveDown();
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

        moveRight();
        if (!checkB()) {
            moveLeft();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveLeft();
        if (!checkB()) {
            moveRight();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();
    }

    private void Down() {
        //System.out.println("D");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveDown();
        if (!checkB()) {
            moveUp();
            moveUp();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveLeft();
        if (!checkB()) {
            moveRight();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveRight();
        if (!checkB()) {
            moveLeft();
            return;
        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

    }

    private void moveUp(){
        currentLocation.y -= speed;
    }

    private void moveDown(){
        currentLocation.y += speed;
    }

    private void moveRight(){
        currentLocation.x += speed;
    }

    private void moveLeft(){
        currentLocation.x -= speed;
    }

    //Draw
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

    // Getters
    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getState() {
        return state;
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }
}