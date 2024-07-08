package view.menu;

import view.base.ButtonB;
import view.base.PanelB;

import javax.swing.*;
import java.util.List;

import static controller.UserInterfaceController.*;
import static controller.constants.DimensionConstants.MAIN_MENU_DIMENSION;
import static controller.constants.UIConstants.MENU_BUTTON_WIDTH;
import static controller.constants.UIMessageConstants.*;

public final class MainMenu extends PanelB {

    private static MainMenu INSTANCE;

    private MainMenu() {
        super(MAIN_MENU_DIMENSION.getValue());
        playMenuTheme();
        ButtonB start = new ButtonB(ButtonB.ButtonType.menu_button, "START", (int) MENU_BUTTON_WIDTH.getValue(), true) {{
            addActionListener(e -> {
                MainMenu.getINSTANCE().togglePanel();
                toggleGameRunning();
            });
        }};
        ButtonB settings = new ButtonB(ButtonB.ButtonType.menu_button, "SETTINGS", (int) MENU_BUTTON_WIDTH.getValue(), true) {{
            addActionListener(e -> {
                MainMenu.getINSTANCE().togglePanel();
                SettingsMenu.getINSTANCE().togglePanel();
            });
        }};
        ButtonB skillTree = new ButtonB(ButtonB.ButtonType.menu_button, "SKILL TREE", (int) MENU_BUTTON_WIDTH.getValue(), true) {{
            addActionListener(e -> {
                MainMenu.getINSTANCE().togglePanel();
                SkillTree.getINSTANCE().togglePanel();
            });
        }};
        ButtonB tutorial = new ButtonB(ButtonB.ButtonType.menu_button, "TUTORIAL", (int) MENU_BUTTON_WIDTH.getValue(), true) {{
            addActionListener(e -> JOptionPane.showMessageDialog(getINSTANCE(), TUTORIAL_MESSAGE.getValue(), TUTORIAL_TITLE.getValue(), JOptionPane.PLAIN_MESSAGE));
        }};
        ButtonB exit = new ButtonB(ButtonB.ButtonType.menu_button, "EXIT", (int) MENU_BUTTON_WIDTH.getValue(), true) {{
            addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(getINSTANCE(), EXIT_MESSAGE.getValue(), EXIT_TITLE.getValue(), JOptionPane.YES_NO_OPTION)
                        == JOptionPane.YES_OPTION) System.exit(0);
            });
        }};
        verticalBulkAdd(List.of(start, settings, skillTree, tutorial, exit));
    }

    public static void flushINSTANCE() {
        PauseMenu.getINSTANCE().setVisible(false);
        INSTANCE = null;
    }

    public static MainMenu getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainMenu();
        return INSTANCE;
    }

}
