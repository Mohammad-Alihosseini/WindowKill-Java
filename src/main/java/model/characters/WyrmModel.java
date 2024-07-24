package model.characters;

import model.collision.Collidable;
import model.entities.AttackTypes;
import model.frames.IsometricMotionPanelModel;
import model.movement.Direction;
import model.projectiles.LongRanged;
import model.projectiles.ShooterEntity;
import view.containers.MotionPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.createWyrm;
import static controller.constants.DimensionConstants.WYRM_DIMENSION;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.WYRM_VERTICES;
import static model.Utils.relativeLocation;
import static model.Utils.roundPoint;

public class WyrmModel extends GeoShapeModel implements LongRanged, Enemy {
    private final Timer timer;
    IsometricMotionPanelModel isometricMotionPanelModel;

    public WyrmModel(Point anchor) {
        super(new Point(0, 0), WYRM_VERTICES.getValue(), WYRM_HEALTH.getValue());

        setPanel(anchor);
        String motionPanelId = isometricMotionPanelModel.getModelId();

        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.RANGED, WYRM_RANGED.getValue());
        getDamageSize().put(AttackTypes.MELEE, WYRM_RANGED.getValue());
        createWyrm(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(2);
        setCollectibleValue(8);

        timer = new Timer((int) TimeUnit.SECONDS.toMillis(ENEMY_SHOUTING_RAPIDITY.getValue()), e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) activateShooting();
            //        toggleMotionPanelView
        });
        timer.start();
    }

    private void setPanel(Point anchor) {
        Point2D motionPanelDim = new Point2D.Float(
                2 * WYRM_DIMENSION.getValue().width,
                2 * WYRM_DIMENSION.getValue().height
        );

        Point2D location = new Point2D.Float(
                anchor.x - WYRM_DIMENSION.getValue().width / 2F,
                anchor.y - WYRM_DIMENSION.getValue().height / 2F
        );

        isometricMotionPanelModel = new IsometricMotionPanelModel(location, motionPanelDim);
    }

    private void activateShooting() {
        Point2D epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
        Point2D relativeLocation = relativeLocation(epsilonAnchor, roundPoint(getMovement().getAnchor()));
        hoveringShoot(this, new Direction(relativeLocation),
                getDamageSize().get(AttackTypes.RANGED), ShooterEntity.Omenoct);
    }

    @Override
    public boolean collide(Collidable collidable) {
        return true;
//        return collidable instanceof GeoShapeModel;
//
////        return (collidable instanceof MotionPanelModel);
    }

    @Override
    public void eliminate() {
        timer.stop();
        super.eliminate();
    }

    @Override
    public int getShootingRapidity() {
        return 0;
    }

    @Override
    public void setShootingRapidity(int shootingRapidity) {

    }
}