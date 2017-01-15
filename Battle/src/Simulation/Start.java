package Simulation;

/**
 * Created by anka on 15.01.17.
 */
public class Start {
    private static int x;
    private static int y;
    private static int seeds;
    private static int time;

    public static void main(String[] args) {
        x = 1000;
        y = 1000;
        seeds = 900;
        time = 10;
        GUI gui = new GUI(x,y,seeds);
        gui.GameTimer(time);
    }
}
