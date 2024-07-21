package model.characters;

import model.collision.Collidable;
import model.entities.AttackTypes;
import model.frames.IsometricMotionPanelModel;
import model.frames.MotionPanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.createWyrm;
import static controller.constants.DimensionConstants.WYRM_DIMENSION;
import static controller.constants.EntityConstants.EntityVertices.WYRM_VERTICES;
import static controller.constants.EntityConstants.WYRM_HEALTH;
import static controller.constants.EntityConstants.WYRM_RANGED;
import static model.Utils.roundPoint;

public class WyrmModel extends GeoShapeModel {
    IsometricMotionPanelModel isometricMotionPanelModel;

    public WyrmModel(Point anchor) {
        super(new Point(0, 0), WYRM_VERTICES.getValue(), WYRM_HEALTH.getValue());

        setPanel(anchor);
        String motionPanelId = isometricMotionPanelModel.getModelId();

        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.RANGED, WYRM_RANGED.getValue());
        createWyrm(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(2);
        setCollectibleValue(8);

    }

    private void setPanel(Point anchor) {
        Point2D motionPanelDim = new Point2D.Float(
                1.5F * WYRM_DIMENSION.getValue().width,
                1.5F * WYRM_DIMENSION.getValue().height
        );

        Point2D location = new Point2D.Float(
                anchor.x - WYRM_DIMENSION.getValue().width / 2F,
                anchor.y - WYRM_DIMENSION.getValue().height / 2F
        );

        List<Point2D> vertices = new CopyOnWriteArrayList<>(
                List.of(
                        new Point2D.Float(0, 0),
                        new Point2D.Float(0, (float) motionPanelDim.getY()),
                        new Point2D.Float((float) motionPanelDim.getX(), 0),
                        new Point2D.Float((float) motionPanelDim.getX(), (float) motionPanelDim.getY())
                )
        );

        isometricMotionPanelModel = new IsometricMotionPanelModel(location, motionPanelDim, vertices);
    }

    @Override
    public boolean collide(Collidable collidable) {
        return (collidable instanceof MotionPanelModel);
    }

    public IsometricMotionPanelModel getIsometricMotionPanelModel() {
        return isometricMotionPanelModel;
    }
}