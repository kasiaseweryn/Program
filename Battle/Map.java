package Battle;

import java.awt.*;
import java.util.Random;

public class Map {
    // Number of seeds and size
    private int seeds;
    public int numRows;
    public int numCols;
//  public static final int PREFERRED_GRID_SIZE_PIXELS = 1;  may be useful in future
    // Grid of terrain
    public Color[][] terrainGrid;

    // Constructor
    public Map(int rows, int cols, int seeds){
        this.numRows = rows;
        this.numCols = cols;
        this.seeds = seeds;
        this.terrainGrid = new Color[numRows][numCols];

        // Local variables
        Random r = new Random();
        int n = 0;
        int[] px = new int[this.seeds];
        int[] py = new int[this.seeds];
        Color[] color = new Color[this.seeds];

        // Generate seeds
        for (int i = 0; i < this.seeds; i++) {
            int x = r.nextInt(numRows);
            int y = r.nextInt(numCols);
            px[i] = x;
            py[i] = y;
            if (x > (0.1* numRows) && x < (0.4* numRows) && y > (0.1* numCols) && y < (0.4* numCols) ) {
                color[i] = Colors.CITY;
            }
            else if ((distance2(numRows,x,numCols,y) < numCols/3.5)) {
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
                for (int i = 0; i < this.seeds; i++) {
                    if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) n = i;
                }
                this.terrainGrid[x][y] = color[n];
            }
        }
    }

    // Function for Voronoi
    private double distance(int x1, int x2, int y1, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private double distance2(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    // Drawing
    public void draw(Graphics g) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Color terrainColor = terrainGrid[i][j];
                g.setColor(terrainColor);
                g.fillRect(i, j, 1, 1);
            }
        }
    }
}