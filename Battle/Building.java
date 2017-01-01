package Battle;

import java.awt.*;
import java.util.Random;

public class Building  {

    private int width, height, loot, rotation;
    public int x, y;

    public Building (int x, int y, int width, int height, int loot) {
        Random r = new Random();
        this.x = x;
        this.y = y;
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

        g2d.setColor(Colors.BUILDINGS);
        g2d.rotate(Math.toRadians(rotation), x + width/2, y + height/2);
        g2d.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.loot), x + width/2, y + height/2);

    }
}