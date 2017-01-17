package Army;

import Armament.Shield;
import Armament.ThrownWeapon;
import Armament.Weapon;
import Fleet.*;
import Map.*;
import Schemes.*;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Colision.Distance.distanceC;
import static java.lang.Math.*;

public class Viking {
    // Stats for battle
    private int health;
    private int moral;
    private int moralThreshold;
    private int defense;
    private int accuracy;
    private int dodge;
    private int loot;
    private int state;
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
        this.defense = r.nextInt(3) + 1;
        this.accuracy = r.nextInt(31) + 30;
        this.dodge = r.nextInt(21) + 10;
        this.loot = 0;
        this.state = States.FIGHT;
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
        // TODO: 17.01.17
// HERE IS A BUG!!!!!!!!
        // Setting targetBoat
        boolean found = false;
        for (Boat i : fleet.getBoats()){
            if (i.getTargetBuilding() == targetBuilding)
                if (i.getSize() > i.getVikings().size()){
                    this.targetBoat = i;
                    i.addViking(this);
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

    // Getters
    public Point getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getState() {
        return state;
    }


    // TODO: 17.01.17 make other one for retreat ]
    public void setState(int state) {
        if (state == States.DEAD || state == States.INBOAT || state == States.WAITING) return;
        else this.state = state;
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
        // Set dead
        if (health == 0) {
            state = States.DEAD;
            return;
        }

        // Set retreat
        if (!moralCheck()) {
            state = States.RETREAT;
            return;
        }

        // Set looting or fight
        if (state == States.LOOTING) {
            if (targetBuilding.getLoot() == 0 || loot == 3) {
                state = States.FIGHT;
                return;
            }
            else {
                state = States.LOOTING;
                return;
            }
        }

        // Set sailing or waiting on shore
        if (state == States.INBOAT) {
            if (map.getTerrainGrid()[currentLocation.x][currentLocation.y] != Colors.OCEAN) {
                state = States.WAITING;
                return;
            }
            else {
                state = States.INBOAT;
                return;
            }
        }

        // Set waiting
        if (state == States.WAITING) {
            state = States.WAITING;
            return;
        }

        if (state == States.RETREAT) {
            state = States.RETREAT;
            return;
        }

        // Set fight
        else state = States.FIGHT;
    }

    // updating currentTarget based on state
    private void updateCurrentTarget(){
        // DEAD
        if (state == States.DEAD) {
            currentTarget = null;
            return;
        }

        // FIGHTING
        if (state == States.FIGHT){
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
        }

        // RETREAT
        if (state == States.RETREAT) {
            currentTarget = targetBoat.getCurrentLocation();
            return;
        }

        // LOOTING
        if (state == States.LOOTING) {
            currentTarget = targetBuilding.getLocation();
            return;
        }

        // INBOAT
        if (state == States.INBOAT) {
            currentTarget = targetBoat.getTargetLocation();
        }

        // WAITING
        if (state == States.WAITING) {
            currentTarget = targetBuilding.getLocation();
        }
    }

    private double distanceFromTargetBuilding(){
        return distanceC(currentLocation.x, targetBuilding.getLocation().x, currentLocation.y, targetBuilding.getLocation().y);
    }

    private double distanceFromTargetEnemy() {
        return distanceC(currentLocation.x, targetEnemy.getCurrentLocation().x, currentLocation.y, targetEnemy.getCurrentLocation().y);
    }

    private double distanceFromTargetBoat(){
        return distanceC(currentLocation.x, targetBoat.getCurrentLocation().x, currentLocation.y, targetBoat.getCurrentLocation().y);
    }

    // Vector based on current target
    private void vector(){
        vector = -(int) (atan2(currentLocation.x - currentTarget.x, currentLocation.y - currentTarget.y)*(180/PI));
    }

    public void findTargetEnemy() {
        boolean found = false;
        if (state == States.FIGHT && (targetEnemy == null || targetEnemy.getHealth() == 0)) {
            if (targetEnemy != null && targetEnemy.getHealth() == 0) {
                targetEnemy.unsetTargeted();
                targetEnemy = null;
            }
            if (distanceC(currentLocation.x, targetBuilding.getLocation().x, currentLocation.y, targetBuilding.getLocation().y) < 200) {
                for (SquadVillagers i : enemies) {
                    for (Villager j : i.getVillagers()) {
                        if (j.getHealth() > 0) {
                            if (distanceC(targetBuilding.getLocation().x, j.getCurrentLocation().x, targetBuilding.getLocation().y, j.getCurrentLocation().y) < 40) {
                                if (j.getTargeted() < 2) {
                                    targetEnemy = j;
                                    j.setTargeted();
                                    found = true;
                                }
                            }
                        }
                        if (found) return;
                    }
                    if (found) return;
                }
            }
        }
    }

    public void action() {
        findTargetEnemy();
        updateCurrentTarget();
        vector();

        // if dead
        if (state == States.DEAD){
            return;
        }

        // if retreating or being on hills
        if (state == States.RETREAT || map.getTerrainGrid()[currentLocation.x][currentLocation.y] == Colors.HILLS){
            if (distanceFromTargetBoat() < targetBoat.getLength()*2) {
                System.out.println("Getting on boat");
                currentLocation.x = targetBoat.getCurrentLocation().x;
                currentLocation.y = targetBoat.getCurrentLocation().y;
                state = States.INBOAT;
                return;
            }
            else {
                System.out.println("Moving to boat");
                move();
                return;
            }
        }

        // if fighting
        if (state == States.FIGHT){
            if (targetEnemy == null){
                //System.out.println("Moving to building");
                move();
                return;
            }
            else if (distanceFromTargetEnemy() <= size + primeWeapon.getRange()) {
                //System.out.println("Attacking");
                attack();
                return;
            }
            else {
                //System.out.println("Moving to enemy");
                move();
                return;
            }

        }

        // if looting
        if (state == States.LOOTING){
            if (distanceFromTargetBuilding() <= targetBuilding.getHeight()/2 + size && loot < 3){
                loot += targetBuilding.removeLoot();
                //System.out.println("Looting");
                return;
            }
            else {
                //System.out.println("Moving to building");
                move();
                return;
            }
        }

        // if sailing
        if (state == States.INBOAT){
            // get out of boat
            if (currentLocation.x == currentTarget.x && currentLocation.y == currentTarget.y){
                Random r = new Random();
                boolean generated = false, noColision;
                Point location = new Point();
                // generating random point in radius of a boat
                while (!generated) {
                    double angle = toRadians(random() * 360);
                    double radius = r.nextDouble() + r.nextInt((int) (targetBoat.getLength())) + targetBoat.getLength()/2;
                    location.x = currentLocation.x + (int) (radius * cos(angle));
                    location.y = currentLocation.y + (int) (radius * sin(angle));
                    // if in bounds
                    if (location.x - size > 0 && location.y - size > 0 && location.x < map.numRows + size && location.y < map.numCols + size) {
                        // if on land
                        if (checkExit(location)) {
                            // checking for vikings
                            noColision = true;
                            double spread = 1.1;
                            for (SquadVikings j : this.allies) {
                                for (Viking k : j.getVikings()) {
                                    double distance = distanceC(location.x, k.getCurrentLocation().x, location.y, k.getCurrentLocation().y);
                                    if (distance < size * spread) {
                                        noColision = false;
                                    }
                                }
                            }
                            if (noColision) {
                                //System.out.println("Getting of boat");
                                currentLocation.x = location.x;
                                currentLocation.y = location.y;
                                state = States.WAITING;
                                generated = true;
                            }
                        }
                    }
                }
            }
            //System.out.println("Sailing");
        }
    }

    private void attack() {
        // TODO: 16.01.17 Make attack system based on DH RP
        Random r = new Random();
        if (r.nextInt(101) < accuracy + primeWeapon.getAccuracy()){
            if (r.nextInt(101) >= targetEnemy.getDodge())
                targetEnemy.damage(r.nextInt(10) + primeWeapon.getDamage(), primeWeapon.getPenetration());
        }
    }

    public boolean onLand() {
        estimateState();
        return (state == States.WAITING);
    }

    // MOVING TEMP!!!
    public void move(){
//        if (vector < 45 && vector >= -45) Up();
//        if (vector < 135 && vector >= 45) Right();
//        if (vector < -135 && vector >= 135) Down();
//        if (vector < -45 && vector >= -135) Left();
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

    private boolean checkB(){
        for (Building i : village.getBuildings()){
            if (distanceFromTargetBuilding() < size/2 + targetBuilding.getHeight()/2 ) return false;
        }
        return true;
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

    private boolean check(){
        return (checkB() && checkM());
    }

    private boolean checkExit( Point location){
        double angle2 = 0;
        while (angle2 < 6.3) {
            if (map.getTerrainGrid()[location.x + (int) (size/2 * cos(angle2))][location.y + (int) (size/2 * sin(angle2))] == Colors.OCEAN) {
                return false;
            }
            angle2 += 0.3925;
        }
        return true;
    }

    private void Left() {
        //System.out.println("L");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveLeft();
//        if (!checkB()) {
//            moveRight();
//            moveRight();
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveDown();
//        if (!checkB()) {
//            moveUp();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveUp();
//        if (!checkB()) {
//            moveDown();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();
    }

    private void Right() {
        //System.out.println("R");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveRight();
//        if (!checkB()) {
//            moveLeft();
//            moveLeft();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveDown();
//        if (!checkB()) {
//            moveDown();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveUp();
//        if (!checkB()) {
//            moveUp();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

    }

    private void Up() {
        //System.out.println("U");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveUp();
//        if (!checkB()) {
//            moveDown();
//            moveDown();
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveDown();

        moveRight();
//        if (!checkB()) {
//            moveLeft();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

        moveLeft();
//        if (!checkB()) {
//            moveRight();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();
    }

    private void Down() {
        //System.out.println("D");
        int zx = currentLocation.x;
        int zy = currentLocation.y;
        moveDown();
//        if (!checkB()) {
//            moveUp();
//            moveUp();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveUp();

        moveLeft();
//        if (!checkB()) {
//            moveRight();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveRight();

        moveRight();
//        if (!checkB()) {
//            moveLeft();
//            return;
//        }
        if (checkM()) {
            previousLocation.x = zx;
            previousLocation.y = zy;
            return;
        }
        moveLeft();

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