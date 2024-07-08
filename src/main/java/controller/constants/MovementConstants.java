package controller.constants;

import model.Profile;

import java.util.concurrent.TimeUnit;

public enum MovementConstants {
    EPSILON_SPEED, ANGULAR_SPEED_BOUND, DEFAULT_ANGULAR_DECAY,
    MAX_SAFE_ROTATION, DEFAULT_SPEED, BULLET_SPEED, DEFAULT_DECELERATION,
    DECELERATION_DECAY, DECELERATION_SENSITIVITY, DAMPEN_FACTOR, POSITION_UPDATE_INTERVAL;

    public float getValue() {
        return switch (this) {
            case EPSILON_SPEED -> Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * 80f / Profile.getCurrent().UPS;
            case ANGULAR_SPEED_BOUND -> 400f / Profile.getCurrent().UPS;
            case DEFAULT_ANGULAR_DECAY -> 1 - (0.5f * Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED / Profile.getCurrent().UPS);
            case MAX_SAFE_ROTATION -> 240f / Profile.getCurrent().UPS;
            case DEFAULT_SPEED -> Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * 30f / Profile.getCurrent().UPS;
            case BULLET_SPEED -> Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * 300f / Profile.getCurrent().UPS;
            case DEFAULT_DECELERATION ->
                    Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * (-10f) / (Profile.getCurrent().UPS * Profile.getCurrent().UPS);
            case DECELERATION_DECAY -> 1 - (2f * Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED / Profile.getCurrent().UPS);
            case DECELERATION_SENSITIVITY -> 0.000001f / Profile.getCurrent().UPS;
            case DAMPEN_FACTOR -> 1 + Profile.getCurrent().GAME_SPEED * 0.015f;
            case POSITION_UPDATE_INTERVAL -> TimeUnit.SECONDS.toNanos(1) / (Profile.getCurrent().GAME_SPEED * Profile.getCurrent().UPS);
        };
    }
}
