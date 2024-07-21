package view.containers;

import view.Utils;
import view.base.TranslatableView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IsometricMotionPanelView extends MotionPanelView implements TranslatableView {
    Point relativeAnchorLocation;
    private RotatedIcon rotatedIcon;


    public IsometricMotionPanelView(Dimension size, Point location) {
        super(size, location);
        this.relativeAnchorLocation = location;

        BufferedImage resized = Utils.toBufferedImage(getImageSave().getScaledInstance(
                size.width, size.height, Image.SCALE_SMOOTH)
        );

        this.setRotatedIcon(new RotatedIcon(
                Utils.bufferedImageClone(resized),
                new Point(relativeAnchorLocation),
                0,
                false)
        );

        TranslatableView.translatableViews.add(this);
    }

    @Override
    public RotatedIcon getRotatedIcon() {
        return rotatedIcon;
    }

    public void setRotatedIcon(RotatedIcon rotatedIcon) {
        this.rotatedIcon = rotatedIcon;
    }

}
