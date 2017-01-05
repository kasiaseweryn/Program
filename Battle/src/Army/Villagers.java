package Army;

import Map.*;
import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Villagers {
        private ArrayList<SquadVillagers> squads;
        private Village target;
        private int state;  //0-loss, 1-win, 2-fight
        private Terrain map;
        private Village village;
        private Color color;

        public Villagers(Terrain map, Village village, Point center){
                // Variables for generation
                Random r = new Random();

                // Initializing
                this.squads = new ArrayList<>();
                this.target = village;
                this.state = 2;
                this.map = map;
                this.village = village;
                this.color = Colors.VILLAGER;

                // Generating squads
                for (Building i:village.getBuildings()){
                        int size = r.nextInt(3) + 8;
                        squads.add(new SquadVillagers(map, village, i, center, size, color));
                        System.out.println(i.getLocation() + " villagers " + size);
                }
        }

        public void draw(Graphics g){
                for (SquadVillagers i:squads) i.draw(g);
        }
}
