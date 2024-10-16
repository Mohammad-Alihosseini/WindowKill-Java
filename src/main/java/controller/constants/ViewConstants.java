package controller.constants;

import model.Profile;

public enum ViewConstants {
    VERTEX_RADIUS, BASE_PAINT_OPACITY, EPSILON_FACTOR, TRIGORATH_FACTOR, WYRM_FACTOR,
    SQUARANTINE_FACTOR, OMENOCT_FACTOR, NECROPICK_FACTOR, ARCHMIRE_FACTOR, BULLET_FACTOR, COLLECTIBLE_SIZE_OFFSET;

    public float getValue() {
        return switch (this) {
            case VERTEX_RADIUS -> Profile.getCurrent().getSizeScale() * 6;
            case BASE_PAINT_OPACITY -> 0.5f;
            case EPSILON_FACTOR -> Profile.getCurrent().getSizeScale() * 50;
            case TRIGORATH_FACTOR -> Profile.getCurrent().getSizeScale() * 70;
            case WYRM_FACTOR -> Profile.getCurrent().getSizeScale() * 108;
            case SQUARANTINE_FACTOR -> Profile.getCurrent().getSizeScale() * 60;
            case OMENOCT_FACTOR -> Profile.getCurrent().getSizeScale() * 72;
            case NECROPICK_FACTOR -> Profile.getCurrent().getSizeScale() * 60;
            case ARCHMIRE_FACTOR -> Profile.getCurrent().getSizeScale() * 84;
            case BULLET_FACTOR -> Profile.getCurrent().getSizeScale() * 20;
            case COLLECTIBLE_SIZE_OFFSET -> 9;
        };
    }
}
