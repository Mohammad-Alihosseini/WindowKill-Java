package view.characters;

import view.containers.MotionPanelView;

import java.awt.*;

import static controller.constants.DimensionConstants.OMENOCT_DIMENSION;
import static controller.constants.FilePaths.OMENOCT_IMAGEPATH;

public class WyrmView extends GeoShapeView {

    public WyrmView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(getRawImage(OMENOCT_IMAGEPATH.getValue()), OMENOCT_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, false);
    }
}
