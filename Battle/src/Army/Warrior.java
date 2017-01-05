package Army;

import Map.*;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.Random;

public class Warrior {
    private float health;
    private float moral;
    private float defense;
    private float accuracy;
    private float dodge;
    private Point speed;
    public Point currentLocation;
    private Point previousLocation;
    private Building targetLocation;
    private Warrior targetEnemy;
    private Weapon primeWeapon;
    private int thrownWeapon;
    private int vector;
    private boolean shield;
    private int shieldDirection;
    private int loot;
    public boolean inCover;
    public int state;
    private Color color;
    private int size;

    public Warrior(Point currentLocation, Building targetLocation, int size, Color color) {
        Random r = new Random();

        this.currentLocation = currentLocation;
        this.previousLocation = currentLocation;
        this.targetLocation = targetLocation;
        this.color = color;
        this.targetEnemy = null;
        this.loot = 0;
        this.size = size;
        this.speed = new Point(1,1);
        this.thrownWeapon = r.nextInt(6) + 5;
        this.vector = 0;
        this.shield = false;
        this.shieldDirection = -90;
        this.inCover = false;
        this.state = 1;
        this.health = 100;
        this.moral = r.nextInt(21)+40;
        this.defense = r.nextInt(4)+3;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];
    }

    public void draw(Graphics g) {
        g.setColor(Colors.VIKING);
        g.fillOval(currentLocation.x, currentLocation.y, size, size);
    }
}