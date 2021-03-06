package Army;

import Armament.ThrownWeapon;
import Armament.Weapon;
import Fleet.Fleet;
import Map.*;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.toRadians;

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
    private ArrayList<ThrownWeapon> thrownWeapons;
    private int vector;
    private boolean shield;
    private int shieldDirection;
    private int loot;
    private int state;
    private int size;
    private Color color;
    private Terrain map;
    private Village village;
    private Fleet fleet;

    public Viking( Point location, Terrain map, Village village, Fleet fleet, Building targetLocation, Color color, int size) {
        Random r = new Random();

        this.map = map;
        this.village = village;
        this.fleet = fleet;
        this.currentLocation = new Point(location);
        this.previousLocation = new Point(location);
        this.targetLocation = targetLocation;
        this.targetEnemy = new Villager();
        this.loot = 0;
        this.size = size;
        this.speed = new Point(1,1);
        this.thrownWeapons = new ArrayList<>();
        for (int i = 0; i < r.nextInt(6) + 5; i++) thrownWeapons.add(new ThrownWeapon());
        this.vector = 0;
        if ( r.nextInt(101) > 50 ) this.shield = true;
        else this.shield = false;
        this.shieldDirection = - (r.nextInt(61) + 30);
        this.state = 1;
        this.health = 100;
        this.color = color;


        this.dodge = r.nextInt(11) + 10;
        this.moral = r.nextInt(21) + 40;
        this.defense = r.nextInt(4) + 2;
        this.accuracy = r.nextInt(31) + 30;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];


    }

    public Viking() {

    }
    public void move()
    {
    	if (currentLocation.x != targetLocation.getLocation().x || currentLocation.y != targetLocation.getLocation().y) {
            if (targetLocation.getLocation().x >= targetLocation.getLocation().y) {
                if (currentLocation.y - targetLocation.getLocation().y > 0) currentLocation.y++;
                else {
                    if (currentLocation.y == targetLocation.getLocation().y && currentLocation.x > targetLocation.getLocation().x) currentLocation.x--;
                    else currentLocation.x++;
                }
                if (currentLocation.y - targetLocation.getLocation().y < 0) currentLocation.y--;;
            }
            if (targetLocation.getLocation().x < targetLocation.getLocation().y){
                if (currentLocation.x - targetLocation.getLocation().x > 0) currentLocation.x--;
                else {
                    if (currentLocation.x == targetLocation.getLocation().x && currentLocation.y > currentLocation.x) currentLocation.y++ ;
                    else currentLocation.y--;
                }
                if (currentLocation.x - targetLocation.getLocation().x < 0) currentLocation.x++ ;
            }
        }
    }
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Viking
        g2d.setColor(color);
        g2d.rotate(toRadians(vector), currentLocation.x, currentLocation.y);
        g2d.fillOval(currentLocation.x - size/2, currentLocation.y - size/2, size, size);
        // Center
        g2d.setColor(Colors.LOCATION);
        g2d.fillRect(currentLocation.x, currentLocation.y, 1, 1);
        // Weapon
        g2d.setColor(Colors.WEAPON);
        g2d.rotate(toRadians(90), currentLocation.x, currentLocation.y);
        g2d.fillRect((int) (currentLocation.x - size/1.4), (int) (currentLocation.y - size/1.8), (int) (size/1.1), size/5);
        // Shield
        if (shield) {
            g2d.setColor(Colors.SHIELD);
            g2d.rotate(toRadians(shieldDirection - 90 - vector), currentLocation.x, currentLocation.y);
            g2d.fillRect(currentLocation.x - size / 4, currentLocation.y - size / 2, size / 2, size / 4);
        }
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }
}