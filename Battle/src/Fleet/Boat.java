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
    public int currentLocationX;
    public int currentLocationY;
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
        this.currentLocationX = x;
        this.currentLocationY = y;
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
        targetLocation.x = currentLocationX;
        targetLocation.y = currentLocationY;
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
        if (currentLocationX != targetLocation.x || currentLocationY != targetLocation.y) {
            if (targetLocation.x > targetLocation.y) {
                if (currentLocationY - targetLocation.y > 0) {
                    rotation = 0;
                    Up(mapa, boats);
                }
                else if (currentLocationY == targetLocation.y && currentLocationX > targetLocation.x) {
                        rotation = -90;
                        Left(mapa, boats);
                }
            }
            if (targetLocation.x < targetLocation.y){
                if (currentLocationX - targetLocation.x > 0) {
                    rotation = -90;
                    Left(mapa, boats);
                } else if (currentLocationX == targetLocation.x && currentLocationY > currentLocationX){
                    rotation = 0;
                    Up(mapa, boats);
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
        if(currentLocationX == previousLocation.x && currentLocationY == previousLocation.y) return false;
        for (Boat i:boats) {
            if (i != this) {
                double distance = distanceC((currentLocationX +(width/2)),(i.currentLocationX +(i.width/2)), (currentLocationY +(this.length/2)), (i.currentLocationY +(i.length/2)));
                //if (distance < length/2 + i.length/2) return false;
                if(currentLocationX < 0 || currentLocationY < 0 || currentLocationX > mapa.numRows || currentLocationY > mapa.numCols) return false;
            }
        }
        return true;
    }

    private void Left(Terrain mapa, ArrayList<Boat> boats)
    {
        //System.out.println("L");
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        int zx = currentLocationX;
        int zy = currentLocationY;
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
        currentLocationY--;
    }
    public void moveDown(){
        currentLocationY++;
    }

    public void moveRight(){
        currentLocationX++;
    }

    public void moveLeft(){
        currentLocationX--;
    }

    public void moveTopRight(){
        currentLocationX++;
        currentLocationY--;
    }

    public void moveTopLeft(){
        currentLocationX--;
        currentLocationY--;
    }

    public void moveBotRight(){
        currentLocationX++;
        currentLocationY++;
    }

    public void moveBotLeft(){
        currentLocationX--;
        currentLocationY++;
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Boat
        g2d.setColor(Colors.BOAT);
        g2d.rotate(Math.toRadians(rotation), currentLocationX, currentLocationY);
        g2d.fillRect(currentLocationX - width/2, currentLocationY - length/2, width, length);
        // Center
        g.setColor(Colors.LOCATION);
        g.fillRect(currentLocationX, currentLocationY, 1, 1);
        // Target
        g.fillRect(targetLocation.x, targetLocation.y, 5, 5);
    }
}