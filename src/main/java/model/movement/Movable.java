package model.movement;

import java.awt.geom.Point2D;

public interface Movable {
    void moveShapeModel(Point2D point);

    void rotateShapeModel(double angle);
}
