package controller.constants;

import model.Profile;

public enum ViewConstants {
    VERTEX_RADIUS, BASE_PAINT_OPACITY, EPSILON_FACTOR, TRIGORATH_FACTOR,
    SQUARANTINE_FACTOR, BULLET_FACTOR, COLLECTIBLE_SIZE_OFFSET;

    public float getValue() {
        return switch (this) {
            case VERTEX_RADIUS -> Profile.getCurrent().SIZE_SCALE * 6;
            case BASE_PAINT_OPACITY -> 0.5f;
            case EPSILON_FACTOR -> Profile.getCurrent().SIZE_SCALE * 50;
            case TRIGORATH_FACTOR -> Profile.getCurrent().SIZE_SCALE * 70;
            case SQUARANTINE_FACTOR -> Profile.getCurrent().SIZE_SCALE * 60;
            case BULLET_FACTOR -> Profile.getCurrent().SIZE_SCALE * 20;
            case COLLECTIBLE_SIZE_OFFSET -> 9;
        };
    }
}
