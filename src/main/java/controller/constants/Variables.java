package controller.constants;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Variables {
    public static final ArrayList<Integer> WAVE_ENEMY_COUNT = new ArrayList<>(List.of(3, 5, 6, 10));
    public static int UPS = 800;
    public static int FPS = 80;
    public static int EPSILON_MELEE_DAMAGE = 10;
    public static int EPSILON_RANGED_DAMAGE = 5;
    public static int EPSILON_RADIUS = 50;
    public static ArrayList<Point2D> EPSILON_VERTICES = new ArrayList<>();
    public static float SOUND_SCALE = 6;
    public static float SIZE_SCALE = 0.75f;
    public static float GAME_SPEED = 1.8f;

    static {
        SIZE_SCALE = (float) Math.sqrt(SIZE_SCALE);
    }
}
