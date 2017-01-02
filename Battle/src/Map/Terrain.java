package Map;

import Schemes.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import static Colision.Distance.*;

public class Terrain {
    // Number of seeds and size
    private int seeds;
    public int numRows;
    public int numCols;
//  public static final int PREFERRED_GRID_SIZE_PIXELS = 1;  may be useful in future
    // Grid of terrain
    public Color[][] terrainGrid;
    public ArrayList<Point> coastH;
    public ArrayList<Point> coastP;

    // Constructor
    public Terrain(int rows, int cols, int seed){
        // Initializing
        this.numRows = rows;
        this.numCols = cols;
        this.seeds = seed;
        this.terrainGrid = new Color[numRows][numCols];
        this.coastH = new ArrayList<>();
        this.coastP = new ArrayList<>();

        // Local variables
        Random r = new Random();
        int n = 0;
        int[] px = new int[seeds];
        int[] py = new int[seeds];
        Color[] color = new Color[seeds];

        // Generate seeds
        for (int i = 0; i < seeds; i++) {
            int x = r.nextInt(numRows);
            int y = r.nextInt(numCols);
            px[i] = x;
            py[i] = y;
            if (x > (0.1* numRows) && x < (0.4* numRows) && y > (0.1* numCols) && y < (0.4* numCols) ) {
                color[i] = Colors.CITY;
            }
            else if ((distanceC(numRows,x,numCols,y) < numCols/3.5)) {
                color[i] = Colors.HILLS;
                }
            else if ((x > (0.7* numRows) && x < (numRows)) || (y > (0.7* numCols) && y < (numCols)) ) {
                color[i] = Colors.OCEAN;
            }
            else color[i] = Colors.PLAINS;
        }

        // Generate terrain
        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numCols; y++) {
                n = 0;
                for (int i = 0; i < seeds; i++) {
                    if (distanceV(px[i], x, py[i], y) < distanceV(px[n], x, py[n], y)) n = i;
                }
                terrainGrid[x][y] = color[n];
            }
        }

        // Generating coasts
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (terrainGrid[i][j] == Colors.HILLS) {
                    if ((terrainGrid[i - 1][j] == Colors.OCEAN || terrainGrid[i][j - 1] == Colors.OCEAN)) {
                        Point point = new Point(i, j);
                        coastH.add(point);
                    }
                }
                if (terrainGrid[i][j] == Colors.PLAINS) {
                    if ((terrainGrid[i + 1][j] == Colors.OCEAN || terrainGrid[i][j + 1] == Colors.OCEAN)) {
                        Point point = new Point(i, j);
                        coastP.add(point);
                    }
                }
            }
        }


    }
    // Drawing
    public void draw(Graphics g) {
        // Landscape
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Color terrainColor = terrainGrid[i][j];
                g.setColor(terrainColor);
                g.fillRect(i, j, 1, 1);
            }
        }
        // Coasts
        g.setColor(Color.MAGENTA);
        for (Point i:coastH) g.fillRect(i.x, i.y, 1, 1);
        g.setColor(Color.ORANGE);
        for (Point i:coastP) g.fillRect(i.x, i.y, 1, 1);
    }
}