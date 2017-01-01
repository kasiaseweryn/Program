package Battle;

import java.awt.*;
import java.util.ArrayList;

public class Boat {
    private int size;
    private ArrayList<Warior> seats;
    private int speedX;
    private int speedY;
    public int current_locationX;
    public int current_locationY;
    private int previous_locationX;
    private int previous_locationY;
    private int target_locationX;
    private int target_locationY;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;
    private int rotation;
    public int width;
    public int length;
    public int state;  //0-have space, 1-full

    public Boat(int x, int y, int width, int length, int size, ArrayList<Point> coastH, ArrayList<Point> coastP){
        this.size = size;
        seats = new ArrayList<Warior>();
        this.speedX = 2;
        this.speedY = 2;
        this.current_locationX = x;
        this.current_locationY = y;
        this.previous_locationX = x;
        this.previous_locationY = y;
        this.target_locationX = x;
        this.target_locationY = y;
        this.coastH = coastH;
        this.coastP = coastP;
        this.rotation = 0;
        this.width = width;
        this.length = length;
        this.state = 0;
    }

    public void target(Building building) {
        int targetX = building.x;
        int targetY = building.y;
        int min = Integer.MAX_VALUE;
        for (Point i : coastH) {
            if (distance(i.x, targetX, i.y, targetY) < min) {
                min = (int) distance(i.x, targetX, i.y, targetY);
                target_locationX = i.x;
                target_locationY = i.y;
            }
        }
        for (Point i : coastP) {
            if (distance(i.x, targetX, i.y, targetY) < min) {
                min = (int) distance(i.x, targetX, i.y, targetY);
                target_locationX = i.x;
                target_locationY = i.y;
            }
        }
        System.out.println(target_locationX + ":" + target_locationY);
    }

    private double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void clearTarget(){
        target_locationX = current_locationX;
        target_locationY = current_locationY;
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

    public void move(Map mapa, ArrayList<Boat> boats) {
        if (current_locationX != target_locationX || current_locationY != target_locationY) {
            if (target_locationX > target_locationY) {
                if (current_locationY - target_locationY > 0) {
                    rotation = 0;
                    Up(mapa, boats);
                }
                else if (current_locationY == target_locationY && current_locationX > target_locationX) {
                        rotation = -90;
                        Left(mapa, boats);
                }
            }
            if (target_locationX < target_locationY){
                if (current_locationX - target_locationX > 0) {
                    rotation = -90;
                    Left(mapa, boats);
                } else if (current_locationX == target_locationX && current_locationY > current_locationX){
                    rotation = 0;
                    Up(mapa, boats);
                }
            }
        }

//        if(current_locationX - target_locationX > 0) {
//            if(current_locationY - target_locationX > 0)  TopLeft(mapa, boats);
//            else if (current_locationY - target_locationY < 0) BotLeft(mapa, boats);
//            else Left(mapa, boats);
//        }
//        else if (current_locationX - target_locationX < 0) {
//            if(current_locationY - target_locationY > 0)  TopRight(mapa, boats);
//            else if (current_locationY - target_locationY  < 0) BotRight(mapa, boats);
//            else Right(mapa, boats);
//        }
//        else {
//            if(current_locationY - target_locationY  > 0)  Up(mapa, boats);
//            else Down(mapa, boats);
//        }
//        }
    }

    private boolean check(Map mapa, ArrayList<Boat> boats) {
        if(current_locationX == previous_locationX && current_locationY == previous_locationY) return false;
        for (Boat i:boats) {
            if (i != this) {
                double distance = distance((current_locationX+(width/2)),(i.current_locationX+(i.width/2)), (current_locationY+(this.length/2)), (i.current_locationY+(i.length/2)));
                //if (distance < length/2 + i.length/2) return false;
                if(current_locationX < 0 || current_locationY < 0 || current_locationX > mapa.numRows || current_locationY > mapa.numCols) return false;
            }
        }
        return true;
    }

    private void Left(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("L");
        int zx = current_locationX;
        int zy = current_locationY;
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();
        moveTopLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotRight();
        moveBotLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopRight();
        moveUp();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveDown();
        moveDown();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveUp();

    }

    public void Right(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("R");
        int zx = current_locationX;
        int zy = current_locationY;
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();
        moveTopRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotLeft();
        moveBotRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopLeft();
        moveUp();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveDown();
        moveDown();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveUp();

    }

    public void Up(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("U");
        int zx = current_locationX;
        int zy = current_locationY;
        moveUp();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveDown();
        moveTopRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotLeft();
        moveTopLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotRight();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();


    }

    public void Down(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("D");
        int zx = current_locationX;
        int zy = current_locationY;
        moveDown();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveUp();
        moveBotLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopRight();
        moveBotRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopLeft();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();

    }

    public void TopLeft(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("TL");
        int zx = current_locationX;
        int zy = current_locationY;
        moveTopLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotRight();
        moveUp();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveDown();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();
        moveTopRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotLeft();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();


    }

    public void TopRight(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("TR");
        int zx = current_locationX;
        int zy = current_locationY;
        moveTopRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotLeft();
        moveUp();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveDown();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();
        moveTopLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveBotRight();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();

    }

    public void BotLeft(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("BL");
        int zx = current_locationX;
        int zy = current_locationY;
        moveBotLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopRight();
        moveDown();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveUp();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();
        moveBotRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopLeft();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();

    }

    public void BotRight(Map mapa, ArrayList<Boat> boats)
    {
        //System.out.println("BR");
        int zx = current_locationX;
        int zy = current_locationY;
        moveBotRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopLeft();
        moveDown();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveUp();
        moveRight();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveLeft();
        moveBotLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveTopRight();
        moveLeft();
        if(check(mapa, boats)) {previous_locationX=zx; previous_locationY=zy;return;}
        moveRight();

    }

    public void moveUp(){
        current_locationY--;
    }
    public void moveDown(){
        current_locationY++;
    }

    public void moveRight(){
        current_locationX++;
    }

    public void moveLeft(){
        current_locationX--;
    }

    public void moveTopRight(){
        current_locationX++;
        current_locationY--;
    }

    public void moveTopLeft(){
        current_locationX--;
        current_locationY--;
    }

    public void moveBotRight(){
        current_locationX++;
        current_locationY++;
    }

    public void moveBotLeft(){
        current_locationX--;
        current_locationY++;
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Colors.BOAT);
        g2d.rotate(Math.toRadians(rotation),current_locationX, current_locationY);
        g2d.fillRect(current_locationX-width/2, current_locationY-length/2, width, length);
        // to see the target
        g.setColor(Color.RED);
        g.fillRect(target_locationX, target_locationY, 5, 5);
        g.fillRect(current_locationX, current_locationY, 1, 1);
    }
}

