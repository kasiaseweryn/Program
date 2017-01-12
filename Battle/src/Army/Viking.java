package Army;

import Armament.Shield;
import Armament.ThrownWeapon;
import Armament.Weapon;
import Fleet.Fleet;
import Map.*;
import Schemes.Weapons;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.toRadians;

public class Viking {
    // Stats for battle
    private int health;
    private int moral;
    private int defense;
    private int accuracy;
    private int dodge;
    private int loot;
    private int state;

    // Stats for locations and targets
    private Point speed;
    private Point currentLocation;
    private Point previousLocation;
    private Building targetLocation;
    private Villager targetEnemy;
    private int vector;

    // Stats for armament
    private Weapon primeWeapon;
    private ArrayList<ThrownWeapon> thrownWeapons;
    private Shield shield;
    private int shieldDirection;

    // Drawing
    private int size;
    private Color color;

    // Map information
    private Terrain map;
    private Village village;
    private Fleet fleet;

    // Other agents
    private ArrayList<SquadVikings> allies;
    private ArrayList<SquadVillagers> enemies;

    public Viking( Point location, Terrain map, Village village, Fleet fleet, Building targetLocation, Color color, int size, ArrayList<SquadVikings> allies) {
        Random r = new Random();
        // Stats for battle
        this.health = 100;
        this.moral = r.nextInt(21) + 40;
        this.defense = r.nextInt(4) + 2;
        this.accuracy = r.nextInt(31) + 30;
        this.dodge = r.nextInt(11) + 10;
        this.loot = 0;
        this.state = 1;

        // Stats for locations and targets
        this.speed = new Point(1,1);
        this.currentLocation = new Point(location);
        this.previousLocation = new Point(location);
        this.targetLocation = targetLocation;
        this.targetEnemy = null;
        this.vector();

        // Stats for armament
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];
        this.thrownWeapons = new ArrayList<>();
        for (int i = 0; i < r.nextInt(6) + 5; i++) thrownWeapons.add(new ThrownWeapon());
        if ( r.nextInt(101) > 50 ) this.shield = new Shield();
        else this.shield = null;
        this.shieldDirection = - (r.nextInt(61) + 30);

        // Drawing
        this.size = size;
        this.color = color;

        // Map information
        this.map = map;
        this.village = village;
        this.fleet = fleet;

        // Other agents
        this.allies = allies;
    }

    // Setters
    public void setEnemies(ArrayList<SquadVillagers> enemies) {
        this.enemies = enemies;
    }

    // Getters
    public Point getCurrentLocation() {
        return currentLocation;
    }

    // OTHER FUNCTIONS
    private void vector(){
        vector = -(int) (atan2(currentLocation.x - targetLocation.getLocation().x, currentLocation.y - targetLocation.getLocation().y)*(180/PI));
    }

    // Drawing
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Viking
        g2d.setColor(color);
        g2d.rotate(toRadians(vector), currentLocation.x, currentLocation.y);
        g2d.fillOval(currentLocation.x - size/2, currentLocation.y - size/2, size, size);
        // Weapon
        primeWeapon.draw(g,currentLocation, size, vector);
        // Shield
        if (shield != null) shield.draw(g, currentLocation, size, vector + shieldDirection);
    }
}