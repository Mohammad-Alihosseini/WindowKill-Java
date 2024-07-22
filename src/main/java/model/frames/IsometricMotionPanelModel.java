package model.frames;

import controller.TypedActionListener;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static controller.UserInterfaceController.findModel;
import static controller.constants.MovementConstants.MOTION_PANEL_SPEED;

public class IsometricMotionPanelModel extends MotionPanelModel {
    public IsometricMotionPanelModel(Point2D location, Point2D dimension) {
        super(location, dimension, dimension);
        // todo extend and shrink should be revised
        new Timer((int) MOTION_PANEL_SPEED.getValue(), e -> {
            extend(lockOnTarget(EpsilonModel.getINSTANCE().getModelId()));
            shrink(lockOnTarget(EpsilonModel.getINSTANCE().getModelId()));
        }).start();
    }

    private Point lockOnTarget(String targetModelId) {
        GeoShapeModel model = findModel(targetModelId);
        if (model != null)
            return new Point(
                    (int) model.getMovement().getAnchor().getX(),
                    (int) model.getMovement().getAnchor().getY()
            );
        return new Point(0, 0);
    }

    @Override
    public Point2D getMovementVector(Point2D collisionPoint) {
        return collisionPoint;
    }

    @Override
    public TypedActionListener.Side[] detectCollisionSides(Point point) {
        TypedActionListener.Side sideX;
        TypedActionListener.Side sideY;
        sideX = (getLocation().getX() + getDimension().getX() - point.x) >= (point.x - getLocation().getX()) ?
                TypedActionListener.Side.LEFT : TypedActionListener.Side.RIGHT;
        sideY = (getLocation().getY() + getDimension().getY() - point.y) >= (point.y - getLocation().getY()) ?
                TypedActionListener.Side.TOP : TypedActionListener.Side.BOTTOM;
        return new TypedActionListener.Side[]{sideX, sideY};
    }
}

