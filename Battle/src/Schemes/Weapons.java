package Schemes;

import Army.Weapon;

public class Weapons {
    //Declaration of weapons   For now values just estimated
    public static final Weapon SPEAR = new Weapon(10,50,5);
    public static final Weapon SWORD = new Weapon(5,70,7);
    public static final Weapon AXE  = new Weapon(3,40,6);
    public static final Weapon DOUBLE_AXE = new Weapon(4,20,10);
    public static final Weapon BOW = new Weapon(50,30,3);

    public static final Weapon[] ARSENAL = {
            SPEAR,
            SWORD,
            AXE,
            DOUBLE_AXE,
            BOW
    };
}
