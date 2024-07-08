package model.entities;

import model.characters.CollectibleModel;
import model.characters.GeoShapeModel;

import java.util.concurrent.ConcurrentHashMap;

import static controller.UserInterfaceController.*;
import static controller.constants.ImpactConstants.MELEE_COOLDOWN;
import static model.characters.CollectibleModel.bulkCreateCollectibles;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static model.collision.Collidable.collidables;

public abstract class Entity {
    public int health;
    public int fullHealth;
    public boolean vulnerable;
    public int numberOfCollectibles = 0;
    public int collectibleValue = 0;
    public ConcurrentHashMap<AttackTypes, Integer> damageSize = new ConcurrentHashMap<>();
    private long lastMeleeTime = 0;

    protected abstract String getModelId();

    protected abstract String getMotionPanelId();

    public void damage(Entity entity, AttackTypes attackType) {
        long now = System.nanoTime();
        if (now - lastMeleeTime >= MELEE_COOLDOWN.getValue()) {
            if (entity.vulnerable) {
                entity.health -= damageSize.get(attackType);
                if (entity.health <= 0) {
                    entity.eliminate();
                    if (entity instanceof CollectibleModel) playXPSoundEffect();
                    else playDownSoundEffect();
                } else playHitSoundEffect();
            }
            lastMeleeTime = now;
        }
    }

    public void eliminate() {
        if (this instanceof GeoShapeModel) {
            bulkCreateCollectibles((GeoShapeModel) this);
            allShapeModelsList.remove(this);
            collidables.remove(this);
            eliminateView(getModelId(), getMotionPanelId());
        }
    }

    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

}
