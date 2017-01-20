package Army;

import Armament.Weapon;
import Map.Building;
import Map.Terrain;
import Map.Village;
import Schemes.Colors;
import Schemes.Weapons;
import static Colision.Distance.distanceC;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.toRadians;

public class Villager {
    private int health;
    private int moral;
    private int defense;
    private int accuracy;
    private int dodge;
    private Point speed;
    public Point currentLocation;
    public Point previousLocation;
    private Building targetLocation;
    private Viking targetEnemy;
    private Weapon primeWeapon;
    private int vector;
    private boolean shield;
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
        this.targetEnemy = new Viking();
        this.loot = 0;
        this.size = size;
        this.speed = new Point(1,1);
        this.vector = 0;
        if ( r.nextInt(101) > 50 ) this.shield = true;
        else this.shield = false;
        this.shieldDirection = - (r.nextInt(61) + 30);
        this.state = 1;
        this.health = 100;
        this.color = color;
        this.dodge = r.nextInt(11) + 10;
        this.accuracy = r.nextInt(21) + 50;
        this.moral = r.nextInt(21) + 40;
        this.defense = r.nextInt(3) + 2;
        this.primeWeapon = Weapons.ARSENAL[r.nextInt(Weapons.ARSENAL.length)];
        this.previousLocation.x = currentLocation.x + r.nextInt(6)-3; 
        this.previousLocation.y = currentLocation.y + r.nextInt(6)-3; 
    }

    public Villager() {

    }

    public double distance(double x, double y){return Math.sqrt(Math.pow((currentLocation.getX() - x),2)+Math.pow((currentLocation.getY() - y),2));}//ok, jest
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Villager
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
    private boolean checkM(){
        double interval = 3.5;
        if(currentLocation.x == previousLocation.x && currentLocation.y == previousLocation.y) return false;
        if(currentLocation.x < size/2 || currentLocation.y < size/2 || currentLocation.x > map.numRows - size/2 || currentLocation.y > map.numCols - size/2) return false;  //granice mapy
        if(map.getTerrainGrid()[(int) (currentLocation.x - size/2/interval)][currentLocation.y] == Colors.OCEAN || map.getTerrainGrid()[currentLocation.x][(int) (currentLocation.y - size /2 /interval)] == Colors.OCEAN || map.getTerrainGrid()[(int) (currentLocation.x + size /2 / interval)][currentLocation.y] == Colors.OCEAN || map.getTerrainGrid()[currentLocation.x][(int) (currentLocation.y + size /2 /interval)] == Colors.OCEAN) return false; //sprawdzanie, czy villagers nie wpada do oceany 
        return true;  //ok
    }
    public boolean checkB(){
        for (Building i : village.getBuildings()){
            if (distanceC(currentLocation.x, i.getLocation().x, currentLocation.y, i.getLocation().y) < size/2 + ( i.getHeight()/2)*Math.sqrt(2) ) return false;
        }
        return true;
    }
    public void moveDefence()
    {
    	if (currentLocation.x != targetLocation.getLocation().x || currentLocation.y != targetLocation.getLocation().y)
    	{
    		if ((currentLocation.x < targetLocation.getLocation().x) && ((currentLocation.y < targetLocation.getLocation().y)))
    		{
    			currentLocation.x++; currentLocation.y++;
    		}
    		else
    	    if ((currentLocation.x < targetLocation.getLocation().x) && ((currentLocation.y > targetLocation.getLocation().y)))
    	    {
    	    	currentLocation.x++; currentLocation.y--;
    	    }
            else
            if ((currentLocation.x > targetLocation.getLocation().x) && ((currentLocation.y < targetLocation.getLocation().y)))
            {
            	currentLocation.x--; currentLocation.y++;	
            }
            else
        	if ((currentLocation.x > targetLocation.getLocation().x) && ((currentLocation.y > targetLocation.getLocation().y)))
        	{
        		currentLocation.x--; currentLocation.y--;
        	}
            else    	
            if ((currentLocation.x < targetLocation.getLocation().x) && ((currentLocation.y == targetLocation.getLocation().y)))
            {
            	currentLocation.x++;
            }
        	else
        	if ((currentLocation.x == targetLocation.getLocation().x) && ((currentLocation.y < targetLocation.getLocation().y)))
        	{
        		currentLocation.y++;
        	}
            else
            if ((currentLocation.x == targetLocation.getLocation().x) && ((currentLocation.y > targetLocation.getLocation().y)))
            {
            	currentLocation.y--;
            }
            else
            if ((currentLocation.x > targetLocation.getLocation().x) && ((currentLocation.y == targetLocation.getLocation().y)))
            {
            	currentLocation.x--;
            	
            }
    		
    	}
    } 
    public void move(Point t)
    {
    	if (currentLocation.x != t.getLocation().x || currentLocation.y != t.getLocation().y)
    	{
    		if ((currentLocation.x < t.getLocation().x) && ((currentLocation.y < t.getLocation().y)))
    		{
    			currentLocation.x++; currentLocation.y++;
    		}
    		else
    	    if ((currentLocation.x < t.getLocation().x) && ((currentLocation.y > t.getLocation().y)))
    	    {
    	    	currentLocation.x++; currentLocation.y--;
    	    }
            else
            if ((currentLocation.x > t.getLocation().x) && ((currentLocation.y < t.getLocation().y)))
            {
            	currentLocation.x--; currentLocation.y++;	
            }
            else
        	if ((currentLocation.x > t.getLocation().x) && ((currentLocation.y > t.getLocation().y)))
        	{
        		currentLocation.x--; currentLocation.y--;
        	}
            else    	
            if ((currentLocation.x < t.getLocation().x) && ((currentLocation.y == t.getLocation().y)))
            {
            	currentLocation.x++;
            }
        	else
        	if ((currentLocation.x == t.getLocation().x) && ((currentLocation.y < t.getLocation().y)))
        	{
        		currentLocation.y++;
        	}
            else
            if ((currentLocation.x == t.getLocation().x) && ((currentLocation.y > t.getLocation().y)))
            {
            	currentLocation.y--;
            }
            else
            if ((currentLocation.x > t.getLocation().x) && ((currentLocation.y == t.getLocation().y)))
            {
            	currentLocation.x--;
            	
            }
    		
    	}
    }      
    public void watchMove()
    {
    	   Point temp;
    	   temp = currentLocation;
           currentLocation = previousLocation;
           previousLocation = temp;
    }
    public void defenceMove()
    {
     	if (distanceC(targetEnemy.getCurrentLocation().getX(), targetLocation.getLocation().getX(), targetEnemy.getCurrentLocation().getY(), targetLocation.getLocation().getY()) < 70 )
        	 move(targetEnemy.getCurrentLocation());
    }
    public void retreatMove()
    {
    	move(targetLocation.getLocation());
    }
    
    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getSize() {
        return size;
    }
}
