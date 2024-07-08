package model;

import controller.TypedActionListener;
import model.collision.Collidable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static controller.UserInterfaceController.createMotionPanel;
import static controller.constants.DimensionConstants.Dimension2DConstants.DEFORM_DIMENSION;
import static controller.constants.DimensionConstants.SCREEN_SIZE;
import static controller.constants.MovementConstants.POSITION_UPDATE_INTERVAL;
import static controller.constants.ShrinkConstants.*;
import static model.Utils.*;

public class MotionPanelModel implements Collidable {
    public volatile static MotionPanelModel mainMotionPanelModel;
    public volatile static CopyOnWriteArrayList<MotionPanelModel> allMotionPanelModelsList = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<ActionListener> deformationListeners = new CopyOnWriteArrayList<>();
    public String modelId;
    public Point2D location;
    public long lastPositionUpdateTime = System.nanoTime();
    public long positionUpdateTimeDiffCapture = 0;
    /**
     * Since there is not a well-structured non-abstract class for high-precision {@link Dimension} in Java/JTS the choice is
     * made to use an instance of Point2D instead of Dimension to do the floating-point arithmetic and calculations
     * of motion panel to avoid stack-up of quantitative errors throughout rapid calls
     */
    public Point2D dimension;
    Geometry geometry;
    Point2D lastLocation;
    Point2D lastDimension;

    public MotionPanelModel(Point2D location, Point2D dimension) {
        if (mainMotionPanelModel == null) mainMotionPanelModel = this;
        this.modelId = UUID.randomUUID().toString();
        allMotionPanelModelsList.add(this);
        setPosition(location, dimension);
        Collidable.collidables.add(this);
        createMotionPanel(modelId, dimension, location);

        new Timer((int) SHRINK_DELAY.getValue(), e -> {
            if (!hasShrinkType(TypedActionListener.Side.CENTER)) deform(DEFORM_DIMENSION.getValue(), TypedActionListener.Side.CENTER, SHRINK_SPEED.getValue());
        }).start();
    }

    public MotionPanelModel(Point2D dimension) {
        this(new Point2D.Float((float) ((SCREEN_SIZE.getValue().width - dimension.getX()) / 2),
                (float) ((SCREEN_SIZE.getValue().height - dimension.getY()) / 2)), dimension);
    }

    /**
     * @param distance the total threshold
     * @param velocity momentary velocity of progress
     * @return value of progress (based on the momentary velocity), avoiding surpass of the threshold
     */
    public static float evaluateProgress(float distance, float velocity) {
        float prog = distance * velocity;
        prog = Math.abs(prog) < Math.abs(distance) ? prog : distance;
        return prog;
    }

    /**
     * Justifies the position of panel after resize process
     *
     * @param sizeOffset width-height change in size of the motion panel
     * @param side       the direction in which motion panel should extend
     * @return the required displacement for motion panel to visualize sided motion
     */
    public static Point2D positionJustify(Point2D sizeOffset, TypedActionListener.Side side) {
        switch (side) {
            case LEFT -> {
                return new Point2D.Float((float) sizeOffset.getX(), (float) (sizeOffset.getY() / 2));
            }
            case RIGHT -> {
                return new Point2D.Float(0, (float) sizeOffset.getY() / 2);
            }
            case TOP -> {
                return new Point2D.Float((float) sizeOffset.getX() / 2, (float) sizeOffset.getY());
            }
            case BOTTOM -> {
                return new Point2D.Float((float) sizeOffset.getX() / 2, 0);
            }
            case CENTER -> {
                return new Point2D.Float((float) sizeOffset.getX() / 2, (float) sizeOffset.getY() / 2);
            }
            default -> {
                return new Point2D.Float(0, 0);
            }
        }
    }

    /**
     * Checks if a given motion panel fits in the screen
     * <p>Note : call to this method is not mandatory for frames except than Epsilon's local frame
     *
     * @param location a point indicating position of the anchor (TL corner)
     * @param size     the dimension of the motion panel
     */
    public static boolean isInScreen(Point2D location, Point2D size) {
        boolean x = 0 <= location.getX();
        boolean y = 0 <= location.getY();
        boolean width = location.getX() + size.getX() <= SCREEN_SIZE.getValue().width;
        boolean height = location.getY() + size.getY() <= SCREEN_SIZE.getValue().height;
        return x && y && width && height;
    }

    /**
     * <p>1- Calculates the thresholds</p>
     * <p>2- Adds a {@link TypedActionListener.ShrinkActionListener} to the list of deformationListeners</p>
     * <p>3- Stops the process if it's a center shrinking unless there is no other shrinks</p>
     * <p>4- Until thresholds are not met, evaluates the progress in width-height and justifies the position</p>
     * <p>5- If motion panel hits the boundary of screen, terminates the process</p>
     *
     * @param finalSize desired size of motion panel
     * @param scale     rate of progression
     * @param side      direction of progression
     * @apiNote Step 5 is not mandatory for frames that are not local to any inertial entity
     */
    public void deform(Point2D finalSize, TypedActionListener.Side side, float scale) {
        AtomicReference<Float> momentaryVelocity = new AtomicReference<>(scale * DEFORM_VELOCITY.getValue());
        final float[] distanceW = {(float) (dimension.getX() - finalSize.getX())};
        final float[] distanceH = {(float) (dimension.getY() - finalSize.getY())};
        deformationListeners.removeIf(actionListener ->
                actionListener instanceof TypedActionListener.ShrinkActionListener && ((TypedActionListener.ShrinkActionListener) actionListener).side == side);

        deformationListeners.add(new TypedActionListener.ShrinkActionListener(side) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean interrupt = false;
                for (Side side1 : Side.values())
                    if (side1 != Side.CENTER && hasShrinkType(side1)) interrupt = true;
                interrupt = side == Side.CENTER && interrupt;

                if (!interrupt && (Math.abs(distanceW[0]) > SHRINK_THRESHOLD_SENSITIVITY.getValue() || Math.abs(distanceH[0]) > SHRINK_THRESHOLD_SENSITIVITY.getValue())) {
                    float progW = evaluateProgress(distanceW[0], momentaryVelocity.get());
                    float progH = evaluateProgress(distanceH[0], momentaryVelocity.get());
                    Point2D newDimension = new Point2D.Float((float) (dimension.getX() - progW), (float) (dimension.getY() - progH));
                    Point2D offset = positionJustify(new Point2D.Float(progW, progH), side);

                    Point2D newLocation = addUpPoints(location, offset);
                    if (isInScreen(newLocation, newDimension)) {
                        setPosition(newLocation, newDimension);
                        distanceW[0] -= progW;
                        distanceH[0] -= progH;
                        if (momentaryVelocity.get() + SHRINK_DECELERATION.getValue() >= DEFORM_SPEED_SENSITIVITY.getValue())
                            momentaryVelocity.updateAndGet(v -> v + SHRINK_DECELERATION.getValue());
                    } else deformationListeners.remove(this);
                } else deformationListeners.remove(this);
            }
        });
    }

    /**
     * Extends the motion panel in the appropriate direction
     *
     * @param point the point of collision of the enforcing entity with the boundary of motion panel
     * @see #detectCollisionSides(Point)
     */
    public void extend(Point point) {
        TypedActionListener.Side[] collisionSides = detectCollisionSides(point);
        if (collisionSides[0] != null) deform(new Point2D.Float((float) (dimension.getX() + EXTENSION_LENGTH.getValue()),
                (float) dimension.getY()), collisionSides[0], EXTEND_SPEED.getValue());
        if (collisionSides[1] != null) deform(new Point2D.Float((float) dimension.getX(), (float) (dimension.getY() + EXTENSION_LENGTH.getValue())),
                collisionSides[1], EXTEND_SPEED.getValue());
    }

    /**
     * Detects the sides from which the motion panel should be extended
     *
     * @param point the point of collision of the enforcing entity with the boundary of motion panel
     */
    public TypedActionListener.Side[] detectCollisionSides(Point point) {
        float x_distance = (float) Math.min(Math.abs(location.getX() + dimension.getX() - point.x), Math.abs(point.x - location.getX()));
        float y_distance = (float) Math.min(Math.abs(location.getY() + dimension.getY() - point.y), Math.abs(point.y - location.getY()));
        TypedActionListener.Side sideX = null;
        TypedActionListener.Side sideY = null;
        if (Math.abs(x_distance) < DEFORM_SENSITIVITY.getValue()) sideX = (location.getX() + dimension.getX() - point.x) >= (point.x - location.getX()) ?
                TypedActionListener.Side.LEFT : TypedActionListener.Side.RIGHT;
        if (Math.abs(y_distance) < DEFORM_SENSITIVITY.getValue()) sideY = (location.getY() + dimension.getY() - point.y) >= (point.y - location.getY()) ?
                TypedActionListener.Side.TOP : TypedActionListener.Side.BOTTOM;
        return new TypedActionListener.Side[]{sideX, sideY};
    }

    /**
     * Checks if there is an ongoing shrink process of a certain kind
     *
     * @param side the direction of shrink process
     */
    public boolean hasShrinkType(TypedActionListener.Side side) {
        for (ActionListener actionListener : deformationListeners) {
            if (actionListener instanceof TypedActionListener.ShrinkActionListener
                    && ((TypedActionListener.ShrinkActionListener) actionListener).side == side) return true;
        }
        return false;
    }

    public void setPosition(Point2D location, Point2D dimension) {
        long now = System.nanoTime();
        if (now - lastPositionUpdateTime >= POSITION_UPDATE_INTERVAL.getValue()) {
            lastPositionUpdateTime = now;
            this.lastLocation = deepClone(location);
            this.lastDimension = deepClone(dimension);
        }
        this.location = location;
        this.dimension = dimension;
    }

    public String getModelId() {
        return modelId;
    }

    @Override
    public void createGeometry() {
        geometry = new GeometryFactory().createLineString(new Coordinate[]{
                new Coordinate(location.getX(), location.getY()),
                new Coordinate(location.getX() + dimension.getX(), location.getY()),
                new Coordinate(location.getX() + dimension.getX(), location.getY() + dimension.getY()),
                new Coordinate(location.getX(), location.getY() + dimension.getY()),
                new Coordinate(location.getX(), location.getY())});
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public float getRadius() {
        return 0;
    }

    @Override
    public Point2D getAnchor() {
        return null;
    }

    @Override
    public Point2D getLastAnchor() {
        return null;
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return true;
    }

    @Override
    public long getPositionUpdateTimeDiffCapture() {
        return this.positionUpdateTimeDiffCapture;
    }

    @Override
    public void setPositionUpdateTimeDiffCapture(long time) {
        this.positionUpdateTimeDiffCapture = time;
    }

    @Override
    public long getLastPositionUpdateTime() {
        return this.lastPositionUpdateTime;
    }

    @Override
    public void setLastPositionUpdateTime(long time) {
        this.lastPositionUpdateTime = time;
    }

    @Override
    public Point2D getMovementVector(Point2D collisionPoint) {
        TypedActionListener.Side[] sides = detectCollisionSides(roundPoint(collisionPoint));
        TypedActionListener.Side collisionSide = sides[0] != null ? sides[0] : sides[1];
        Point2D out = new Point2D.Float(0, 0);
        switch (collisionSide) {
            case LEFT -> out = new Point2D.Float((float) (location.getX() - lastLocation.getX()), 0);
            case RIGHT -> out = new Point2D.Float((float) (location.getX() + dimension.getX() - lastLocation.getX() - lastDimension.getX()), 0);
            case TOP -> out = new Point2D.Float(0, (float) (location.getY() - lastLocation.getY()));
            case BOTTOM -> out = new Point2D.Float(0, (float) (location.getY() + dimension.getY() - lastLocation.getY() - lastDimension.getY()));
        }
        return out;
    }
}
