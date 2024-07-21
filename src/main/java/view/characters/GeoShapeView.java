package view.characters;

import view.Utils;
import view.base.TranslatableView;
import view.containers.MotionPanelView;
import view.containers.RotatedIcon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static view.Utils.toBufferedImage;

public class GeoShapeView implements TranslatableView {
    public static final List<GeoShapeView> allShapeViewsList = new CopyOnWriteArrayList<>();
    public static final ConcurrentMap<String, BufferedImage> rawImageHashMap = new ConcurrentHashMap<>();
    Dimension viewSize;
    Point relativeAnchorLocation;
    boolean isCircular;
    BufferedImage image;
    private String viewId;
    private RotatedIcon rotatedIcon;
    private List<Point> vertexLocations = new CopyOnWriteArrayList<>();

    public GeoShapeView(BufferedImage image, Dimension viewSize, Point relativeAnchorLocation, MotionPanelView motionPanelView, boolean isCircular) {
        BufferedImage resized = Utils.toBufferedImage(image.getScaledInstance(viewSize.width, viewSize.height, Image.SCALE_SMOOTH));
        this.viewSize = viewSize;
        this.relativeAnchorLocation = relativeAnchorLocation;
        this.image = image;
        this.setRotatedIcon(new RotatedIcon(Utils.bufferedImageClone(resized), new Point(relativeAnchorLocation), 0, isCircular));
        this.isCircular = isCircular;
        allShapeViewsList.add(this);
        motionPanelView.shapeViews.add(this);
        TranslatableView.translatableViews.add(this);
    }

    public static BufferedImage getRawImage(String imagePath) {
        rawImageHashMap.computeIfAbsent(imagePath, k -> toBufferedImage(imagePath));
        return rawImageHashMap.get(imagePath);
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public RotatedIcon getRotatedIcon() {
        return rotatedIcon;
    }

    public void setRotatedIcon(RotatedIcon rotatedIcon) {
        this.rotatedIcon = rotatedIcon;
    }

    public List<Point> getVertexLocations() {
        return vertexLocations;
    }

    public void setVertexLocations(List<Point> vertexLocations) {
        this.vertexLocations = vertexLocations;
    }
}
