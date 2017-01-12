package Army;

import Schemes.Colors;

import java.awt.*;

/**
 * Created by anka on 03.01.17.
 */
public class ThrownWeapon {
    public float range = 5;
    public float accuracy = 3;
    public float damage = 2;

    public ThrownWeapon(){
    }

    public void draw(int x, int y, float direction, Graphics g) {
        g.setColor(Colors.WEAPON);
        //implement roration based on direction
        g.fillRect(x, y, 1, 2);
    }
}