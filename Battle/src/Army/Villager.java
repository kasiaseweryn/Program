package Army;

import Map.Building;
import Map.Terrain;
import Map.Village;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.Random;

public class Villager {
    private float health;
    private float moral;
    private float defense;
    private float accuracy;
    private float dodge;
    private Point speed;
    private Point currentLocation;
    private Point previousLocation;
    private Building targetLocation;
    private Viking targetEnemy;
    private Weapon primeWeapon;
    private int thrownWeapon;
    private int vector;
    private boolean shield;
    private int shieldDirection;
    private int loot;
    private boolean inCover;
    private int state;
    private int size;
    private Color color;
    private Terrain map;
    private Village village;

    public Villager(Point location, Terrain map, Village village, Building target, Color color){
        // Variables for generating
        Random r = new Random();

        // Initializing
        this.map = map;
        this.village = village;
        this.currentLocation = location;
        this.previousLocation = location;
        this.targetLocation = target;
        this.targetEnemy = new Viking();
        this.loot = 0;
        this.size = map.numCols/175;
        this.speed = new Point(1,1);
        this.thrownWeapon = r.nextInt(6) + 5;
        this.vector = 0;
        this.shield = false;
        this.shieldDirection = -90;
        this.inCover = false;
        this.state = 1;
        this.health = 100;
        this.color = color;

        this.moral = r.nextInt(21)+40;
        this.defense = r.nextInt(4)+3;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];
    }

    public Villager() {

    }


    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(currentLocation.x - size/2, currentLocation.y - size/2, size, size);
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
