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
import static java.lang.Math.*;
import static java.lang.Math.sin;

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
    private int speed;
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
        this.speed = 1;
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
// HERE IS A BUG!!!!!!!!
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
// HERE IS A BUG!!!!!!!!
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

    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }


    // Getters
    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getState() {
        return state;
    }

    public int getHealth() {
        return health;
    }

    public int getLoot() {
        return loot;
    }


    // OTHER FUNCTIONS
    public void updateMoral() {
        // TODO: 14.01.17 make it based on situations and stuff
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

    private double distanceFromCurrentTarget(){
        return distanceC(currentLocation.x, targetBoat.getCurrentLocation().x, currentLocation.y, targetBoat.getCurrentLocation().y);
    }
    // Vector based on current target
    private void vector(){
        vector = -(int) (atan2(currentLocation.x - currentTarget.x, currentLocation.y - currentTarget.y)*(180/PI));
    }

    public void findTargetEnemy(){
        if (targetEnemy == null && state == 1){
            if (distanceC(currentLocation.x, targetBuilding.getLocation().x, currentLocation.y, targetBuilding.getLocation().y) < 50) {
            // TODO: 16.01.17 make search in certain radius of a building and pick the closest target not targeted by more that one
            }
        }
    }

    public void action() {
        //findTargetEnemy();
        updateCurrentTarget();
        vector();

        // if dead
        if (state == 0){
            return;
        }

        // if retreating or being on hills
        if (state == 2 || map.getTerrainGrid()[currentLocation.x][currentLocation.y] == Colors.HILLS){
            if (distanceFromCurrentTarget() < targetBoat.getLength()*2) {
                currentLocation = targetBoat.getCurrentLocation();
                state = 4;
                //System.out.println("Get on boat");
                return;
            }
            else {
                move();
                //System.out.println("Move to boat");
                return;
            }
        }

        // if fighting
        if (state == 1){
            if (currentTarget != null)
                if (distanceFromCurrentTarget() <= primeWeapon.getRange()) {
                    attack();
                    return;
                }
            else {
                    move();
                    //System.out.println("Move");
                    return;
                }
        }

        // if looting
        if (state == 3){
            if (distanceFromCurrentTarget() <= targetBuilding.getHeight()/2 && loot < 3){
                loot += targetBuilding.getLoot();
                return;
            }
            else {
                move();
                return;
            }
        }

        // if sailing
        if (state == 4){
            if (currentLocation == currentTarget){
                // TODO: 16.01.17 find a place to get of boat
            }
        }
    }

    private void attack() {
        // TODO: 16.01.17 Make attack system based on DH RP
    }

    // temporary because we have to make tests
    public void move(){
        if (vector < 22.5 && vector >= -22.5) {
            moveUp();
            if (checkM()) return;
            else moveDown();
        }
        if (vector < 67.5 && vector >= 22.5) {
            moveUpRight();
            if (checkM()) return;
            else moveDownLeft();
        }
        if (vector < 112.5 && vector >= 67.5) {
            moveRight();
            if (checkM()) return;
            else moveLeft();
        }
        if (vector < 157.5 && vector >= 112.5) {
            moveDownRight();
            if (checkM()) return;
            else moveUpLeft();
        }
        if (vector < -157.5 && vector >= 157.5) {
            moveDown();
            if (checkM()) return;
            else moveUp();
        }
        if (vector < -112.5 && vector >= -157.5) {
            moveDownLeft();
            if (checkM()) return;
            else moveUpRight();
        }
        if (vector < -67.5 && vector >= -112.5) {
            moveLeft();
            if (checkM()) return;
            else moveRight();
        }
        if (vector < -22.5 && vector >= -67.5) {
            moveUpLeft();
            if (checkM()) return;
            else moveDownRight();
        }
    }

    private boolean checkM(){
        // Is in previous location
        if(currentLocation.x == previousLocation.x && currentLocation.y == previousLocation.y) return false;
        // Is outside the border
        if(currentLocation.x < size || currentLocation.y < size || currentLocation.x > map.numRows - size || currentLocation.y > map.numCols - size) return false;
        // Is on the land
        double angle2 = 0;
        while (angle2 < 6.3) {
            if (map.getTerrainGrid()[currentLocation.x + (int) (size/2 * cos(angle2))][currentLocation.y + (int) (size/2 * sin(angle2))] == Colors.OCEAN) {
                return false;
            }
            angle2 += 0.3925;
        }
        return true;
    }

    private void moveUp(){
        currentLocation.y -= speed;
    }

    private void moveDown(){
        currentLocation.y += speed;
    }

    private void moveRight(){
        currentLocation.x += speed;
    }

    private void moveLeft(){
        currentLocation.x -= speed;
    }

    private void moveUpRight() {
        currentLocation.x += speed;
        currentLocation.y -= speed;
    }

    private void moveUpLeft(){
        currentLocation.x -= speed;
        currentLocation.y -= speed;
    }

    private void moveDownRight(){
        currentLocation.x += speed;
        currentLocation.y += speed;
    }

    private void moveDownLeft(){
        currentLocation.x -= speed;
        currentLocation.y += speed;
    }


    // Drawing
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Viking
        g2d.setColor(color);
        g2d.rotate(toRadians(vector), currentLocation.x, currentLocation.y);
        g2d.fillOval(currentLocation.x - size / 2, currentLocation.y - size / 2, size, size);
        // Weapon
        primeWeapon.draw(g, currentLocation, size, vector);
        // Shield
        if (shield != null) shield.draw(g, currentLocation, size, vector + shieldDirection);
    }
}