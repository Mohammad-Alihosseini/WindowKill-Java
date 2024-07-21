package view.characters;

import view.containers.MotionPanelView;

import java.awt.*;

import static controller.constants.DimensionConstants.WYRM_DIMENSION;
import static controller.constants.FilePaths.WYRM_IMAGEPATH;

public class WyrmView extends GeoShapeView {

    public WyrmView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(getRawImage(WYRM_IMAGEPATH.getValue()), WYRM_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, false);
    }
}
