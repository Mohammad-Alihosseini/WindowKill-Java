package controller.constants;

import model.Profile;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.DimensionConstants.*;

public enum EntityConstants {
    EPSILON_HEALTH, SHOTS_PER_SECOND, SKILL_COOLDOWN_IN_MINUTES, COLLECTIBLE_LIFE_TIME, EPSILON_RAPID_SHOOTING_DELAY,
    EPSILON_SHOOTING_RAPIDITY, ENEMY_SHOUTING_RAPIDITY, TRIGORATH_MELEE_DAMAGE, SQUARANTINE_MELEE_DAMAGE,
    TRIGORATH_HEALTH, SQUARANTINE_HEALTH, WYRM_HEALTH, BARRICADOS_HEALTH, NECROPICK_HEALTH, ARCHMIRE_HEALTH, OMENOCT_HEALTH,
    BULLET_HEALTH, COLLECTIBLE_HEALTH,
    OMENOCT_MELEE, OMENOCT_RANGED, NECROPICK_RANGED;

    public int getValue() {
        return switch (this) {
            case EPSILON_SHOOTING_RAPIDITY -> Profile.getCurrent().getEpsilonShootingRapidity();
            case ENEMY_SHOUTING_RAPIDITY -> 4;
            case EPSILON_HEALTH -> 100;
            case SHOTS_PER_SECOND -> 2;
            case SKILL_COOLDOWN_IN_MINUTES -> 5;
            case EPSILON_RAPID_SHOOTING_DELAY -> 50;
            case TRIGORATH_HEALTH -> 15;
            case TRIGORATH_MELEE_DAMAGE -> 10;
            case SQUARANTINE_HEALTH -> 10;
            case SQUARANTINE_MELEE_DAMAGE -> 6;
            case WYRM_HEALTH -> 12;
            case BARRICADOS_HEALTH -> 999999999;
            case NECROPICK_HEALTH -> 10;
            case ARCHMIRE_HEALTH -> 30;
            case OMENOCT_HEALTH -> 20;
            case BULLET_HEALTH, COLLECTIBLE_HEALTH -> 0;
            case COLLECTIBLE_LIFE_TIME -> 8;
            case OMENOCT_MELEE -> 8;
            case OMENOCT_RANGED -> 4;
            case NECROPICK_RANGED -> 5;
        };
    }

    public enum EntityVertices {
        TRIGORATH_VERTICES, SQUARANTINE_VERTICES, OMENOCT_VERTICES, BULLET_VERTICES, EPSILON_VERTICES,
        COLLECTIBLE_VERTICES, NECROPICK_VERTICES;

        public List<Point2D> getValue() {
            float octagonSide = OMENOCT_DIMENSION.getValue().height / 3F;

            return switch (this) {

                case TRIGORATH_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(0, 0),
                                new Point2D.Float(0, TRIGORATH_DIMENSION.getValue().height),
                                new Point2D.Float(TRIGORATH_DIMENSION.getValue().width, TRIGORATH_DIMENSION.getValue().height / 2F)));
                case SQUARANTINE_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(0, 0),
                                new Point2D.Float(0, SQUARANTINE_DIMENSION.getValue().height),
                                new Point2D.Float(SQUARANTINE_DIMENSION.getValue().width, SQUARANTINE_DIMENSION.getValue().height),
                                new Point2D.Float(SQUARANTINE_DIMENSION.getValue().width, 0)));
                case OMENOCT_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(1 * octagonSide, 0),
                                new Point2D.Float(2 * octagonSide, 0),
                                new Point2D.Float(0, (float) (Math.sqrt(2) / 2 * octagonSide)),
                                new Point2D.Float(3 * octagonSide, (float) (Math.sqrt(2) / 2 * octagonSide)),
                                new Point2D.Float(0, (float) (Math.sqrt(2) / 2 * octagonSide) + octagonSide),
                                new Point2D.Float(3 * octagonSide, (float) (Math.sqrt(2) / 2 * octagonSide) + octagonSide),
                                new Point2D.Float(1 * octagonSide, 3 * octagonSide),
                                new Point2D.Float(2 * octagonSide, 3 * octagonSide)
                        ));
                case BULLET_VERTICES, EPSILON_VERTICES, COLLECTIBLE_VERTICES -> new CopyOnWriteArrayList<>();

                case NECROPICK_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(OMENOCT_DIMENSION.getValue().width / 2F, 0),
                                new Point2D.Float(OMENOCT_DIMENSION.getValue().width / 2F, OMENOCT_DIMENSION.getValue().height),
                                new Point2D.Float(0, 2 * OMENOCT_DIMENSION.getValue().height / 3F),
                                new Point2D.Float(OMENOCT_DIMENSION.getValue().width, 2 * OMENOCT_DIMENSION.getValue().height / 3F)
                        ));
            };
        }
    }

    public enum PointConstants {
        EPSILON_CENTER, BULLET_CENTER;

        public Point2D getValue() {
            return switch (this) {

                case EPSILON_CENTER ->
                        new Point2D.Float(EPSILON_DIMENSION.getValue().width / 2F, EPSILON_DIMENSION.getValue().height / 2F);
                case BULLET_CENTER ->
                        new Point2D.Float(BULLET_DIMENSION.getValue().width / 2F, BULLET_DIMENSION.getValue().height / 2F);
            };
        }
    }
}
