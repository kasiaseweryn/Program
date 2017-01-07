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

        public Villagers(Terrain map, Village village){

                // Initializing
                this.squads = new ArrayList<>();
                this.target = village;
                this.state = 2;
                this.map = map;
                this.village = village;

                // Generating squads
                for (Building i:village.getBuildings()){
                        squads.add(new SquadVillagers(map, village, i));
                }
        }

        public void draw(Graphics g){
                for (SquadVillagers i:squads) i.draw(g);
        }

        public ArrayList<SquadVillagers> getSquads() {
                return squads;
        }
}
