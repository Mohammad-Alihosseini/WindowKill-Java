package model.movement;

import controller.TypedActionListener;
import model.characters.GeoShapeModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.findModel;
import static controller.constants.ImpactConstants.*;
import static controller.constants.MovementConstants.*;
import static model.Utils.*;

public class Movement {
    private final CopyOnWriteArrayList<ActionListener> moveListeners = new CopyOnWriteArrayList<>();
    public Point2D lastAnchor = new Point2D.Float(0, 0);
    public long lastAnchorUpdateTime = System.nanoTime();
    public long positionUpdateTimeDiffCapture = 0;
    public Point2D anchor = new Point2D.Float(0, 0);
    public Direction direction = new Direction(0, Direction.DirectionOrientation.stable);
    public float angularSpeed = new Random().nextFloat(-ANGULAR_SPEED_BOUND.getValue(), ANGULAR_SPEED_BOUND.getValue());
    public float speedSave = DEFAULT_SPEED.getValue();
    public float speed = speedSave;
    public float deceleration = DEFAULT_DECELERATION.getValue();
    public float decay = DECELERATION_DECAY.getValue();
    String modelId;
    Point2D target = null;
    String targetModelId = null;

    public Movement(String modelId, Point2D anchor) {
        this.modelId = modelId;
        setAnchor(anchor);
        moveListeners.add(new TypedActionListener.MoveActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnchor();
                move();
            }
        });
        moveListeners.add(new TypedActionListener.RotateActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRotation();
                rotate();
            }
        });
    }

    public void move() {
        GeoShapeModel model = findModel(modelId);
        if (model == null) return;
        model.moveShapeModel(getAnchor());
    }

    public void updateAnchor(float speed, Direction direction) {
        if (targetModelId != null) lockOnTarget(targetModelId);
        else if (target != null) lockOnTarget(target);
        GeoShapeModel model = findModel(modelId);
        if (model == null) return;
        Point2D destination = addUpPoints(getAnchor(), multiplyPoint(direction.getDirectionVector(), speed));
        if (!model.crossesUnmovable(destination)) setAnchor(destination);
    }

    public void updateAnchor() {
        updateAnchor(speed, direction);
    }

    public void updateRotation() {
        this.angularSpeed *= DEFAULT_ANGULAR_DECAY.getValue();
    }

    public void rotate() {
        GeoShapeModel model = findModel(modelId);
        if (model == null) return;
        model.rotateShapeModel(angularSpeed);
    }

    public void decelerate(Direction direction, float tempSpeed, float tempAcceleration) {
        moveListeners.removeIf(actionListener -> actionListener instanceof DecelerationWorker &&
                ((DecelerationWorker) actionListener).direction.equals(direction));
        moveListeners.add(new DecelerationWorker(direction, tempSpeed, tempAcceleration) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (speed > DECELERATION_SENSITIVITY.getValue()) {
                    speed += acceleration;
                    acceleration = acceleration >= 0 ? acceleration * decay : acceleration / decay;
                    updateAnchor(speed, direction);
                } else moveListeners.remove(this);
            }
        });
    }

    public void decelerate(Direction direction) {
        decelerate(direction, speed, deceleration);
    }

    public void impact(Direction direction, float tempSpeed, float tempAcceleration, float scale) {
        moveListeners.removeIf(actionListener -> actionListener instanceof DecelerationWorker);
        moveListeners.removeIf(actionListener -> actionListener instanceof TypedActionListener.ImpactActionListener);
        speed = speedSave;
        final Movement[] finalMovement = {this};
        final float[] finalTempSpeed = {tempSpeed * scale};
        final float[] finalTempAcc = {tempAcceleration * scale};
        moveListeners.add(new TypedActionListener.ImpactActionListener() {
            boolean speedDecreased = false;
            long timeDecreased = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!speedDecreased) {
                    finalMovement[0].speed /= Math.max(1.0001, IMPACT_DRIFT_FACTOR.getValue() * scale);
                    speedDecreased = true;
                    timeDecreased = System.nanoTime();
                } else if (System.nanoTime() - timeDecreased > IMPACT_COOLDOWN.getValue()) {
                    finalMovement[0].speed = finalMovement[0].speedSave;
                }
                if (finalTempSpeed[0] > DECELERATION_SENSITIVITY.getValue()) {
                    finalTempSpeed[0] += finalTempAcc[0];
                    finalTempAcc[0] = finalTempAcc[0] >= 0 ? finalTempAcc[0] * decay : finalTempAcc[0] / decay;
                    updateAnchor(finalTempSpeed[0], direction);
                } else {
                    moveListeners.remove(this);
                }
            }
        });
    }

    public void impact(Direction direction, float scale) {
        impact(direction, IMPACT_SPEED.getValue(), IMPACT_DECELERATION.getValue(), scale);
    }

    public void lockOnTarget(Point2D point) {
        if (point != null) {
            target = point;
            setDirection(new Direction(relativeLocation(point, getAnchor())));
        }
    }

    public void lockOnTarget(String targetModelId) {
        this.targetModelId = targetModelId;
        GeoShapeModel model = findModel(targetModelId);
        if (model != null) target = model.getMovement().getAnchor();
        lockOnTarget(target);
    }

    public void dampenDecelerations() {
        for (ActionListener actionListener : moveListeners) {
            if (actionListener instanceof DecelerationWorker) {
                ((DecelerationWorker) actionListener).acceleration *=
                        ((DecelerationWorker) actionListener).acceleration >= 0 ?
                                1 / DAMPEN_FACTOR.getValue() : DAMPEN_FACTOR.getValue();
            }
        }
    }

    public CopyOnWriteArrayList<ActionListener> getMoveListeners() {
        return moveListeners;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D point) {
        long now = System.nanoTime();
        if (now - lastAnchorUpdateTime >= POSITION_UPDATE_INTERVAL.getValue()) {
            lastAnchorUpdateTime = now;
            this.lastAnchor = deepClone(anchor);
        }
        this.anchor = new Point2D.Float((float) point.getX(), (float) point.getY());
    }

    public Point2D getLastAnchor() {
        return lastAnchor;
    }

    public abstract static class DecelerationWorker extends TypedActionListener.DecelerateActionListener {
        Direction direction;
        float speed;
        float acceleration;

        public DecelerationWorker(Direction direction, float speed, float acceleration) {
            this.direction = direction;
            this.speed = speed;
            this.acceleration = acceleration;
        }
    }
}
