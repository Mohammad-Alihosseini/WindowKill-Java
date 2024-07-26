package controller.constants;

public enum WaveConstants {
    MIN_ENEMY_SPAWN_RADIUS, MAX_ENEMY_SPAWN_RADIUS, ENEMY_DROP_DELAY_SECONDS;

    public int getValue() {
        return switch (this) {
            case MIN_ENEMY_SPAWN_RADIUS -> 500;
            case MAX_ENEMY_SPAWN_RADIUS -> 800;
            case ENEMY_DROP_DELAY_SECONDS -> 2;
        };
    }
}
