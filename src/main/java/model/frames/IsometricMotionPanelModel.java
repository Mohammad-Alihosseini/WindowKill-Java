package model.frames;

import controller.TypedActionListener;
import model.collision.Collidable;
import model.movement.Movement;
import model.movement.Translatable;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.DimensionConstants.Dimension2DConstants.DEFORM_DIMENSION;
import static controller.constants.ShrinkConstants.SHRINK_DELAY;
import static controller.constants.ShrinkConstants.SHRINK_SCALE;
import static model.Utils.addUpPoints;
import static model.Utils.deepCloneList;

public class IsometricMotionPanelModel extends MotionPanelModel implements Translatable {
    private final List<Point2D> vertices = new CopyOnWriteArrayList<>();
    private final List<Point2D> verticesSave = new CopyOnWriteArrayList<>();
    private final Movement movement;
    private float totalRotation = 0;
    private Point2D anchorSave;

    public IsometricMotionPanelModel(Point2D location, Point2D dimension, List<Point2D> vertices) {
        super(location, dimension, dimension);
        setVerticesSave(vertices);
        movement = new Movement(getModelId(), location);
        setAnchorSave(getCenter());
        moveShapeModel(location);
        getMovement().setAnchor(location);
        Translatable.translatable.add(this);

        // createIsometricMotionPanel(getModelId(), dimension, location);
        // todo extends toward epsilon
        // todo remove unnecessary changes
        new Timer((int) SHRINK_DELAY.getValue(), e -> {
            if (!hasShrinkType(TypedActionListener.Side.CENTER)) {
                deform(DEFORM_DIMENSION.getValue(), TypedActionListener.Side.CENTER, SHRINK_SCALE.getValue());
            }
        }).start();
    }

    public Point2D getCenter() {
        Point2D sumPoint = new Point2D.Float(0, 0);
        for (Point2D vertex : getVertices()) sumPoint = addUpPoints(sumPoint, vertex);
        return new Point2D.Float(
                (float) sumPoint.getX() / getVertices().size(),
                (float) sumPoint.getY() / getVertices().size()
        );
    }

    @Override
    public Point2D getMovementVector(Point2D collisionPoint) {
        return collisionPoint;
    }

    @Override
    public List<Point2D> getVertices() {
        return vertices;
    }

    @Override
    public List<Point2D> getVerticesSave() {
        return verticesSave;
    }

    public void setVerticesSave(List<Point2D> verticesSave) {
        this.vertices.clear();
        this.vertices.addAll(deepCloneList(verticesSave));
        this.verticesSave.clear();
        this.verticesSave.addAll(deepCloneList(verticesSave));
        createGeometry();
    }

    @Override
    public Movement getMovement() {
        return movement;
    }

    @Override
    public Point2D getAnchorSave() {
        return anchorSave;
    }

    public void setAnchorSave(Point2D anchorSave) {
        this.anchorSave = anchorSave;
    }

    @Override
    public float getTotalRotation() {
        return totalRotation;
    }

    @Override
    public void setTotalRotation(float totalRotation) {
        this.totalRotation = totalRotation;
    }

    public Point2D getAnchor() {
        return getMovement().getAnchor();
    }

    public boolean crossesUnmovable(Point2D anchorLocation) {
        for (Collidable collidable : collidables) {
            boolean shouldCollide = this.collide(collidable) && collidable.collide(this);
            if (!(collidable instanceof Translatable) && shouldCollide && willCross(collidable, anchorLocation))
                return true;
        }
        return false;
    }
}

