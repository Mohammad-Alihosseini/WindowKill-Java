package model.characters;

import model.frames.MotionPanelModel;
import model.collision.Collidable;
import model.entities.AttackTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.createArchmire;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.ARCHMIRE_VERTICES;
import static controller.constants.EntityConstants.EntityVertices.MINIARCHMIRE_VERTICES;
import static model.Utils.roundPoint;

public class ArchmirePathModel extends GeoShapeModel implements Enemy {

    public ArchmirePathModel(Point anchor, String motionPanelId, Boolean isMini) {
        super(new Point(0, 0), getVertices(isMini), ARCHMIRE_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.MELEE, ARCHMIRE_MELEE.getValue());
        createArchmire(getModelId(), roundPoint(getAnchorSave()), motionPanelId, isMini);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(0);
        setCollectibleValue(0);
        setVulnerable(false);

        getMovement().setAngularSpeed(0);
        getMovement().setSpeedSave(0);
        getMovement().setSpeed(0);
        getMovement().setDeceleration(0);
        getMovement().setDecay(0);

        Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(ARCHMIRE_PATH_LIFE_TIME.getValue()), e -> eliminate());
        timer.setCoalesce(true);
        timer.setRepeats(false);
        timer.start();

    }

    private static java.util.List<Point2D> getVertices(boolean isMini) {
        java.util.List<Point2D> vertices;
        if (isMini) vertices = MINIARCHMIRE_VERTICES.getValue();
        else vertices = ARCHMIRE_VERTICES.getValue();
        return vertices;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return !(collidable instanceof GeoShapeModel) && !(collidable instanceof MotionPanelModel);
    }
}