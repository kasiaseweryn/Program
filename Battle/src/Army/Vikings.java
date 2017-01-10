package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class Vikings {
    private ArrayList<SquadVikings> squads;
    private Village target;
    private int state;  //0-loss, 1-win, 2-fight
    private Terrain map;
    private Village village;
    private Fleet fleet;
    private ArrayList<SquadVillagers> enemies;

    public Vikings(Terrain map, Village village, Fleet fleet, Building base){

        // Initializing
        this.squads = new ArrayList<>();
        this.target = village;
        this.state = 2;
        this.map = map;
        this.village = village;
        this.fleet = fleet;
        double angle = 2.6;
        double radius = 1.1 * sqrt((base.getWidth()*base.getWidth()) + base.getHeight()*base.getHeight());
        Point location = new Point();

        // Generating squads
        for (Building building:village.getBuildings()){
            angle += 0.5;
            location.x = base.getLocation().x + (int) (radius * cos(angle));
            location.y = base.getLocation().y + (int) (radius * sin(angle));
            squads.add(new SquadVikings(map, village, fleet, building, new Point(location), squads));
        }
    }

    public void setEnemies(ArrayList<SquadVillagers> enemies) {
        this.enemies = enemies;
        for (SquadVikings i:squads){
            i.setEnemies(enemies);
        }
    }

    public ArrayList<SquadVikings> getSquads() {
        return squads;
    }

    public void draw(Graphics g){
        for (SquadVikings i:squads) i.draw(g);
    }
}
