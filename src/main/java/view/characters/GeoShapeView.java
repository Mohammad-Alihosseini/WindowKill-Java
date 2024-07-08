package view.characters;

import view.Utils;
import view.containers.MotionPanelView;
import view.containers.RotatedIcon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static model.Utils.relativeLocation;
import static view.Utils.rotatedInfo;

public class GeoShapeView {
    public volatile static CopyOnWriteArrayList<GeoShapeView> allShapeViewsList = new CopyOnWriteArrayList<>();
    public volatile static ConcurrentHashMap<String, BufferedImage> rawImageHashMap = new ConcurrentHashMap<>();
    public String viewId;
    public RotatedIcon rotatedIcon;
    public CopyOnWriteArrayList<Point> vertexLocations = new CopyOnWriteArrayList<>();
    Dimension viewSize;
    Point relativeAnchorLocation;
    boolean isCircular;
    BufferedImage image;

    public GeoShapeView(BufferedImage image, Dimension viewSize, Point relativeAnchorLocation, MotionPanelView motionPanelView, boolean isCircular) {
        BufferedImage resized = Utils.toBufferedImage(image.getScaledInstance(viewSize.width, viewSize.height, Image.SCALE_SMOOTH));
        this.viewSize = viewSize;
        this.relativeAnchorLocation = relativeAnchorLocation;
        this.image = image;
        this.rotatedIcon = new RotatedIcon(Utils.bufferedImageClone(resized), new Point(relativeAnchorLocation), 0, isCircular);
        this.isCircular = isCircular;
        allShapeViewsList.add(this);
        motionPanelView.shapeViews.add(this);
    }

    public static BufferedImage getRawImage(String imagePath) {
        try {
            if (!rawImageHashMap.containsKey(imagePath)) rawImageHashMap.put(imagePath, ImageIO.read(new File(imagePath)));
            return rawImageHashMap.get(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveShapeView(Point newAnchorLocation) {
        rotatedIcon.corner = (Point) relativeLocation(newAnchorLocation, rotatedIcon.getRotationAnchor());
    }

    public void rotateShapeView(double angle) {
        rotatedIcon.rotate(angle);
        Dimension viewSizeSave = new Dimension(rotatedIcon.icon.getIconWidth(), rotatedIcon.icon.getIconHeight());
        Point[] rotatedInfo = rotatedInfo(viewSizeSave, rotatedIcon.getRotationAnchor(), rotatedIcon.degrees, rotatedIcon.isCircular);
        rotatedIcon.offset = new Point(rotatedIcon.corner.x - rotatedInfo[1].x, rotatedIcon.corner.y - rotatedInfo[1].y);
        rotatedIcon.width = rotatedInfo[0].x;
        rotatedIcon.height = rotatedInfo[0].y;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
