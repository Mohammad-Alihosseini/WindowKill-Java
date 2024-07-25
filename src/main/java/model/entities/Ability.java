package model.entities;

import model.characters.EpsilonModel;
import model.movement.Movement;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static controller.constants.AbilityConstants.*;
import static controller.constants.EntityConstants.EPSILON_SHOOTING_RAPIDITY;
import static model.collision.Collision.emitImpactWave;

public enum Ability {
    HEPHAESTUS, ATHENA, APOLLO,
    DEIMOS, HYPNOS, PHONOI;
    private long lastSkillTime = 0;

    public String getName() {
        return switch (this) {

            case HEPHAESTUS -> "O' HEPHAESTUS, BANISH";
            case ATHENA -> "O' ATHENA, EMPOWER";
            case APOLLO -> "O' APOLLO, HEAL";
            case DEIMOS -> "O’ Deimos, Dismay";
            case HYPNOS -> "O’ Hypnos, Slumber";
            case PHONOI -> "O’ Phonoi, Slaughter";
        };
    }

    public int getCost() {
        return switch (this) {

            case HEPHAESTUS -> 100;
            case ATHENA -> 75;
            case APOLLO -> 50;
            case DEIMOS -> 120;
            case HYPNOS -> 150;
            case PHONOI -> 200;
        };
    }

    public ActionListener getAction() {
        return switch (this) {

            case HEPHAESTUS ->
                    e -> emitImpactWave(EpsilonModel.getINSTANCE().getAnchor(), HEPHAESTUS_ABILITY_WAVE_POWER.getValue());
            case ATHENA -> e -> {
                EpsilonModel.getINSTANCE().setShootingRapidity((int) ATHENA_ABILITY_SHOOTING_RAPIDITY.getValue());
                Timer timer = new Timer((int) ATHENA_ABILITY_TIME.getValue(), e1 -> EpsilonModel.getINSTANCE().setShootingRapidity(EPSILON_SHOOTING_RAPIDITY.getValue()));
                timer.setRepeats(false);
                timer.start();
            };
            case APOLLO -> e -> EpsilonModel.getINSTANCE().addHealth((int) APOLLO_ABILITY_HEALING_AMOUNT.getValue());
            case DEIMOS -> null;
            case HYPNOS -> e -> {
                Movement.setHypnos(true);
                Timer timer = new Timer((int) HYPNOS_ABILITY_TIME.getValue(), e1 -> Movement.setHypnos(false));

                timer.setRepeats(false);
                timer.start();
            };
            case PHONOI -> e -> {
                long now = System.nanoTime();
                if (now - getLastSkillTime() >= TimeUnit.SECONDS.toNanos((int) PHONOI_COOLDOWN.getValue())) {
                    EpsilonModel.setPhonoi(true);
                    setLastSkillTime(now);
                }
            };
        };
    }

    public long getLastSkillTime() {
        return lastSkillTime;
    }

    public void setLastSkillTime(long lastSkillTime) {
        this.lastSkillTime = lastSkillTime;
    }
}
