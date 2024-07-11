package model.characters;

import model.Profile;
import model.entities.AttackTypes;
import model.projectiles.LongRanged;

import java.awt.*;

import static controller.constants.EntityConstants.ENEMY_SHOUTING_RAPIDITY;
import static controller.constants.EntityConstants.EntityVertices.SQUARANTINE_VERTICES;
import static controller.constants.EntityConstants.SQUARANTINE_HEALTH;

public class NecropickModel extends GeoShapeModel implements LongRanged {
    private int shootingRapidity = ENEMY_SHOUTING_RAPIDITY.getValue();


    public NecropickModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), SQUARANTINE_VERTICES.getValue(), SQUARANTINE_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.MELEE, Profile.getCurrent().getEpsilonMeleeDamage());
        getDamageSize().put(AttackTypes.RANGED, Profile.getCurrent().getEpsilonRangedDamage());
//        createSquarantine(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(1);
        setCollectibleValue(5);
    }

    @Override
    public int getShootingRapidity() {
        return shootingRapidity;
    }

    @Override
    public void setShootingRapidity(int shootingRapidity) {
        this.shootingRapidity = shootingRapidity;
    }
}