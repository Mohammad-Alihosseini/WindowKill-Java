package model.characters;

import model.entities.AttackTypes;

import java.awt.*;

import static controller.UserInterfaceController.createTrigorath;
import static controller.constants.EntityConstants.EntityVertices.TRIGORATH_VERTICES;
import static controller.constants.EntityConstants.TRIGORATH_HEALTH;
import static controller.constants.EntityConstants.TRIGORATH_MELEE_DAMAGE;
import static model.Utils.roundPoint;

public class TrigorathModel extends GeoShapeModel {
    public TrigorathModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), TRIGORATH_VERTICES.getValue(), TRIGORATH_HEALTH.getValue());
        this.isCircular = false;
        this.motionPanelId = motionPanelId;
        this.anchorSave = getCenter();
        damageSize.put(AttackTypes.MELEE, TRIGORATH_MELEE_DAMAGE.getValue());
        createTrigorath(modelId, roundPoint(anchorSave), motionPanelId);
        moveShapeModel(anchor);
        movement.setAnchor(anchor);
        numberOfCollectibles = 2;
        collectibleValue = 5;
    }
}
