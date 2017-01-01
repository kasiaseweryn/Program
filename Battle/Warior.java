package Battle;

import java.awt.*;

public class Warior {
    public float health;
    public float moral;
    public float defense;
    public float speedX;
    public float speedY;
    public float size;
    public float current_locationX;
    public float current_locationY;
    public float previous_locationX;
    public float previous_locationY;
    public Building target_location;
    public Warior target_enemy;
    public Weapon prime_weapon;
    public int thrown_weapon;
    public float viewing_direction;
    public float shield_direction;
    public int loot;
    public boolean in_cover;
    public int state;
    public Color color;
}
