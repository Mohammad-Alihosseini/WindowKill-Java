package controller.constants;

public enum UIMessageConstants {
    GAME_SPEED_SLIDER_NAME, VOLUME_SLIDER_NAME, EXIT_MESSAGE, EXIT_TITLE, PURCHASE_TITLE,
    SUCCESSFUL_PURCHASE_TITLE, UNSUCCESSFUL_PURCHASE_TITLE, SUCCESSFUL_ABILITY_ACTIVATION_TITLE, UNSUCCESSFUL_ABILITY_ACTIVATION_TITLE, ACTIVATE_TITLE,
    TUTORIAL_MESSAGE, TUTORIAL_TITLE, EXIT_GAME_MESSAGE, EXIT_GAME_TITLE, ENTER_PROFILE_ID_MESSAGE, NEW_PROFILE_CODE, INVALID_PROFILE_MESSAGE, SAVE_FILE_EXTENSION;

    public String getValue() {
        return switch (this) {

            case GAME_SPEED_SLIDER_NAME -> "Game Speed";
            case VOLUME_SLIDER_NAME -> "Master Volume";
            case EXIT_MESSAGE -> "Are you sure to exit the game?";
            case EXIT_TITLE -> "Confirm Exit";
            case PURCHASE_TITLE -> "Confirm Purchase";
            case SUCCESSFUL_PURCHASE_TITLE -> "Skill Acquired";
            case UNSUCCESSFUL_PURCHASE_TITLE -> "Purchase Unsuccessful";
            case SUCCESSFUL_ABILITY_ACTIVATION_TITLE -> "Ability Activated";
            case UNSUCCESSFUL_ABILITY_ACTIVATION_TITLE -> "Activation Unsuccessful";
            case ACTIVATE_TITLE -> "Confirm Activation";
            case TUTORIAL_MESSAGE -> """
                    Use WASD to move your character\s
                    Use LMB to shoot at your enemies\s
                    Each enemy has a certain amount of HP\s
                    Entities fade in color as they lose HP\s
                    Nearby collisions emit impact waves. Use them in your favor\s
                    Enemies become more and more as time passes. Be fast to survive\s
                    Use SHIFT to use your selected skill. Press Esc to pause the game
                    """;
            case TUTORIAL_TITLE -> "Game Tutorial";
            case EXIT_GAME_MESSAGE -> "Are you sure to exit the game?\nAll progress will be lost.";
            case EXIT_GAME_TITLE -> "Exit Game";
            case ENTER_PROFILE_ID_MESSAGE -> "Enter your profile id: (type \"" + NEW_PROFILE_CODE.getValue() + "\" if you don't have a profile)";
            case NEW_PROFILE_CODE -> "NEW";
            case INVALID_PROFILE_MESSAGE -> "Invalid profile id";
            case SAVE_FILE_EXTENSION -> ".json";
        };
    }
}
