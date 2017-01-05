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
    private Color color;

    public Vikings(Terrain map, Village village, Fleet fleet, Building base){
        // Variables for generation
        Random r = new Random();

        // Initializing
        this.squads = new ArrayList<>();
        this.target = village;
        this.state = 2;
        this.map = map;
        this.village = village;
        this.fleet = fleet;
        this.color = Colors.VIKING;

        // Generating squads
        for (Building i:village.getBuildings()){
            int size = r.nextInt(3) + 8;
            squads.add(new SquadVikings(map, village, fleet, i, base.getLocation(), size, color));
            System.out.println(i.getLocation() + " vikings " + size);
        }
    }


    public void draw(Graphics g){
        for (SquadVikings i:squads) i.draw(g);
    }
}
