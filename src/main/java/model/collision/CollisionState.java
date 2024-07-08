package model.collision;

import model.movement.Direction;

import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionState {
    public Point2D collisionPoint;
    public Collidable collidable1;
    public Collidable collidable2;
    public Direction directionOfCollidable1;
    public Direction directionOfCollidable2;
    public float torque1;
    public float torque2;
    public float scale1;
    public float scale2;

    public CollisionState(CopyOnWriteArrayList<?> motionProperties) {
        this.collisionPoint = (Point2D) motionProperties.get(0);
        this.collidable1 = (Collidable) motionProperties.get(1);
        this.collidable2 = (Collidable) motionProperties.get(2);
        this.directionOfCollidable1 = (Direction) motionProperties.get(3);
        this.directionOfCollidable2 = (Direction) motionProperties.get(4);
        this.torque1 = (float) motionProperties.get(5);
        this.torque2 = (float) motionProperties.get(6);
        this.scale1 = (float) motionProperties.get(7);
        this.scale2 = (float) motionProperties.get(8);
    }

    public CollisionState(Point2D point) {
        this.collisionPoint = point;
        this.collidable1 = null;
        this.collidable2 = null;
        this.directionOfCollidable1 = null;
        this.directionOfCollidable2 = null;
        this.torque1 = 0;
        this.torque2 = 0;
        this.scale1 = 0;
        this.scale2 = 0;
    }
}
