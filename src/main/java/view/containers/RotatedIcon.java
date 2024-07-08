package view.containers;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static controller.constants.DefaultMethods.radianTable;
import static view.Utils.relativeLocation;

public class RotatedIcon implements Icon {
    public final Icon icon;
    public final boolean isCircular;
    public Point corner = new Point(0, 0);
    public Point offset = new Point(0, 0);
    public int width;
    public int height;
    public Point rotationAnchor;
    public float opacity = 1;
    public double degrees;

    public RotatedIcon(BufferedImage image, Point rotationAnchor, double degrees, boolean isCircular) {
        this.icon = new ImageIcon(image);
        this.rotationAnchor = rotationAnchor;
        this.degrees = degrees;
        this.isCircular = isCircular;
    }

    @Override
    public int getIconWidth() {
        if (isCircular) return icon.getIconWidth();
        else return width;
    }

    @Override
    public int getIconHeight() {
        if (isCircular) return icon.getIconHeight();
        else return height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Point relativeCorner = relativeLocation(c, corner);
        Point relativeOffset = relativeLocation(c, offset);
        Point relativeIconCorner = new Point(Math.max(relativeOffset.x, 0), Math.max(relativeOffset.y, 0));
        int upperX = Math.min(relativeIconCorner.x + getIconWidth(), c.getWidth());
        int upperY = Math.min(relativeIconCorner.y + getIconHeight(), c.getHeight());

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(relativeIconCorner.x, relativeIconCorner.y, upperX - relativeIconCorner.x, upperY - relativeIconCorner.y);
        g2d.rotate(radianTable[(int) degrees], relativeCorner.x + rotationAnchor.x, relativeCorner.y + rotationAnchor.y);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) Math.pow(opacity, 3)));
        icon.paintIcon(c, g2d, relativeCorner.x, relativeCorner.y);
        g2d.dispose();
    }

    public void rotate(double degrees) {
        this.degrees -= degrees;
        this.degrees = (this.degrees - Math.floor(this.degrees / 360) * 360);
    }

    public Point getRotationAnchor() {
        return rotationAnchor;
    }
}