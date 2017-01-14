package Army;

import Armament.Shield;
import Armament.ThrownWeapon;
import Armament.Weapon;
import Fleet.*;
import Map.*;
import Schemes.Colors;
import Schemes.Weapons;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Colision.Distance.distanceC;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.toRadians;

public class Viking {
    // Stats for battle
    private int health;
    private int moral;
    private int moralThreshold;
    private int defense;
    private int accuracy;
    private int dodge;
    private int loot;
    private int state;      // 0-dead, 1-fight, 2-retreat, 3-looting, 4-inBoat
    private int targeted;

    // Stats for locations and targets
    private Point speed;
    private Point currentLocation;
    private Point previousLocation;
    private Building targetBuilding;
    private Villager targetEnemy;
    private Boat targetBoat;
    private Point currentTarget;
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

    // Constructor
    public Viking(Point location, Terrain map, Village village, Fleet fleet, Building targetBuilding, Color color, int size, ArrayList<SquadVikings> allies) {
        Random r = new Random();
        // Stats for battle
        this.health = 100;
        this.moral = 100;
        this.moralThreshold = r.nextInt(11) + 20;
        this.defense = r.nextInt(4) + 2;
        this.accuracy = r.nextInt(31) + 30;
        this.dodge = r.nextInt(11) + 10;
        this.loot = 0;
        this.state = 1;
        this.targeted = 0;

        // Stats for locations and targets
        this.speed = new Point(1,1);
        this.currentLocation = new Point(location);
        this.previousLocation = new Point(location);
        this.targetBuilding = targetBuilding;
        this.targetEnemy = null;
        this.targetBoat = null;

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

        // Setting targetBoat
        boolean found = false;
        for (Boat i : fleet.getBoats()){
            if (i.getTargetBuilding() == targetBuilding)
                if (i.getSize() > i.getVikings().size()){
                    this.targetBoat = i;
                    i.addWarrior(this);
                    found = true;

                }
            if (found) break;
        }

        //  Setting currentTarget to boat
        this.currentTarget = targetBoat.getCurrentLocation();
        vector();
    }


    // Setters
    public void setEnemies(ArrayList<SquadVillagers> enemies) {
        this.enemies = enemies;
    }

    public void setTargeted() {
        targeted ++;
    }

    public void unsetTargeted() {
        targeted--;
    }

    public void setTargetBuilding(Building targetBuilding) {
        this.targetBuilding = targetBuilding;
    }


    // Getters
    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getState() {
        return state;
    }


    // OTHER FUNCTIONS
    public void updateMoral() {             //// TODO: 14.01.17 make it based on situations and stuff
    }

    private boolean moralCheck(){
        updateMoral();
        return moral > moralThreshold;
    }

    public void estimateState() {
        if (health == 0) state = 0;
        if (!moralCheck()) state = 2;
        else state = 1;
        // state 3 and 4 are forced
    }
    
    private void vector(){
        vector = -(int) (atan2(currentLocation.x - currentTarget.x, currentLocation.y - currentTarget.y)*(180/PI));
    }

    // updating currentTarget based on state
    private void updateCurrentTarget(){
        if (state == 0) {
            currentTarget = null;
            return;
        }
        if (state == 1)
            if (map.getTerrainGrid()[currentLocation.x][currentLocation.y] == Colors.HILLS) {
                currentTarget = targetBoat.getCurrentLocation();
                return;
            }
            else if (targetEnemy != null) {
                currentTarget = targetEnemy.getCurrentLocation();
                return;
            }
            else {
                currentTarget = targetBuilding.getLocation();
                return;
            }
        if (state == 2) {
            currentTarget = targetBoat.getCurrentLocation();
            return;
        }
        if (state == 3) {
            currentTarget = targetBuilding.getLocation();
            return;
        }
        if (state == 4) {
            currentTarget = targetBoat.getTargetLocation();
            return;
        }
    }

    public void findTargetEnemy(){
        if (targetEnemy == null && state == 1){
            if (distanceC(currentLocation.x, targetBuilding.getLocation().x, currentLocation.y, targetBuilding.getLocation().y) < 200) {
            }
        }
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

    public void move() {
        vector();
    }
}