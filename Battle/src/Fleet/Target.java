package Fleet;

import java.awt.*;

public class Target {
    public Point target;
    public Point building;
    public int used;    //0 - no used, 1 - used

    public Target(Point target, Point building){
        this.target = new Point(target);
        this.building = new Point(building);
        this.used = 0;
    }

    public void use(){
        this.used = 1;
    }

    public void draw(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(target.x, target.y, 3, 3);
    }
}
