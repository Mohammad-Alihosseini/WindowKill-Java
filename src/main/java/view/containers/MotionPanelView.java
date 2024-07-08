package view.containers;

import view.base.PanelB;
import view.characters.GeoShapeView;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.isGameOn;
import static controller.constants.DefaultMethods.getCenterOffset;
import static controller.constants.ViewConstants.VERTEX_RADIUS;
import static view.containers.GlassFrame.getGlassFrame;

public class MotionPanelView extends PanelB {
    public volatile static MotionPanelView mainMotionPanelView;
    public volatile static CopyOnWriteArrayList<MotionPanelView> allMotionPanelViewsList = new CopyOnWriteArrayList<>();
    public volatile CopyOnWriteArrayList<GeoShapeView> shapeViews = new CopyOnWriteArrayList<>();
    public String viewId;

    public MotionPanelView(Dimension size, Point location) {
        super(size.width, size.height, null);
        if (mainMotionPanelView == null) mainMotionPanelView = this;
        setBackground(new Color(0, 0, 0, 0));
        setSize(size);
        setDoubleBuffered(true);
        setLocation(location);
        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        allMotionPanelViewsList.add(this);
        getGlassFrame().add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        for (GeoShapeView shapeView : shapeViews) {
            shapeView.rotatedIcon.paintIcon(this, g, 0, 0);
            if (isGameOn()) {
                for (Point point : shapeView.vertexLocations) {
                    g.fillOval((int) (point.x - getCenterOffset(VERTEX_RADIUS.getValue())), (int) (point.y - getCenterOffset(VERTEX_RADIUS.getValue())),
                            (int) VERTEX_RADIUS.getValue(), (int) VERTEX_RADIUS.getValue());
                }
            }
        }
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
