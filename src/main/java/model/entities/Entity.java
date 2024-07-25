package model.entities;

import model.characters.CollectibleModel;
import model.characters.Enemy;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.movement.Movable;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static controller.UserInterfaceController.*;
import static controller.constants.AbilityConstants.WRIT_OF_CHIRON_HEALING_AMOUNT;
import static controller.constants.ImpactConstants.MELEE_COOLDOWN;
import static model.characters.CollectibleModel.bulkCreateCollectibles;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static model.collision.Collidable.collidables;

public abstract class Entity {
    private static final Random random = new Random();
    private static boolean WritOfMelampus = false;
    private static boolean WritOfChiron = false;
    private final ConcurrentMap<AttackTypes, Integer> damageSize = new ConcurrentHashMap<>();
    private int health;
    private int fullHealth;
    private boolean vulnerable;
    private int numberOfCollectibles = 0;
    private int collectibleValue = 0;
    private long lastMeleeTime = 0;

    public static boolean isWritOfChiron() {
        return WritOfChiron;
    }

    public static void setWritOfChiron(boolean writOfChiron) {
        WritOfChiron = writOfChiron;
    }

    public abstract String getModelId();

    public abstract String getMotionPanelId();

    public void damage(Entity entity, AttackTypes attackType) {
        long now = System.nanoTime();
        int rnd = random.nextInt(1, 21);
        if (isWritOfMelampus() && entity instanceof EpsilonModel && rnd == 3) return;
        if (isWritOfChiron() && entity instanceof Enemy && this instanceof EpsilonModel)
            setHealth(getHealth() + (int) WRIT_OF_CHIRON_HEALING_AMOUNT.getValue());
        if (now - getLastMeleeTime() >= MELEE_COOLDOWN.getValue()) {
            if (entity.isVulnerable()) {
                entity.setHealth(entity.getHealth() - getDamageSize().get(attackType));
                if (entity.getHealth() <= 0) {
                    entity.eliminate();
                    if (entity instanceof CollectibleModel) playXPSoundEffect();
                    else playDownSoundEffect();
                } else playHitSoundEffect();
            }
            setLastMeleeTime(now);
        }
    }

    // just used for Writ of Astrape and damage to enemies not epsilon
    public void damage(Entity entity, int damageSize) {
        long now = System.nanoTime();
        if (now - getLastMeleeTime() >= MELEE_COOLDOWN.getValue()) {
            if (entity.isVulnerable()) {
                entity.setHealth(entity.getHealth() - damageSize);
                if (entity.getHealth() <= 0) {
                    entity.eliminate();
                    if (entity instanceof CollectibleModel) playXPSoundEffect();
                    else playDownSoundEffect();
                } else playHitSoundEffect();
            }
            setLastMeleeTime(now);
        }
    }

    public void eliminate() {
        if (this instanceof GeoShapeModel geoShapeModel) {
            bulkCreateCollectibles((GeoShapeModel) this);
            allShapeModelsList.remove(this);
            collidables.remove(geoShapeModel);
            Movable.movables.remove(geoShapeModel);
            eliminateView(getModelId(), getMotionPanelId());
        }
    }

    public void addHealth(int units) {
        this.setHealth(Math.min(getFullHealth(), getHealth() + units));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getFullHealth() {
        return fullHealth;
    }

    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public int getNumberOfCollectibles() {
        return numberOfCollectibles;
    }

    public void setNumberOfCollectibles(int numberOfCollectibles) {
        this.numberOfCollectibles = numberOfCollectibles;
    }

    public int getCollectibleValue() {
        return collectibleValue;
    }

    public void setCollectibleValue(int collectibleValue) {
        this.collectibleValue = collectibleValue;
    }

    public ConcurrentMap<AttackTypes, Integer> getDamageSize() {
        return damageSize;
    }

    public long getLastMeleeTime() {
        return lastMeleeTime;
    }

    public void setLastMeleeTime(long lastMeleeTime) {
        this.lastMeleeTime = lastMeleeTime;
    }

    public boolean isWritOfMelampus() {
        return WritOfMelampus;
    }

    public static void setWritOfMelampus(boolean newWritOfMelampus) {
        WritOfMelampus = newWritOfMelampus;
    }
}
