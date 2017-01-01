package Battle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fleet {
    public ArrayList<Boat> boats;
    public int state;
    private ArrayList<Point> coastH;
    private ArrayList<Point> coastP;

    // Constructor
    public Fleet(Map mapa, Village village) {
        // Variables for generating
        Random r = new Random();
        int width = mapa.numCols /150;
        int length = 5*mapa.numRows /150;
        int amount = village.buildings.size();
        int generated = 0;

        // Initialazing
        boats = new ArrayList<>();
        coastH = new ArrayList<>();
        coastP = new ArrayList<>();
        state = 0;

        // Generating coasts
        for (int i = 0; i < mapa.numRows; i++) {
            for (int j = 0; j < mapa.numCols; j++) {
                if (mapa.terrainGrid[i][j] == Colors.HILLS) {
                    if ((mapa.terrainGrid[i - 1][j-1] == Colors.OCEAN && mapa.terrainGrid[i][j - 1] == Colors.OCEAN)) {
                        Point point = new Point(i, j);
                        coastH.add(point);
                    }
                }
                if (mapa.terrainGrid[i][j] == Colors.PLAINS) {
                    if ((mapa.terrainGrid[i + 1][j] == Colors.OCEAN || mapa.terrainGrid[i][j + 1] == Colors.OCEAN)) {
                        Point point = new Point(i, j);
                        coastP.add(point);
                    }
                }
            }
        }
        // Generating boats  -- have to improve!
        int size = coastH.size(), x ,y, choosen = 0;
        boolean noColision;

        while (generated < amount && choosen < size) {
            x = coastH.get(choosen).x;
            y = coastH.get(choosen).y;
            noColision = true;
            for(Boat i:boats){
                double distance = distance( (x + (width/2)), (i.current_locationX + (i.width/2)), (y + (length/2)), (i.current_locationY + (i.length/2)) );
                if (distance < length*1.2) noColision = false;
            }
            if (noColision){
                System.out.println(generated + ":" + choosen);
                int cordX = x - width;
                int cordY = y - length;
                boats.add(new Boat(cordX, cordY, width, length, 5, coastH, coastP));
                generated++;
            }
            choosen++;
        }
    }

    private double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    // Drawing
    public void draw(Graphics g){
        for (Boat i:boats) i.draw(g);
        // to see the possible points of generation
        g.setColor(Color.MAGENTA);
        for (Point i:coastH) g.fillRect(i.x, i.y, 1, 1);
        g.setColor(Color.ORANGE);
        for (Point i:coastP) g.fillRect(i.x, i.y, 1, 1);
    }
}