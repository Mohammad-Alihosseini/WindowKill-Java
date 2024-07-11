package view.characters;

import view.containers.MotionPanelView;

import java.awt.*;

import static controller.constants.DimensionConstants.BULLET_DIMENSION;


public class BulletView extends GeoShapeView {

    public BulletView(Point relativeAnchorLocation, MotionPanelView motionPanelView, String path) {
        super(getRawImage(path), BULLET_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, true);
    }

}
