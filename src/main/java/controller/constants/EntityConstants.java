package controller.constants;

import model.Profile;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.DimensionConstants.*;

public enum EntityConstants {
    EPSILON_HEALTH, SHOTS_PER_SECOND, SKILL_COOLDOWN_IN_MINUTES, DROWN_COOL_DOWN_SECONDS, COLLECTIBLE_LIFE_TIME, EPSILON_RAPID_SHOOTING_DELAY,
    EPSILON_SHOOTING_RAPIDITY, ENEMY_SHOUTING_RAPIDITY, ENEMY_BULLET_LIFE_TIME,
    TRIGORATH_MELEE_DAMAGE, SQUARANTINE_MELEE_DAMAGE, TRIGORATH_HEALTH, SQUARANTINE_HEALTH,
    BARRICADOS_HEALTH,
    BULLET_HEALTH, COLLECTIBLE_HEALTH,
    ARCHMIRE_HEALTH, ARCHMIRE_MELEE,
    ARCHMIRE_PATH_LIFE_TIME, ARCHMIRE_PATH_MELEE, ARCHMIRE_PATH_HEALTH,
    OMENOCT_HEALTH, OMENOCT_MELEE, OMENOCT_RANGED,
    NECROPICK_RANGED, NECROPICK_MELEE, NECROPICK_DISTANCE_FROM_EPSILON, NECROPICK_HEALTH,
    WYRM_HEALTH, WYRM_RANGED, WYRM_MELEE;

    public int getValue() {
        return switch (this) {
            case EPSILON_SHOOTING_RAPIDITY -> Profile.getCurrent().getEpsilonShootingRapidity();
            case ENEMY_SHOUTING_RAPIDITY -> 4;
            case EPSILON_HEALTH -> 100;
            case SHOTS_PER_SECOND -> 2;
            case SKILL_COOLDOWN_IN_MINUTES -> 5;
            case EPSILON_RAPID_SHOOTING_DELAY -> 50;
            case TRIGORATH_HEALTH -> 15;
            case ENEMY_BULLET_LIFE_TIME -> 10;
            case TRIGORATH_MELEE_DAMAGE -> 10;
            case SQUARANTINE_HEALTH -> 10;
            case SQUARANTINE_MELEE_DAMAGE -> 6;
            case WYRM_HEALTH -> 12;
            case BARRICADOS_HEALTH -> 999999999;
            case NECROPICK_HEALTH -> 10;
            case ARCHMIRE_HEALTH -> 30;
            case ARCHMIRE_PATH_HEALTH -> 999999999;
            case OMENOCT_HEALTH -> 20;
            case BULLET_HEALTH, COLLECTIBLE_HEALTH -> 0;
            case DROWN_COOL_DOWN_SECONDS -> 1;
            case COLLECTIBLE_LIFE_TIME -> 8;
            case ARCHMIRE_MELEE -> 10;
            case ARCHMIRE_PATH_LIFE_TIME -> 5;
            case ARCHMIRE_PATH_MELEE -> 2;
            case OMENOCT_MELEE -> 8;
            case OMENOCT_RANGED -> 4;
            case NECROPICK_RANGED -> 5;
            case NECROPICK_MELEE -> 0;
            case NECROPICK_DISTANCE_FROM_EPSILON -> (int) Profile.getCurrent().getSizeScale() * 120;
            case WYRM_RANGED -> 8;
            case WYRM_MELEE -> 0;
        };
    }

    public enum EntityVertices {
        TRIGORATH_VERTICES, SQUARANTINE_VERTICES, OMENOCT_VERTICES, BULLET_VERTICES, EPSILON_VERTICES,
        ARCHMIRE_VERTICES, MINIARCHMIRE_VERTICES, WYRM_VERTICES,
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
                case ARCHMIRE_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(ARCHMIRE_DIMENSION.getValue().width / 2F, 0),
                                new Point2D.Float(ARCHMIRE_DIMENSION.getValue().width / 2F, ARCHMIRE_DIMENSION.getValue().height),
                                new Point2D.Float(0, 4 * ARCHMIRE_DIMENSION.getValue().height / 5F),
                                new Point2D.Float(ARCHMIRE_DIMENSION.getValue().width, 4 * ARCHMIRE_DIMENSION.getValue().height / 5F)
                        ));

                case MINIARCHMIRE_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(MINIARCHMIRE_DIMENSION.getValue().width / 2F, 0),
                                new Point2D.Float(MINIARCHMIRE_DIMENSION.getValue().width / 2F, MINIARCHMIRE_DIMENSION.getValue().height),
                                new Point2D.Float(0, 4 * MINIARCHMIRE_DIMENSION.getValue().height / 5F),
                                new Point2D.Float(MINIARCHMIRE_DIMENSION.getValue().width, 4 * MINIARCHMIRE_DIMENSION.getValue().height / 5F)
                        ));

                case NECROPICK_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(OMENOCT_DIMENSION.getValue().width / 2F, 0),
                                new Point2D.Float(OMENOCT_DIMENSION.getValue().width / 2F, OMENOCT_DIMENSION.getValue().height),
                                new Point2D.Float(0, OMENOCT_DIMENSION.getValue().height / 5F),
                                new Point2D.Float(OMENOCT_DIMENSION.getValue().width, OMENOCT_DIMENSION.getValue().height / 5F)
                        ));

                case WYRM_VERTICES -> new CopyOnWriteArrayList<>(
                        List.of(new Point2D.Float(WYRM_DIMENSION.getValue().width / 3F, 0),
                                new Point2D.Float(2 * WYRM_DIMENSION.getValue().width / 3F, WYRM_DIMENSION.getValue().height),
                                new Point2D.Float(0, 4 * WYRM_DIMENSION.getValue().height / 5F),
                                new Point2D.Float(WYRM_DIMENSION.getValue().width, WYRM_DIMENSION.getValue().height / 5F)
                        ));
                case BULLET_VERTICES, EPSILON_VERTICES, COLLECTIBLE_VERTICES -> new CopyOnWriteArrayList<>();
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
