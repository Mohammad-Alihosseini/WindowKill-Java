package view.base;

import view.containers.RotatedIcon;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static model.Utils.relativeLocation;
import static view.Utils.rotatedInfo;

public interface TranslatableView {
    CopyOnWriteArrayList<TranslatableView> translatableViews = new CopyOnWriteArrayList<>();

    RotatedIcon getRotatedIcon();

    default void moveShapeView(Point newAnchorLocation) {
        getRotatedIcon().setCorner((Point) relativeLocation(newAnchorLocation, getRotatedIcon().getRotationAnchor()));
    }

    default void rotateShapeView(float angle) {
        getRotatedIcon().rotate(angle);

        Dimension viewSizeSave = new Dimension(
                getRotatedIcon().getIcon().getIconWidth(),
                getRotatedIcon().getIcon().getIconHeight()
        );

        Point[] rotatedInfo = rotatedInfo(
                viewSizeSave,
                getRotatedIcon().getRotationAnchor(),
                getRotatedIcon().getDegrees(),
                getRotatedIcon().isCircular()
        );

        getRotatedIcon().setOffset(new Point(
                getRotatedIcon().getCorner().x - rotatedInfo[1].x,
                getRotatedIcon().getCorner().y - rotatedInfo[1].y)
        );

        getRotatedIcon().setWidth(rotatedInfo[0].x);
        getRotatedIcon().setHeight(rotatedInfo[0].y);
    }

    String getViewId();
}
