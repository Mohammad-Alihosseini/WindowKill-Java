package controller.constants;

import model.Profile;

import java.util.concurrent.TimeUnit;

public enum ImpactConstants {
    /**
     * Impact speed shall be higher than movement speed to avoid tunneling <p>
     * Lower values of sensitivity avoid tunneling at cost of computational power
     */
    IMPACT_SPEED, IMPACT_DECELERATION, IMPACT_RADIUS, IMPACT_SCALE, IMPACT_DRIFT_FACTOR, IMPACT_COOLDOWN,
    COLLISION_SENSITIVITY, DETECTION_SENSITIVITY, DIRECTION_SENSITIVITY, MELEE_COOLDOWN;

    public float getValue() {
        return switch (this) {
            case IMPACT_SPEED -> Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * 120f / Profile.getCurrent().UPS;
            case IMPACT_DECELERATION ->
                    Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED * (-55f) / (Profile.getCurrent().UPS * Profile.getCurrent().UPS);
            case IMPACT_RADIUS -> Profile.getCurrent().SIZE_SCALE * 100;
            case IMPACT_SCALE -> 2f;
            case IMPACT_DRIFT_FACTOR -> 1.1f;
            case IMPACT_COOLDOWN -> TimeUnit.SECONDS.toNanos(1) / (100f * Profile.getCurrent().SIZE_SCALE * Profile.getCurrent().GAME_SPEED);
            case COLLISION_SENSITIVITY -> 0.8f * Profile.getCurrent().SIZE_SCALE;
            case DETECTION_SENSITIVITY -> 4f / Profile.getCurrent().SIZE_SCALE;
            case DIRECTION_SENSITIVITY -> 0.00001f;
            case MELEE_COOLDOWN -> TimeUnit.MILLISECONDS.toNanos(75);
        };
    }
}
