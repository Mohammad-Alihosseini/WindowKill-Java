package model.characters;

import model.entities.AttackTypes;
import model.movement.Direction;
import model.projectiles.LongRanged;
import model.projectiles.ShooterEntity;
import view.containers.MotionPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.createOmenoct;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.OMENOCT_VERTICES;
import static model.Utils.relativeLocation;
import static model.Utils.roundPoint;

public class OmenoctModel extends GeoShapeModel implements LongRanged, Enemy {
    private final Timer timer;
    private int shootingRapidity = ENEMY_SHOUTING_RAPIDITY.getValue();

    public OmenoctModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), OMENOCT_VERTICES.getValue(), OMENOCT_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.MELEE, OMENOCT_MELEE.getValue());
        getDamageSize().put(AttackTypes.RANGED, OMENOCT_RANGED.getValue());
        createOmenoct(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(8);
        setCollectibleValue(4);

        //todo if condition
        timer = new Timer((int) TimeUnit.SECONDS.toMillis(ENEMY_SHOUTING_RAPIDITY.getValue()), e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) activateShooting();
        });

        timer.start();
    }

    private void activateShooting() {
        Point2D epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
        Point2D relativeLocation = relativeLocation(epsilonAnchor, roundPoint(getMovement().getAnchor()));
        hoveringShoot(this, new Direction(relativeLocation),
                getDamageSize().get(AttackTypes.RANGED), ShooterEntity.Omenoct);
    }

    @Override
    public void eliminate() {
        super.eliminate();
        timer.stop();
    }

    @Override
    public int getShootingRapidity() {
        return shootingRapidity;
    }

    @Override
    public void setShootingRapidity(int shootingRapidity) {
        this.shootingRapidity = shootingRapidity;
    }
}