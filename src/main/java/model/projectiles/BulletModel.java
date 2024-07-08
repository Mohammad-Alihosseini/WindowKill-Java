package model.projectiles;

import model.characters.GeoShapeModel;
import model.entities.AttackTypes;

import java.awt.*;

import static controller.UserInterfaceController.createBullet;
import static controller.constants.EntityConstants.BULLET_HEALTH;
import static controller.constants.EntityConstants.EntityVertices.BULLET_VERTICES;
import static controller.constants.EntityConstants.PointConstants.BULLET_CENTER;
import static controller.constants.MovementConstants.BULLET_SPEED;
import static model.Utils.deepClone;
import static model.Utils.roundPoint;

public class BulletModel extends GeoShapeModel {
    public BulletModel(Point anchor, String motionPanelId, int damage) {
        super(new Point(0, 0), BULLET_VERTICES.getValue(), BULLET_HEALTH.getValue());
        this.isCircular = true;
        this.motionPanelId = motionPanelId;
        this.anchorSave = deepClone(BULLET_CENTER.getValue());
        assert anchorSave != null;
        createBullet(modelId, roundPoint(anchorSave), motionPanelId);
        moveShapeModel(anchor);
        movement.setAnchor(anchor);
        movement.angularSpeed = 0;
        movement.speed = BULLET_SPEED.getValue();
        movement.speedSave = BULLET_SPEED.getValue();
        damageSize.put(AttackTypes.MELEE, damage);
    }
}
