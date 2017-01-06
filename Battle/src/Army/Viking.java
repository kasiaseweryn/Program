package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.Random;

public class Viking {
    private float health;
    private float moral;
    private float defense;
    private float accuracy;
    private float dodge;
    private Point speed;
    private Point currentLocation;
    private Point previousLocation;
    private Building targetLocation;
    private Villager targetEnemy;
    private Weapon primeWeapon;
    private int thrownWeapon;
    private int vector;
    private boolean shield;
    private int shieldDirection;
    private int loot;
    private boolean inCover;
    private int state;
    private int size;
    private Terrain map;
    private Village village;
    private Fleet fleet;

    public Viking(Terrain map, Village village, Fleet fleet, Building targetLocation, Point currentLocation) {
        Random r = new Random();

        this.map = map;
        this.village = village;
        this.fleet = fleet;
        this.currentLocation = new Point();
        this.previousLocation = new Point();
        this.targetLocation = targetLocation;
        this.targetEnemy = new Villager();
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

        this.moral = r.nextInt(21) + 40;
        this.defense = r.nextInt(4) + 2;
        this.accuracy = r.nextInt(31) + 30;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];


    }

    public Viking() {

    }

    public void draw(Graphics g) {
        g.setColor(Colors.VIKING);
        g.fillOval(currentLocation.x, currentLocation.y, size, size);
    }
}