package controller.constants;

import java.awt.*;
import java.awt.geom.Point2D;

import static controller.constants.ViewConstants.*;

public enum DimensionConstants {
    SCREEN_SIZE, EPSILON_DIMENSION, TRIGORATH_DIMENSION, SQUARANTINE_DIMENSION, OMENOCT_DIMENSION, NECROPICK_DIMENSION,
    BULLET_DIMENSION, LOGIN_PAGE_DIMENSION, ARCHMIRE_DIMENSION, MINIARCHMIRE_DIMENSION, WYRM_DIMENSION,
    MAIN_MENU_DIMENSION, SETTINGS_MENU_DIMENSION, SKILL_TREE_DIMENSION, PAUSE_MENU_DIMENSION, FPS_COUNTER_DIMENSION;

    public Dimension getValue() {
        return switch (this) {

            case SCREEN_SIZE -> Toolkit.getDefaultToolkit().getScreenSize();
            case EPSILON_DIMENSION -> new Dimension((int) EPSILON_FACTOR.getValue(), (int) EPSILON_FACTOR.getValue());
            case TRIGORATH_DIMENSION ->
                    new Dimension((int) (TRIGORATH_FACTOR.getValue() * Math.sqrt(3) / 2), (int) TRIGORATH_FACTOR.getValue());
            case SQUARANTINE_DIMENSION ->
                    new Dimension((int) SQUARANTINE_FACTOR.getValue(), (int) SQUARANTINE_FACTOR.getValue());
            case OMENOCT_DIMENSION -> new Dimension((int) OMENOCT_FACTOR.getValue(), (int) OMENOCT_FACTOR.getValue());
            case NECROPICK_DIMENSION ->
                    new Dimension((int) NECROPICK_FACTOR.getValue(), (int) NECROPICK_FACTOR.getValue());
            case ARCHMIRE_DIMENSION ->
                    new Dimension((int) ARCHMIRE_FACTOR.getValue(), (int) ARCHMIRE_FACTOR.getValue());
            case MINIARCHMIRE_DIMENSION ->
                    new Dimension((int) ARCHMIRE_FACTOR.getValue() / 2, (int) ARCHMIRE_FACTOR.getValue() / 2);
            case BULLET_DIMENSION -> new Dimension((int) BULLET_FACTOR.getValue(), (int) BULLET_FACTOR.getValue());
            case LOGIN_PAGE_DIMENSION -> new Dimension(750, 400);
            case WYRM_DIMENSION -> new Dimension((int) WYRM_FACTOR.getValue(), (int) WYRM_FACTOR.getValue());
            case MAIN_MENU_DIMENSION -> new Dimension(700, 800);
            case SETTINGS_MENU_DIMENSION -> new Dimension(900, 400);
            case SKILL_TREE_DIMENSION -> new Dimension(900, 950);
            case PAUSE_MENU_DIMENSION -> new Dimension(900, 750);
            case FPS_COUNTER_DIMENSION -> new Dimension(100, 55);
        };
    }

    public enum Dimension2DConstants {
        DEFORM_DIMENSION, MAIN_MOTIONPANEL_DIMENSION;

        public Point2D getValue() {
            return switch (this) {

                case DEFORM_DIMENSION -> new Point2D.Float(500, 500);
                case MAIN_MOTIONPANEL_DIMENSION -> new Point2D.Float(800, 800);
            };
        }
    }
}

