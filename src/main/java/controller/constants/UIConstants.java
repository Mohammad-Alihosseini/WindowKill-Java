package controller.constants;

import model.Profile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static controller.constants.FilePaths.MANTINIA_FONT_PATH;
import static controller.constants.FilePaths.ORBITRON_FONT_PATH;

public enum UIConstants {
    MAX_GAME_SPEED, MIN_GAME_SPEED, MAX_VOLUME, MIN_VOLUME, PLAIN_FONT_SIZE, BOLD_FONT_SIZE,
    MENU_BUTTON_WIDTH, TEXT_SCALE, SLIDER_PRECISION_SCALE, MESSAGE_FADE_INTERVAL, MESSAGE_HEIGHT, MESSAGE_FONT_SIZE,
    BACK_BUTTON_WIDTH, BACK_BUTTON_FONT_SCALE, SKILL_BUTTON_WIDTH, SKILL_FONT_SIZE_SCALE, ABILITY_FONT_SIZE_SCALE, SKILL_TEXT_OFFSET,
    SLIDER_MINOR_SPACINGS_NUMBER, SLIDER_MAJOR_SPACINGS_NUMBER, SLIDER_LABEL_WIDTH, SLIDER_LABEL_FONT_SIZE;

    public static final Color BLOOD_RED = new Color(138, 3, 3);
    public static final Color SCI_FI_BLUE = new Color(52, 220, 240);
    public static Font MANTINIA_FONT;
    public static Font ORBITRON_FONT;

    static {
        try {
            MANTINIA_FONT = Font.createFont(Font.TRUETYPE_FONT, new File(MANTINIA_FONT_PATH.getValue()));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(MANTINIA_FONT);
        } catch (IOException | FontFormatException ignored) {
        }
    }

    static {
        try {
            ORBITRON_FONT = Font.createFont(Font.TRUETYPE_FONT, new File(ORBITRON_FONT_PATH.getValue()));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(ORBITRON_FONT);
        } catch (IOException | FontFormatException ignored) {
        }
    }

    public float getValue() {
        return switch (this) {

            case MAX_GAME_SPEED -> 2;
            case MIN_GAME_SPEED -> 1;
            case MAX_VOLUME -> 8;
            case MIN_VOLUME -> 0; //All preset volumes shall be less than 2/MAX_VOLUME (currently 0.25)
            case PLAIN_FONT_SIZE -> 32 * Profile.getCurrent().SIZE_SCALE;
            case BOLD_FONT_SIZE -> 1.05f * PLAIN_FONT_SIZE.getValue();
            case MENU_BUTTON_WIDTH -> 560;
            case TEXT_SCALE -> 0.8f;
            case SLIDER_PRECISION_SCALE -> 10000000;
            case MESSAGE_FADE_INTERVAL -> 10;
            case MESSAGE_HEIGHT -> 100;
            case MESSAGE_FONT_SIZE -> 75;
            case BACK_BUTTON_WIDTH -> 720;
            case BACK_BUTTON_FONT_SCALE -> 0.65f;
            case SKILL_BUTTON_WIDTH -> 240;
            case SKILL_FONT_SIZE_SCALE -> 0.6f;
            case ABILITY_FONT_SIZE_SCALE -> 0.5f;
            case SKILL_TEXT_OFFSET -> -40;
            case SLIDER_MINOR_SPACINGS_NUMBER -> 100;
            case SLIDER_MAJOR_SPACINGS_NUMBER -> 20;
            case SLIDER_LABEL_WIDTH -> 150;
            case SLIDER_LABEL_FONT_SIZE -> 0.45f;
        };
    }
}
