package model.characters;

import model.entities.AttackTypes;

import java.awt.*;

import static controller.UserInterfaceController.createSquarantine;
import static controller.constants.EntityConstants.EntityVertices.SQUARANTINE_VERTICES;
import static controller.constants.EntityConstants.SQUARANTINE_HEALTH;
import static controller.constants.EntityConstants.SQUARANTINE_MELEE_DAMAGE;
import static model.Utils.roundPoint;

public class SquarantineModel extends GeoShapeModel {

    public SquarantineModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), SQUARANTINE_VERTICES.getValue(), SQUARANTINE_HEALTH.getValue());
        this.isCircular = false;
        this.motionPanelId = motionPanelId;
        this.anchorSave = getCenter();
        damageSize.put(AttackTypes.MELEE, SQUARANTINE_MELEE_DAMAGE.getValue());
        createSquarantine(modelId, roundPoint(anchorSave), motionPanelId);
        moveShapeModel(anchor);
        movement.setAnchor(anchor);
        numberOfCollectibles = 1;
        collectibleValue = 5;
    }
}