package Map;

import Schemes.Colors;

import java.awt.*;
import java.util.Random;

public class Building  {

    private int width, height, loot, rotation;
    private Point location;

    public Building (Point location, int width, int height, int loot) {
        Random r = new Random();
        this.location = new Point(location);
        this.width = width;
        this.height = height;
        this.loot = loot;
        this.rotation = r.nextInt(91);
    }

    public int looting () {
        if (this.loot > 0){
            this.loot --;
            return 1;
        }
        else return 0;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Building
        g2d.setColor(Colors.BUILDINGS);
        g2d.rotate(Math.toRadians(rotation), location.x, location.y);
        g2d.fillRect(location.x - width/2, location.y - height/2, width, height);
        // Center
        g2d.setColor(Colors.LOCATION);
        g2d.fillRect(location.x, location.y, 1, 1);
        // Loot
        g.setColor(Colors.TEXT);
        g.drawString(String.valueOf(this.loot), location.x, location.y);

    }

    public Point getLocation() {
        return location;
    }
}