package view.menu;

import controller.constants.DefaultMethods;
import model.Profile;
import view.base.ButtonB;
import view.base.PanelB;
import view.base.SliderB;
import view.containers.TopElement;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.AudioHandler.setAllVolumes;
import static controller.UserInterfaceController.*;
import static controller.constants.DefaultMethods.ABILITY_ACTIVATE_MESSAGE;
import static controller.constants.DimensionConstants.PAUSE_MENU_DIMENSION;
import static controller.constants.UIConstants.*;
import static controller.constants.UIMessageConstants.*;
import static view.containers.GlassFrame.getGlassFrame;

public class PauseMenu extends PanelB implements TopElement {
    static CopyOnWriteArrayList<Component> abilities = new CopyOnWriteArrayList<>();
    private static PauseMenu INSTANCE;
    private static boolean pauseAccess=true;

    private PauseMenu() {
        super(PAUSE_MENU_DIMENSION.getValue());
        ButtonB xp = new ButtonB(ButtonB.ButtonType.small_menu_button, Profile.getCurrent().currentGameXP + " XP",
                (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), true, false);
        xp.setFont(xp.boldFont);
        ButtonB resume = new ButtonB(ButtonB.ButtonType.small_menu_button, "RESUME", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> PauseMenu.getINSTANCE().togglePanel());
        }};
        SliderB volumeSlider = new SliderB(this, MIN_VOLUME.getValue(), MAX_VOLUME.getValue(), Profile.getCurrent().SOUND_SCALE, VOLUME_SLIDER_NAME.getValue());
        volumeSlider.addChangeListener(e -> {
            Profile.getCurrent().SOUND_SCALE = volumeSlider.getPreciseValue();
            setAllVolumes();
        });
        ConcurrentHashMap<String, Integer> abilitiesData = getAbilitiesData();
        for (String abilityName : abilitiesData.keySet()) {
            abilities.add(new ButtonB(ButtonB.ButtonType.acquired_skill, abilityName, (int) SKILL_BUTTON_WIDTH.getValue(), ABILITY_FONT_SIZE_SCALE.getValue(), false, false) {{
                addActionListener(e -> {
                    int action = JOptionPane.showConfirmDialog(getINSTANCE(), ABILITY_ACTIVATE_MESSAGE(abilitiesData.get(abilityName)), "Ability Activation", JOptionPane.YES_NO_OPTION);
                    if (action == JOptionPane.YES_OPTION) {
                        if (activateAbility(abilityName)) {
                            int confirmAction = JOptionPane.showOptionDialog(getINSTANCE(), DefaultMethods.SUCCESSFUL_ACTIVATE_MESSAGE(abilityName),
                                    SUCCESSFUL_ABILITY_ACTIVATION_TITLE.getValue(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
                            if (confirmAction == JOptionPane.CLOSED_OPTION) {
                                PauseMenu.getINSTANCE().togglePanel();
                            }
                        } else {
                            JOptionPane.showOptionDialog(getINSTANCE(), DefaultMethods.UNSUCCESSFUL_ACTIVATE_MESSAGE(abilityName), UNSUCCESSFUL_PURCHASE_TITLE.getValue(),
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
                        }
                    }
                });
            }});
        }
        ButtonB exit = new ButtonB(ButtonB.ButtonType.small_menu_button, "EXIT", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                int action = JOptionPane.showConfirmDialog(getINSTANCE(), EXIT_GAME_MESSAGE.getValue(), EXIT_GAME_TITLE.getValue()
                        , JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (action == JOptionPane.YES_OPTION) {
                    exitGame();
                    PauseMenu.getINSTANCE().togglePanel(true);
                    MainMenu.flushINSTANCE();
                    MainMenu.getINSTANCE().togglePanel();
                }
            });
        }};
        constraints.gridwidth = 2;
        add(xp, false, true);
        add(resume, false, true);
        constraints.gridy++;
        bulkAdd(abilities, 3);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        horizontalBulkAdd(java.util.List.of(volumeSlider.labelButton, volumeSlider));
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add(exit, false, true);
    }

    public static PauseMenu getINSTANCE() {
        if (INSTANCE != null && !INSTANCE.isVisible()) INSTANCE = new PauseMenu();
        else if (INSTANCE == null) INSTANCE = new PauseMenu();
        return INSTANCE;
    }

    public static void setPauseAccess(boolean pauseAccess) {
        PauseMenu.pauseAccess = pauseAccess;
    }

    @Override
    public void togglePanel() {
        togglePanel(false);
    }
    public void togglePanel(boolean exit){
        if (!exit){
            if (pauseAccess) {
                super.togglePanel();
                toggleGameRunning();
            }
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        pinOnTop();
    }

    @Override
    public JFrame getFrame() {
        return getGlassFrame();
    }
}
