package view.characters;

import view.containers.MotionPanelView;

import java.awt.*;

import static controller.constants.DimensionConstants.ARCHMIRE_DIMENSION;
import static controller.constants.DimensionConstants.MINIARCHMIRE_DIMENSION;
import static controller.constants.FilePaths.ARCHMIRE_IMAGEPATH;

public class ArchmireView extends GeoShapeView {

    public ArchmireView(Point relativeAnchorLocation, MotionPanelView motionPanelView, boolean isMini) {
        super(getRawImage(ARCHMIRE_IMAGEPATH.getValue()), getDimension(isMini), relativeAnchorLocation, motionPanelView, false);
    }

    private static Dimension getDimension(boolean isMini) {
        if (isMini) return MINIARCHMIRE_DIMENSION.getValue();
        else return ARCHMIRE_DIMENSION.getValue();
    }
}
