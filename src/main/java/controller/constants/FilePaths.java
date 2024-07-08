package controller.constants;

public enum FilePaths {
    BUTTONS_UI_PATH, SLIDER_UI_PATH, ORBITRON_FONT_PATH, MANTINIA_FONT_PATH, GAME_THEME_PATH, MENU_THEME_PATH,
    COUNTDOWN_EFFECTS_PATH, SHOOT_SOUND_EFFECTS_PATH, DOWN_SOUND_EFFECTS_PATH, HIT_SOUND_EFFECTS_PATH, XP_SOUND_EFFECTS_PATH,
    SQUARANTINE_IMAGEPATH, TRIGORATH_IMAGEPATH, EPSILON_IMAGEPATH, BULLET_IMAGEPATH, MENU_IMAGEPATH, JsonFolderPath;

    public String getValue() {
        return switch (this) {

            case BUTTONS_UI_PATH -> "./src/main/resources/ui elements/";
            case SLIDER_UI_PATH -> "./src/main/resources/ui elements/slider.png";
            case ORBITRON_FONT_PATH -> "./src/main/resources/fonts/Orbitron.ttf";
            case MANTINIA_FONT_PATH -> "./src/main/resources/fonts/Mantinia.otf";
            case GAME_THEME_PATH -> "./src/main/resources/effects/backgrounds/BG0.ogg";
            case MENU_THEME_PATH -> "./src/main/resources/effects/backgrounds/BG1.ogg";
            case COUNTDOWN_EFFECTS_PATH -> "./src/main/resources/effects/countdown/";
            case SHOOT_SOUND_EFFECTS_PATH -> "./src/main/resources/effects/shoot effects/";
            case DOWN_SOUND_EFFECTS_PATH -> "./src/main/resources/effects/down effects/";
            case HIT_SOUND_EFFECTS_PATH -> "./src/main/resources/effects/hit effects/";
            case XP_SOUND_EFFECTS_PATH -> "./src/main/resources/effects/xp effects/";
            case SQUARANTINE_IMAGEPATH -> "./src/main/resources/images/squarantine.png";
            case TRIGORATH_IMAGEPATH -> "./src/main/resources/images/trigorath.png";
            case BULLET_IMAGEPATH, EPSILON_IMAGEPATH -> "./src/main/resources/images/epsilon.png";
            case MENU_IMAGEPATH -> "./src/main/resources/ui elements/menu.png";
            case JsonFolderPath -> "./src/main/saves/";
        };
    }

}
