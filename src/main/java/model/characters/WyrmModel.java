package model.characters;

import model.frames.MotionPanelModel;
import model.entities.AttackTypes;

import java.awt.*;
import java.awt.geom.Point2D;

import static controller.UserInterfaceController.createWyrm;
import static controller.constants.DimensionConstants.WYRM_DIMENSION;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.WYRM_VERTICES;
import static model.Utils.roundPoint;

public class WyrmModel extends GeoShapeModel {

    public WyrmModel(Point anchor) {
        super(new Point(0, 0), WYRM_VERTICES.getValue(), WYRM_HEALTH.getValue());

        Point2D motionPanelDim = new Point2D.Float(WYRM_DIMENSION.getValue().width, WYRM_DIMENSION.getValue().height);
        var motionPanelModel = new MotionPanelModel(anchor, motionPanelDim);
        String motionPanelId = motionPanelModel.getModelId();

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
}