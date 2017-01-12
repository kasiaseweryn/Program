package Armament;

public class Weapon {
    private int range;
    private int accurency;
    private int damage;
    private int penetration;

    public Weapon(int range, int accurency, int damage, int penetration ){
        this.range = range;
        this.accurency = accurency;
        this.damage = damage;
        this.penetration = penetration;
    }
}