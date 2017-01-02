package Army;

import Schemes.Colors;

import java.awt.*;

public class Weapon {
    public float range;
    public float accurency;
    public float damage;

    public Weapon(float range, float accurency, float damage){
        this.range = range;
        this.accurency = accurency;
        this.damage = damage;
    }

    public void draw(int x, int y, float direction, Graphics g) {
        g.setColor(Colors.WEAPON);
        //implement roration based on direction
        g.fillRect(x, y, 1, 10);
    }
}