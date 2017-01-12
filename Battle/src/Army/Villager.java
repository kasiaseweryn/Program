package Army;

import Armament.Shield;
import Armament.Weapon;
import Map.Building;
import Map.Terrain;
import Map.Village;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.toRadians;

public class Villager {
    private int health;
    private int moral;
    private int defense;
    private int accuracy;
    private int dodge;
    private Point speed;
    private Point currentLocation;
    private Point previousLocation;
    private Building targetLocation;
    private Viking targetEnemy;
    private Weapon primeWeapon;
    private int vector;
    private Shield shield;
    private int shieldDirection;
    private int loot;
    private int state;
    private int size;
    private Color color;
    private Terrain map;
    private Village village;

    public Villager(Point location, Terrain map, Village village, Building target, Color color, int size){
        // Variables for generating
        Random r = new Random();

        // Initializing
        this.map = map;
        this.village = village;
        this.currentLocation = new Point(location);
        this.previousLocation = new Point(location);
        this.targetLocation = target;
        this.targetEnemy = null;
        this.loot = 0;
        this.size = size;
        this.speed = new Point(1,1);
        this.vector();
        if ( r.nextInt(101) > 50 ) this.shield = new Shield();
        else this.shield = null;
        this.shieldDirection = - (r.nextInt(61) + 30);
        this.state = 1;
        this.health = 100;
        this.color = color;

        this.dodge = r.nextInt(11) + 10;
        this.accuracy = r.nextInt(21) + 50;
        this.moral = r.nextInt(21) + 40;
        this.defense = r.nextInt(3) + 2;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];
    }

    private void vector(){
        vector = -(int) (atan2(currentLocation.x - targetLocation.getLocation().x, currentLocation.y - targetLocation.getLocation().y)*(180/PI));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Villager
        g2d.setColor(color);
        g2d.rotate(toRadians(vector), currentLocation.x, currentLocation.y);
        g2d.fillOval(currentLocation.x - size/2, currentLocation.y - size/2, size, size);
        // Weapon
        primeWeapon.draw(g, currentLocation, size, vector);
        // Shield
        if (shield != null) shield.draw(g, currentLocation, size, vector + shieldDirection);
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getSize() {
        return size;
    }
}
