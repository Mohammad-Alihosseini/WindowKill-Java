package model.entities;

import model.Profile;
import model.characters.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.*;
import static controller.constants.AbilityConstants.*;
import static controller.constants.EntityConstants.SKILL_COOLDOWN_IN_MINUTES;
import static controller.constants.Variables.EPSILON_RADIUS;

public enum Skill {
    Ares, Astrape, Cerberus,
    Aceso, Melampus, Chiron,
    Proteus, Empusa, Dolus;

    public static Skill activeSkill = null;
    public boolean acquired = false;
    public long lastSkillTime = 0;

    public static void initializeSkills() {
        activeSkill = findSkill(Profile.getCurrent().activeSkillSaveName);
        CopyOnWriteArrayList<Skill> acquiredSkillSave = new CopyOnWriteArrayList<>();
        for (String skillName : Profile.getCurrent().acquiredSkillsNames) acquiredSkillSave.add(findSkill(skillName));
        for (Skill skill : acquiredSkillSave) skill.acquired = true;
    }

    public String getName() {
        return "WRIT OF " + name();
    }

    public int getCost() {
        return switch (this) {

            case Ares -> 750;
            case Astrape -> 1000;
            case Cerberus -> 1500;
            case Aceso -> 500;
            case Melampus -> 750;
            case Chiron -> 900;
            case Proteus -> 1000;
            case Empusa -> 750;
            case Dolus -> 1500;
        };
    }

    public SkillType getType() {
        return switch (this) {

            case Ares, Astrape, Cerberus -> SkillType.ATTACK;
            case Aceso, Melampus, Chiron -> SkillType.GUARD;
            case Proteus, Empusa, Dolus -> SkillType.POLYMORPHIA;
        };
    }

    public ActionListener getAction() {
        return switch (this) {

            case Ares -> e -> {
                Profile.getCurrent().EPSILON_MELEE_DAMAGE += (int) WRIT_OF_ARES_BUFF_AMOUNT.getValue();
                Profile.getCurrent().EPSILON_RANGED_DAMAGE += (int) WRIT_OF_ARES_BUFF_AMOUNT.getValue();
            };
            case Astrape -> e -> {
                //todo collision -> -2hp
            };
            case Cerberus -> e -> {
                //todo 3 circle hovering -> -10hp
            };
            case Aceso -> e -> {
                Timer healthTimer = new Timer((int) WRIT_OF_ACESO_HEALING_FREQUENCY.getValue(), null);
                healthTimer.addActionListener(e1 -> {
                    if (isGameRunning()) EpsilonModel.getINSTANCE().addHealth((int) WRIT_OF_ACESO_HEALING_AMOUNT.getValue());
                    if (!isGameOn()) healthTimer.stop();
                });
                healthTimer.start();
            };
            case Melampus -> e -> {
                //todo melee safe -> %5 chance
            };
            case Chiron -> e -> {
                //todo +3hp per damage
            };
            case Proteus -> e -> EpsilonModel.getINSTANCE().addVertex();
            case Empusa -> e -> {
                //todo this line those not work
                EPSILON_RADIUS = (int) ((1-0.1) * EPSILON_RADIUS);
            };
            case Dolus -> e -> {
                //todo 2 skill random
            };
        };
    }

    public void fire() {
        long now = System.nanoTime();
        if (now - lastSkillTime >= TimeUnit.MINUTES.toNanos(SKILL_COOLDOWN_IN_MINUTES.getValue())) {
            getAction().actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            lastSkillTime = now;
        }
    }

    public enum SkillType {
        ATTACK, GUARD, POLYMORPHIA
    }
}
