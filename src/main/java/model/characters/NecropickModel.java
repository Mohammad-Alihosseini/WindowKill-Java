package model.characters;

import model.entities.AttackTypes;
import model.projectiles.LongRanged;

import java.awt.*;

import static controller.UserInterfaceController.createNecropick;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.NECROPICK_VERTICES;
import static model.Utils.roundPoint;

public class NecropickModel extends GeoShapeModel implements LongRanged {
    private int shootingRapidity = ENEMY_SHOUTING_RAPIDITY.getValue();


    public NecropickModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), NECROPICK_VERTICES.getValue(), NECROPICK_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.RANGED, NECROPICK_RANGED.getValue());
        createNecropick(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(4);
        setCollectibleValue(2);
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