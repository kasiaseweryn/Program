package Army;

import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Villagers {
    private ArrayList<SquadVillagers> squads;
    private Village village;
    private int state;  //0-loss, 1-win, 2-fight
    private Terrain map;
    private ArrayList<SquadVikings> enemies;

    public Villagers(Terrain map, Village village){
        // Initializing
        this.squads = new ArrayList<>();
        this.village = village;
        this.state = 2;
        this.map = map;

        // Generating squads
        for (Building i:village.getBuildings()){
            squads.add(new SquadVillagers(map, village, i, squads));
        }

    }

    // Estimating and setting state
    public void estimateState(){
        // Update state of squads
        for (SquadVillagers i : squads) i.estimateState();

        int lost = 0, looted = 0, defeated = 0;
        // Losing statement
        for (SquadVillagers i : squads) {
            if (i.getState() == 0) lost ++;
        }
        for (Building i : village.getBuildings()) {
            if (i.getLoot() == 0) looted ++;
        }
        if (lost == squads.size() || looted == village.getBuildings().size()) {
            state = 0;
            return;
        }

        // Winning statement
        for (SquadVikings i : enemies) {
            if (i.getState() == 0 || i.getState() == 2) defeated ++;
        }
        if (defeated == enemies.size()) {
            state = 1;
            return;
        }

        // Else figth
        state = 2;
        return;
    }

    // Actions based on state
    public void action(){
        for (SquadVillagers i : squads) {
            if (state == 0) i.surrender();
            if (state == 1) i.celebrate();
            if (state == 2) i.action();
        }
    }

        // Setters
        public void setEnemies(ArrayList<SquadVikings> enemies) {
            this.enemies = enemies;
            for (SquadVillagers i:squads){
                i.setEnemies(enemies);
            }
        }

        // Getters
        public ArrayList<SquadVillagers> getSquads() {
            return squads;
        }

        public void draw(Graphics g){
            for (SquadVillagers i:squads) i.draw(g);
        }
}
