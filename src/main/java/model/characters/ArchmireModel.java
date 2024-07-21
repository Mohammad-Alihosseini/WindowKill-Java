package model.characters;

import model.collision.Collidable;
import model.entities.AttackTypes;
import model.frames.MotionPanelModel;
import model.projectiles.BulletModel;
import view.containers.MotionPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

import static controller.UserInterfaceController.createArchmire;
import static controller.constants.EntityConstants.ARCHMIRE_HEALTH;
import static controller.constants.EntityConstants.ARCHMIRE_MELEE;
import static controller.constants.EntityConstants.EntityVertices.ARCHMIRE_VERTICES;
import static controller.constants.EntityConstants.EntityVertices.MINIARCHMIRE_VERTICES;
import static model.Utils.roundPoint;

public class ArchmireModel extends GeoShapeModel implements Enemy {
    private final Timer timer;

    public ArchmireModel(Point anchor, String motionPanelId, Boolean isMini) {
        super(new Point(0, 0), getVertices(isMini), ARCHMIRE_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.MELEE, ARCHMIRE_MELEE.getValue());
        createArchmire(getModelId(), roundPoint(getAnchorSave()), motionPanelId, isMini);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);

        if (isMini) {
            setNumberOfCollectibles(2);
            setCollectibleValue(3);
        } else {
            setNumberOfCollectibles(5);
            setCollectibleValue(6);
        }

        timer = new Timer(1000, e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) {
                Point newAnchor = new Point(
                        (int) getMovement().getLastAnchor().getX(),
                        (int) getMovement().getLastAnchor().getY()
                );
                new ArchmirePathModel(newAnchor, motionPanelId, isMini);
            }
        });

        timer.start();
    }

    private static List<Point2D> getVertices(boolean isMini) {
        List<Point2D> vertices;
        if (isMini) vertices = MINIARCHMIRE_VERTICES.getValue();
        else vertices = ARCHMIRE_VERTICES.getValue();
        return vertices;
    }

    @Override
    public boolean collide(Collidable collidable) {
        if (collidable instanceof BulletModel) return true;
        return !(collidable instanceof GeoShapeModel) && !(collidable instanceof MotionPanelModel);
    }

    public void eliminate() {
        timer.stop();
        super.eliminate();
    }
}