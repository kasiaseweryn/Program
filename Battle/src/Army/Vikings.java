package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.States;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Vikings {
    private ArrayList<SquadVikings> squads;
    private int state;                          //0-loss, 1-win, 2-fight, 3-ended looting

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
        this.state = States.FIGHT;

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
        // Update state of squads
        for (SquadVikings i : squads) {
            i.estimateState();
            System.out.println(i.getState());
        }

        int lost = 0, retreated = 0, looted = 0, defeated = 0;

        // Losing statement
        for (SquadVikings i : squads) {
            if (i.getState() == States.DEAD) lost ++;
            if (i.getState() == States.RETREAT) retreated++;
        }
        if (lost == squads.size()) {
            state = States.LOSS;
            return;
        }

        // Fight or loss
        if (retreated != 0 && lost + retreated == squads.size()){
            int size = 0, alive = 0;
            for (SquadVikings i : squads) {
                size += i.getSize();
                if (i.getState() == States.RETREAT) {
                    for (Viking j : i.getVikings()) {
                        if (j.getState() != States.DEAD) alive++;
                    }
                }
            }
            if (alive < size/2){
                state = States.LOSS;
                return;
            }
            // TODO: 16.01.17 make else to set that after regrouping they attack
        }

        // Winning statement
        for (Building i : village.getBuildings()) if (i.getLoot() == 0) looted ++;
        // todo check if vikings are away from village
        for (SquadVillagers i : enemies) if (i.getState() == States.DEAD) defeated ++;
        if (looted == village.getBuildings().size() || defeated == enemies.size()) {
            state = States.WIN;
            return;
        }

        // Else figth
        state = States.FIGHT;
    }

    // Actions based on state
    public void action() {
        estimateState();
        switch (state){
            case States.LOSS : fleet.returnToBase();
                break;
            case States.WIN : fleet.returnToBase();
                for (SquadVikings i : squads) i.setState(States.RETREAT);
                break;
            case States.FIGHT :
                break;
        }
        fleet.action();
        for (SquadVikings i : squads) i.action();
    }

    // Drawing
    public void draw(Graphics g){
        for (SquadVikings i:squads) i.draw(g);
    }

    public int getState() {
        return state;
    }
}
