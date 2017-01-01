package Battle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Village {
    public ArrayList<Building> buildings;
    public int[] center;

    // Constructor
    public Village(Map mapa) {
        // Variables for generating
        Random r = new Random();
        int size = mapa.numCols /30;
        int border = mapa.numCols /50;
        int max1 = mapa.numRows /17;
        int min1 = mapa.numRows /27;
        int max2 = mapa.numCols /20;
        int min2 = mapa.numCols /25;
        boolean inBound;

        //Initializing
        buildings = new ArrayList<>();
        center = new int[] {0, 0};

        // Genereating village
        for (int i = 0; i < mapa.numRows; i++) {
            for (int j = 0; j < mapa.numCols; j++) {
                if (mapa.terrainGrid[i][j] == Colors.CITY && i > border && j > border) {
                    inBound = true;
                    for (int x = i - border; x < i + border + size; x++) {
                        for (int y = j - border; y < j + border + size; y++) {
                            if (mapa.terrainGrid[x][y] != Colors.CITY) inBound = false;
                        }
                    }
                    if (inBound) {
                        buildings.add(new Building(i, j, size, size, r.nextInt(6-3+1)+3));
                        i += r.nextInt(max1 - min1 + 1) + min1;
                        j += r.nextInt(max2 - min2 + 1) + min2;
                    }
                }
            }
        }

        // Generating center
        for (int i = 0; i < buildings.size(); i ++){
            center[0] += buildings.get(i).x + size/2;
            center[1] += buildings.get(i).y + size/2;
        }
        center[0] = center[0]/buildings.size();
        center[1] = center[1]/buildings.size();
    }

    // Drawing
    public void draw(Graphics g) {
        for (Building i:buildings) i.draw(g);
        g.setColor(Color.BLUE);
        g.drawOval(center[0] - 6, center[1] - 6, 6, 6);
    }
}