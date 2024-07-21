package model.movement;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.moveShapeView;
import static controller.UserInterfaceController.rotateShapeView;
import static model.Utils.*;

public interface Translatable {

    CopyOnWriteArrayList<Translatable> translatable = new CopyOnWriteArrayList<>();

    List<Point2D> getVertices();

    List<Point2D> getVerticesSave();

    Movement getMovement();

    Point2D getAnchorSave();

    String getModelId();

    float getTotalRotation();

    void setTotalRotation(float totalRotation);

    boolean crossesUnmovable(Point2D anchorLocation);

    default void moveShapeModel(Point2D newAnchor) {
        for (int i = 0; i < getVerticesSave().size(); i++)
            getVertices().set(i, addUpPoints(getVerticesSave().get(i), relativeLocation(newAnchor, getAnchorSave())));
        moveShapeView(getModelId(), getMovement().getAnchor());
    }

    default void rotateShapeModel(float currentRotation) {
        setTotalRotation(getTotalRotation() - currentRotation);
        for (int i = 0; i < getVertices().size(); i++)
            getVertices().set(i, addUpPoints(relativeLocation(getMovement().getAnchor(), getAnchorSave()),
                    rotateAbout(getVerticesSave().get(i), getAnchorSave(), getTotalRotation())));
        rotateShapeView(getModelId(), currentRotation);
    }


}
