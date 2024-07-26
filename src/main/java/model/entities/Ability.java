package model.entities;

import model.characters.Enemy;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.projectiles.EnemyBullet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static controller.constants.AbilityConstants.*;
import static controller.constants.EntityConstants.EPSILON_SHOOTING_RAPIDITY;
import static controller.constants.MovementConstants.*;
import static model.collision.Collision.emitImpactWave;

public enum Ability {
    HEPHAESTUS, ATHENA, APOLLO,
    DEIMOS, HYPNOS, PHONOI;
    private static final Random random = new Random();
    private long lastSkillTime = 0;

    private static void zeroVelocity(GeoShapeModel model) {
        model.getMovement().setAngularSpeed(0.001F);
        model.getMovement().setSpeedSave(0.001F);
        model.getMovement().setSpeed(0.001F);
        model.getMovement().setDeceleration(0.001F);
        model.getMovement().setDecay(0.001F);
    }

    @NotNull
    private static Timer HypnosTimer() {
        Timer timer = new Timer((int) HYPNOS_ABILITY_TIME.getValue(), e1 -> {
            for (GeoShapeModel model : GeoShapeModel.allShapeModelsList) {
                if ((model instanceof Enemy || model instanceof EnemyBullet)) {
                    model.getMovement().setAngularSpeed(random.nextFloat(-ANGULAR_SPEED_BOUND.getValue(), ANGULAR_SPEED_BOUND.getValue()));
                    model.getMovement().setSpeedSave(DEFAULT_SPEED.getValue());
                    model.getMovement().setSpeed(model.getMovement().getSpeedSave());
                    model.getMovement().setDeceleration(DEFAULT_DECELERATION.getValue());
                    model.getMovement().setDecay(DECELERATION_DECAY.getValue());
                }
            }
        }
        );

        timer.setRepeats(false);
        return timer;
    }

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
                for (GeoShapeModel model : GeoShapeModel.allShapeModelsList) {
                    if ((model instanceof Enemy || model instanceof EnemyBullet)) {
                        zeroVelocity(model);
                    }
                }
                Timer timer = HypnosTimer();
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
