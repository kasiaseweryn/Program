package Army;

import Fleet.Fleet;
import Map.*;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Vikings {
    private ArrayList<SquadVikings> squads;
    private int state;  //0-loss, 1-win, 2-fight

    private Terrain map;
    private Village village;
    private Fleet fleet;

    private ArrayList<SquadVillagers> enemies;

    public Vikings(Terrain map, Village village, Fleet fleet, Building base){
        // Variables for generating
        double angle = 2.2;
        double radius = 1.1 * sqrt((base.getWidth()*base.getWidth()) + base.getHeight()*base.getHeight());
        Point location = new Point();

        // Initializing
        this.squads = new ArrayList<>();
        this.state = 2;

        this.map = map;
        this.village = village;
        this.fleet = fleet;

        // Generating squads
        for (Building building:village.getBuildings()){
            angle += 0.5;
            location.x = base.getLocation().x + (int) (radius * cos(angle));
            location.y = base.getLocation().y + (int) (radius * sin(angle));
            squads.add(new SquadVikings(map, village, fleet, building, new Point(location), squads));
        }
    }

    // Setters
    public void setEnemies(ArrayList<SquadVillagers> enemies) {
        this.enemies = enemies;
        for (SquadVikings i:squads){
            i.setEnemies(enemies);
        }
    }

    // Getters
    public ArrayList<SquadVikings> getSquads() {
        return squads;
    }

    // OTHER FUNCTIONS
    public void estimateState(){
        int lost = 0, looted = 0, defeated = 0;
        // Losing statement
        for (SquadVikings i : squads) {
            if (i.getState() == 0 || i.getState() == 2) lost ++;
        }
        if (lost == squads.size()) {
            state = 0;
            return;
        }

        // Winning statement
        for (Building i : village.getBuildings()) {
            if (i.getLoot() == 0) looted ++;
        }
        for (SquadVillagers i : enemies) {
            if (i.getState() == 0) defeated ++;
        }
        if (looted == village.getBuildings().size() || defeated == enemies.size()) {
            state = 1;
            return;
        }

        // Else figth
        state = 2;
        return;
    }

    // Actions based on state
    public void action(){
        for (SquadVikings i : squads) {
            if (state == 0) i.surrender();
            if (state == 1) i.celebrate();
            if (state == 2) i.action();
        }
    }

    // Drawing
    public void draw(Graphics g){
        for (SquadVikings i:squads) i.draw(g);
    }
}
