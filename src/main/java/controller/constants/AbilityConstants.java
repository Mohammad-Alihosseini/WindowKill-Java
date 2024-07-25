package controller.constants;

import java.util.concurrent.TimeUnit;

public enum AbilityConstants {
    WRIT_OF_ACESO_HEALING_FREQUENCY, WRIT_OF_ACESO_HEALING_AMOUNT, WRIT_OF_ARES_BUFF_AMOUNT, ATHENA_ABILITY_SHOOTING_RAPIDITY,
    PHONOI_ABILITY_SHOOT_DAMAGE, WRIT_OF_ASTRAPE_DAMAGE, WRIT_OF_CHIRON_HEALING_AMOUNT, ATHENA_ABILITY_TIME,
    APOLLO_ABILITY_HEALING_AMOUNT, HYPNOS_ABILITY_TIME, HEPHAESTUS_ABILITY_WAVE_POWER, PHONOI_COOLDOWN;

    public float getValue() {
        return switch (this) {

            case WRIT_OF_ACESO_HEALING_FREQUENCY -> TimeUnit.SECONDS.toNanos(1);
            case WRIT_OF_ACESO_HEALING_AMOUNT -> 1;
            case WRIT_OF_ARES_BUFF_AMOUNT -> 2;
            case ATHENA_ABILITY_SHOOTING_RAPIDITY -> 3;
            case PHONOI_ABILITY_SHOOT_DAMAGE -> 50;
            case WRIT_OF_ASTRAPE_DAMAGE -> 2;
            case WRIT_OF_CHIRON_HEALING_AMOUNT -> 3;
            case ATHENA_ABILITY_TIME -> TimeUnit.SECONDS.toMillis(10);
            case APOLLO_ABILITY_HEALING_AMOUNT -> 10;
            case HYPNOS_ABILITY_TIME -> TimeUnit.SECONDS.toMillis(10);
            case HEPHAESTUS_ABILITY_WAVE_POWER -> 1.5f;
            case PHONOI_COOLDOWN -> 120;
        };
    }
}
