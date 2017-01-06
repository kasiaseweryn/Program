package Army;

import Fleet.Fleet;
import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Vikings {
    private ArrayList<SquadVikings> squads;
    private Village target;
    private int state;  //0-loss, 1-win, 2-fight
    private Terrain map;
    private Village village;
    private Fleet fleet;

    public Vikings(Terrain map, Village village, Fleet fleet, Building base){

        // Initializing
        this.squads = new ArrayList<>();
        this.target = village;
        this.state = 2;
        this.map = map;
        this.village = village;
        this.fleet = fleet;

        // Generating squads
        for (Building building:village.getBuildings()){
            squads.add(new SquadVikings(map, village, fleet, building, base.getLocation()));
        }
    }


    public void draw(Graphics g){
        for (SquadVikings i:squads) i.draw(g);
    }
}
