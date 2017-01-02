package Fleet;

import Schemes.Colors;
import Army.Warior;
import Map.Terrain;

import java.awt.*;
import java.util.ArrayList;
import static Colision.Distance.*;

public class Boat {
    private int size;
    private ArrayList<Warior> seats;
    public Point currentLocation;
    private Point previousLocation;
    public Point targetLocation;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;
    private int rotation;
    public int width;
    public int length;
    public int state;  //0-have space, 1-full

    public Boat(int x , int y, int width, int length, int size, ArrayList<Point> coastH, ArrayList<Point> coastP){
        this.size = size;
        this.seats = new ArrayList<>();
        this.targetLocation = new Point(x,y);
        this.previousLocation = new Point(x,y);
        this.currentLocation = new Point(x,y);
        this.coastH = coastH;
        this.coastP = coastP;
        this.rotation = 0;
        this.width = width;
        this.length = length;
        this.state = 0;
    }

    public void target(Point target) {
        targetLocation.x = target.x;
        targetLocation.y = target.y;
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

    public void move(Terrain mapa, ArrayList<Boat> boats) {
        if (currentLocation.x != targetLocation.x || currentLocation.y != targetLocation.y) {
            if (targetLocation.x > targetLocation.y) {
                if (currentLocation.y - targetLocation.y > 0) {
                    rotation = 0;
                    Up(mapa, boats);
                }
                else {
                    if (currentLocation.y == targetLocation.y && currentLocation.x > targetLocation.x) {
                        rotation = -90;
                        Left(mapa, boats);
                    }
                    else {
                        rotation = 90;
                        Right(mapa, boats);
                    }
                }
            }
            if (targetLocation.x < targetLocation.y){
                if (currentLocation.x - targetLocation.x > 0) {
                    rotation = -90;
                    Left(mapa, boats);
                } else {
                    if (currentLocation.x == targetLocation.x && currentLocation.y > currentLocation.x){
                        rotation = 0;
                        Up(mapa, boats);
                    }
                    else {
                        rotation = 180;
                        Down(mapa, boats);
                    }
                }
            }
        }

//        if(currentLocationX - targetLocation.x > 0) {
//            if(currentLocationY - targetLocation.x > 0)  TopLeft(mapa, boats);
//            else if (currentLocationY - targetLocation.y < 0) BotLeft(mapa, boats);
//            else Left(mapa, boats);
//        }
//        else if (currentLocationX - targetLocation.x < 0) {
//            if(currentLocationY - targetLocation.y > 0)  TopRight(mapa, boats);
//            else if (currentLocationY - targetLocation.y  < 0) BotRight(mapa, boats);
//            else Right(mapa, boats);
//        }
//        else {
//            if(currentLocationY - targetLocation.y  > 0)  Up(mapa, boats);
//            else Down(mapa, boats);
//        }
    }

    private boolean check(Terrain mapa, ArrayList<Boat> boats) {
        if(currentLocation.x == previousLocation.x && currentLocation.y == previousLocation.y) return false;
        for (Boat i:boats) {
            if (i != this) {
                double distance = distanceC((currentLocation.x +(width/2)),(i.currentLocation.x +(i.width/2)), (currentLocation.y +(this.length/2)), (i.currentLocation.y +(i.length/2)));
                //if (distance < length/2 + i.length/2) return false;
                if(currentLocation.x < 0 || currentLocation.y < 0 || currentLocation.x > mapa.numRows || currentLocation.y > mapa.numCols) return false;
            }
        }
        return true;
    }

    private void Left(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("L");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();
        moveTopLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotRight();
        moveBotLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopRight();
        moveUp();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveDown();
        moveDown();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveUp();

    }

    public void Right(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("R");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();
        moveTopRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotLeft();
        moveBotRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopLeft();
        moveUp();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveDown();
        moveDown();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveUp();

    }

    public void Up(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("U");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveUp();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveDown();
        moveTopRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotLeft();
        moveTopLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotRight();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();


    }

    public void Down(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("D");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveDown();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveUp();
        moveBotLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopRight();
        moveBotRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopLeft();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();

    }

    public void TopLeft(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("TL");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveTopLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotRight();
        moveUp();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveDown();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();
        moveTopRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotLeft();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();


    }

    public void TopRight(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("TR");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveTopRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotLeft();
        moveUp();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveDown();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();
        moveTopLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveBotRight();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();

    }

    public void BotLeft(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("BL");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveBotLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopRight();
        moveDown();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveUp();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();
        moveBotRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopLeft();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();

    }

    public void BotRight(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("BR");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveBotRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopLeft();
        moveDown();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveUp();
        moveRight();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveLeft();
        moveBotLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveTopRight();
        moveLeft();
        if(check(mapa, boats)) {
            previousLocation.x =zx; previousLocation.y =zy;return;}
        moveRight();

    }

    public void moveUp(){
        currentLocation.y--;
    }
    public void moveDown(){
        currentLocation.y++;
    }

    public void moveRight(){
        currentLocation.x++;
    }

    public void moveLeft(){
        currentLocation.x--;
    }

    public void moveTopRight(){
        currentLocation.x++;
        currentLocation.y--;
    }

    public void moveTopLeft(){
        currentLocation.x--;
        currentLocation.y--;
    }

    public void moveBotRight(){
        currentLocation.x++;
        currentLocation.y++;
    }

    public void moveBotLeft(){
        currentLocation.x--;
        currentLocation.y++;
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Boat
        g2d.setColor(Colors.BOAT);
        g2d.rotate(Math.toRadians(rotation), currentLocation.x, currentLocation.y);
        g2d.fillRect(currentLocation.x - width/2, currentLocation.y - length/2, width, length);
        // Center
        g.setColor(Colors.LOCATION);
        g.fillRect(currentLocation.x, currentLocation.y, 1, 1);
        // Target
        g.fillRect(targetLocation.x, targetLocation.y, 5, 5);
    }
}