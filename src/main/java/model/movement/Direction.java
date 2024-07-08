package model.movement;

import controller.constants.DefaultMethods;

import java.awt.geom.Point2D;

import static controller.constants.ImpactConstants.DIRECTION_SENSITIVITY;
import static model.Utils.deepCopy;
import static model.Utils.multiplyPoint;

public class Direction implements Cloneable {
    public float directionSlope;
    public DirectionOrientation direction;
    public boolean isUpside = false;
    public boolean isDownside = false;

    public Direction(float directionSlope, DirectionOrientation direction) {
        this.directionSlope = directionSlope;
        this.direction = direction;
    }

    public Direction(Point2D point) {
        if ((point.getX() == 0 || Math.abs(point.getY() / point.getX()) > 1 / DIRECTION_SENSITIVITY.getValue()) && point.getY() > 0) {
            this.directionSlope = 0;
            this.direction = DirectionOrientation.positive;
            isUpside = true;
        } else if ((point.getX() == 0 || Math.abs(point.getY() / point.getX()) > 1 / DIRECTION_SENSITIVITY.getValue()) && point.getY() < 0) {
            this.directionSlope = 0;
            this.direction = DirectionOrientation.positive;
            isDownside = true;
        } else if (point.getX() == 0 || Math.abs(point.getY() / point.getX()) > 1 / DIRECTION_SENSITIVITY.getValue()) {
            this.directionSlope = 0;
            this.direction = DirectionOrientation.stable;
        } else {
            this.directionSlope = (float) (point.getY() / point.getX());
            if (point.getX() > 0) this.direction = DirectionOrientation.positive;
            else this.direction = DirectionOrientation.negative;
        }
    }

    public Direction(float angle) {
        float newAngle = (float) (angle - Math.floor(angle / 360) * 360);
        deepCopy(new Direction(new Point2D.Float((float) DefaultMethods.cosTable[(int) newAngle], (float) DefaultMethods.sinTable[(int) newAngle])), this);
    }

    public Point2D getDirectionVector() {
        if (direction == DirectionOrientation.stable) return new Point2D.Float(0, 0);
        if (isDownside) return new Point2D.Float(0, -1);
        if (isUpside) return new Point2D.Float(0, 1);

        float normalScale = (float) Math.sqrt(1 / (1 + directionSlope * directionSlope));
        if (direction == DirectionOrientation.positive) return multiplyPoint(new Point2D.Float(1, directionSlope), normalScale);
        if (direction == DirectionOrientation.negative) return multiplyPoint(new Point2D.Float(-1, -directionSlope), normalScale);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direction direction1)) return false;
        return Float.compare(direction1.directionSlope, directionSlope) == 0 && isUpside == direction1.isUpside &&
                isDownside == direction1.isDownside && direction == direction1.direction;
    }

    @Override
    public Direction clone() {
        try {
            Direction clone = (Direction) super.clone();
            clone.isUpside = isUpside;
            clone.isDownside = isDownside;
            clone.direction = direction;
            clone.directionSlope = directionSlope;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public enum DirectionOrientation {
        positive, negative, stable
    }
}
