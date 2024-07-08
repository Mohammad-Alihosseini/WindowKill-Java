package view.menu;

import controller.constants.DefaultMethods;
import model.Profile;
import org.apache.commons.lang3.tuple.Pair;
import view.base.ButtonB;
import view.base.PanelB;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.*;
import static controller.constants.DimensionConstants.SKILL_TREE_DIMENSION;
import static controller.constants.UIConstants.*;
import static controller.constants.UIMessageConstants.*;

public class SkillTree extends PanelB {

    static CopyOnWriteArrayList<Component> categories = new CopyOnWriteArrayList<>();
    static CopyOnWriteArrayList<CopyOnWriteArrayList<Component>> skills = new CopyOnWriteArrayList<>();
    private static SkillTree INSTANCE;

    private SkillTree() {
        super(SKILL_TREE_DIMENSION.getValue());
        ButtonB xp = new ButtonB(ButtonB.ButtonType.small_menu_button, Profile.getCurrent().totalXP + " XP",
                (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), true, false);
        xp.setFont(xp.boldFont);
        updateSkillTree();
        ButtonB back = new ButtonB(ButtonB.ButtonType.small_menu_button, "BACK", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                SkillTree.getINSTANCE().togglePanel();
                MainMenu.getINSTANCE().togglePanel();
            });
        }};
        constraints.gridwidth = categories.size();
        add(xp, false, true);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        horizontalBulkAdd(categories);
        constraints.gridy++;
        for (CopyOnWriteArrayList<Component> levelSkills : skills) {
            constraints.gridy++;
            constraints.gridx = 0;
            horizontalBulkAdd(levelSkills);
        }
        constraints.gridx = 0;
        constraints.gridwidth = categories.size();
        add(back, false, true);
    }

    public static void updateSkillTree() {
        categories.clear();
        skills.clear();

        ConcurrentHashMap<String, Pair<CopyOnWriteArrayList<String>, Integer>> skillCategoriesData = getSkillCategoryData();
        ConcurrentHashMap<String, Pair<Integer, Boolean>> skillsData = getSkillsData();
        String activeSkillName = getActiveSkill();

        for (String categoryName : skillCategoriesData.keySet()) {
            ButtonB.ButtonType type = switch (skillCategoriesData.get(categoryName).getRight()) {
                case 0 -> ButtonB.ButtonType.category0;
                case 1 -> ButtonB.ButtonType.category1;
                case 2 -> ButtonB.ButtonType.category2;
                case 3 -> ButtonB.ButtonType.category3;
                default -> null;
            };
            ButtonB category = new ButtonB(type, categoryName, (int) SKILL_BUTTON_WIDTH.getValue(), SKILL_FONT_SIZE_SCALE.getValue(), true, false);
            category.setIconTextGap((int) SKILL_TEXT_OFFSET.getValue());
            category.setVerticalTextPosition(SwingConstants.TOP);
            categories.add(category);
        }
        int level = 0;
        boolean finished = false;
        while (!finished) {
            finished = true;
            CopyOnWriteArrayList<Component> levelSkills = new CopyOnWriteArrayList<>();
            for (String categoryName : skillCategoriesData.keySet()) {
                if (skillCategoriesData.get(categoryName).getLeft().size() > level) {
                    finished = false;
                    String name = skillCategoriesData.get(categoryName).getLeft().get(level);
                    ButtonB.ButtonType type = skillsData.get(name).getRight() ? ButtonB.ButtonType.acquired_skill : ButtonB.ButtonType.unacquired_skill;
                    if (name.equals(activeSkillName)) type = ButtonB.ButtonType.active_skill;

                    ButtonB skill = new ButtonB(type, name, (int) SKILL_BUTTON_WIDTH.getValue(), SKILL_FONT_SIZE_SCALE.getValue(),
                            type.equals(ButtonB.ButtonType.active_skill), false);
                    ButtonB.ButtonType finalType = type;
                    skill.addActionListener(e -> {
                        switch (finalType) {
                            case unacquired_skill -> {
                                int action = JOptionPane.showConfirmDialog(getINSTANCE(),
                                        DefaultMethods.PURCHASE_MESSAGE(skillsData.get(name).getLeft()), PURCHASE_TITLE.getValue(), JOptionPane.YES_NO_OPTION);
                                if (action == JOptionPane.YES_OPTION) {
                                    if (purchaseSkill(name)) {
                                        int actionConfirm = JOptionPane.showOptionDialog(getINSTANCE(),
                                                DefaultMethods.SUCCESSFUL_PURCHASE_MESSAGE(name), SUCCESSFUL_PURCHASE_TITLE.getValue(),
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
                                        if (actionConfirm == JOptionPane.CLOSED_OPTION) renewINSTANCE();
                                    } else {
                                        int actionConfirm = JOptionPane.showOptionDialog(getINSTANCE(), DefaultMethods.UNSUCCESSFUL_PURCHASE_MESSAGE(name), UNSUCCESSFUL_PURCHASE_TITLE.getValue(),
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
                                        if (actionConfirm == JOptionPane.CLOSED_OPTION) renewINSTANCE();
                                    }
                                }
                            }
                            case acquired_skill -> {
                                int action = JOptionPane.showConfirmDialog(getINSTANCE(), DefaultMethods.SKILL_ACTIVATE_MESSAGE(name), ACTIVATE_TITLE.getValue(), JOptionPane.YES_NO_OPTION);
                                if (action == JOptionPane.YES_OPTION) {
                                    setActiveSkill(name);
                                    renewINSTANCE();
                                }
                            }
                        }
                    });
                    levelSkills.add(skill);
                } else levelSkills.add(null);
            }
            if (!finished) {
                skills.add(levelSkills);
                level++;
            }
        }
    }

    public static void renewINSTANCE() {
        INSTANCE.setVisible(false);
        INSTANCE = null;
        getINSTANCE().togglePanel();
    }

    public static SkillTree getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SkillTree();
        return INSTANCE;
    }
}
