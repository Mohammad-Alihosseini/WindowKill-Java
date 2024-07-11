package view.characters;

import view.containers.MotionPanelView;

import java.awt.*;

import static controller.constants.DimensionConstants.NECROPICK_DIMENSION;
import static controller.constants.FilePaths.NECROPICK_IMAGEPATH;

public class NecropickView extends GeoShapeView {

    public NecropickView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(getRawImage(NECROPICK_IMAGEPATH.getValue()), NECROPICK_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, false);
    }
}
