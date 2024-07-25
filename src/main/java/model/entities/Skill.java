package model.entities;

import model.Profile;
import model.characters.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.*;
import static controller.constants.AbilityConstants.*;
import static controller.constants.EntityConstants.SKILL_COOLDOWN_IN_MINUTES;
import static model.collision.Collision.setWritOfAstrape;
import static model.entities.Entity.setWritOfChiron;
import static model.entities.Entity.setWritOfMelampus;

public enum Skill {
    Ares, Astrape, Cerberus,
    Aceso, Melampus, Chiron,
    Proteus, Empusa, Dolus;

    private static final Random RANDOM = new Random();
    private static final List<Skill> VALUES = Collections.unmodifiableList(Arrays.asList(Skill.values()));
    private static final int SIZE = VALUES.size();
    private static Skill activeSkill = null;
    private static Skill randomSkill1 = null;
    private static Skill randomSkill2 = null;
    private boolean acquired = false;
    private long lastSkillTime = 0;

    public static void initializeSkills() {
        setActiveSkill(findSkill(Profile.getCurrent().getActiveSkillSaveName()));
        CopyOnWriteArrayList<Skill> acquiredSkillSave = new CopyOnWriteArrayList<>();
        for (String skillName : Profile.getCurrent().getAcquiredSkillsNames())
            acquiredSkillSave.add(findSkill(skillName));
        for (Skill skill : acquiredSkillSave) skill.setAcquired(true);
    }

    public static Skill getActiveSkill() {
        return activeSkill;
    }

    public static void setActiveSkill(Skill activeSkill) {
        Skill.activeSkill = activeSkill;
    }

    public static Skill getRandomSkill1() {
        if (randomSkill1 == null) randomSkill1 = VALUES.get(RANDOM.nextInt(SIZE));
        return randomSkill1;
    }

    public static Skill getRandomSkill2() {
        if (randomSkill2 == null) {
            randomSkill2 = VALUES.get(RANDOM.nextInt(SIZE));
            while (randomSkill1 == randomSkill2) {
                randomSkill2 = VALUES.get(RANDOM.nextInt(SIZE));
            }
        }
        return randomSkill2;
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
                Profile.getCurrent().setEpsilonMeleeDamage((int) (Profile.getCurrent().getEpsilonMeleeDamage() + WRIT_OF_ARES_BUFF_AMOUNT.getValue()));
                Profile.getCurrent().setEpsilonRangedDamage((int) (Profile.getCurrent().getEpsilonRangedDamage() + WRIT_OF_ARES_BUFF_AMOUNT.getValue()));
            };
            case Astrape -> e -> setWritOfAstrape(true);
            case Cerberus -> e -> {
                //todo 3 circle hovering -> -10hp
            };
            case Aceso -> e -> {
                Timer healthTimer = new Timer((int) WRIT_OF_ACESO_HEALING_FREQUENCY.getValue(), null);
                healthTimer.addActionListener(e1 -> {
                    if (isGameRunning())
                        EpsilonModel.getINSTANCE().addHealth((int) WRIT_OF_ACESO_HEALING_AMOUNT.getValue());
                    if (!isGameOn()) healthTimer.stop();
                });
                healthTimer.start();
            };
            case Melampus -> e -> setWritOfMelampus(true);
            case Chiron -> e -> setWritOfChiron(true);
            case Proteus -> e -> EpsilonModel.getINSTANCE().addVertex();
            case Empusa -> e -> {
                //todo 0.9 * epsilon size
//                EpsilonModel.getINSTANCE().setAnchorSave(deepClone(MINI_EPSILON_CENTER.getValue()));
//                MotionPanelView.getMainMotionPanelView().shapeViews.remove(EpsilonView.);
//                createEpsilon();
                //EPSILON_RADIUS = (int) ((1-0.1) * EPSILON_RADIUS);
            };
            case Dolus -> e -> {
                System.out.println();
                getRandomSkill1().fire();
                getRandomSkill2().fire();
            };
        };
    }

    public void fire() {
        long now = System.nanoTime();
        if (now - getLastSkillTime() >= TimeUnit.MINUTES.toNanos(SKILL_COOLDOWN_IN_MINUTES.getValue())) {
            getAction().actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            setLastSkillTime(now);
        }
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }

    public long getLastSkillTime() {
        return lastSkillTime;
    }

    public void setLastSkillTime(long lastSkillTime) {
        this.lastSkillTime = lastSkillTime;
    }

    public enum SkillType {
        ATTACK, GUARD, POLYMORPHIA
    }
}
