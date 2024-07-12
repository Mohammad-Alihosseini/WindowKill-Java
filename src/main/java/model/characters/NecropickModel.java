package model.characters;

import model.collision.Collidable;
import model.entities.AttackTypes;
import model.movement.Direction;
import model.projectiles.LongRanged;
import model.projectiles.ShooterEntity;
import view.containers.MotionPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.createNecropick;
import static controller.UserInterfaceController.toggleView;
import static controller.constants.DimensionConstants.SCREEN_SIZE;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.EntityVertices.NECROPICK_VERTICES;
import static controller.constants.MovementConstants.*;
import static model.Utils.relativeLocation;
import static model.Utils.roundPoint;

public class NecropickModel extends GeoShapeModel implements LongRanged, Enemy {
    private final Timer timer;
    CopyOnWriteArrayList<Point2D> targets;
    private boolean isEven = false;
    private boolean isPainted = true;
    private int shootingRapidity = ENEMY_SHOUTING_RAPIDITY.getValue();


    public NecropickModel(Point anchor, String motionPanelId) {
        super(new Point(0, 0), NECROPICK_VERTICES.getValue(), NECROPICK_HEALTH.getValue());
        this.setCircular(false);
        setMotionPanelId(motionPanelId);
        this.setAnchorSave(getCenter());
        getDamageSize().put(AttackTypes.MELEE, NECROPICK_MELEE.getValue());
        getDamageSize().put(AttackTypes.RANGED, NECROPICK_RANGED.getValue());
        createNecropick(getModelId(), roundPoint(getAnchorSave()), motionPanelId);
        moveShapeModel(anchor);
        getMovement().setAnchor(anchor);
        setNumberOfCollectibles(4);
        setCollectibleValue(2);
        setTargetOfBullets();
        float angularSpeed = getMovement().getAngularSpeed();


        timer = new Timer((int) TimeUnit.SECONDS.toMillis(4), e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) {
                if (isEven) {
                    isEven = false;
                    isPainted = true;
                    toggleView(getModelId(), motionPanelId, false);
                    getMovement().setAngularSpeed(0.001F);
                    getMovement().setSpeedSave(0.001F);
                    getMovement().setSpeed(0.001F);
                    getMovement().setDeceleration(0.001F);
                    getMovement().setDecay(0.001F);
                    activateShooting();
                } else {
                    isEven = true;
                    isPainted = false;
                    toggleView(getModelId(), motionPanelId, true);
                    getMovement().setAngularSpeed(angularSpeed);
                    getMovement().setSpeedSave(DEFAULT_SPEED.getValue());
                    getMovement().setSpeed(getMovement().getSpeedSave());
                    getMovement().setDeceleration(DEFAULT_DECELERATION.getValue());
                    getMovement().setDecay(DECELERATION_DECAY.getValue());
                }
            }
        });
        timer.start();
    }

    private void activateShooting() {
        for (Point2D target : targets) {
            Point2D relativeLocation = relativeLocation(target, roundPoint(getMovement().getAnchor()));
            hoveringShoot(this, new Direction(relativeLocation),
                    getDamageSize().get(AttackTypes.RANGED), ShooterEntity.Necropick);
        }
    }

    private void setTargetOfBullets() {
        targets = new CopyOnWriteArrayList<>(
                java.util.List.of(
                        new Point2D.Float(SCREEN_SIZE.getValue().width, -SCREEN_SIZE.getValue().height),
                        new Point2D.Float(-SCREEN_SIZE.getValue().width, -SCREEN_SIZE.getValue().height),
                        new Point2D.Float(-SCREEN_SIZE.getValue().width, SCREEN_SIZE.getValue().height),
                        new Point2D.Float(SCREEN_SIZE.getValue().width, SCREEN_SIZE.getValue().height)
                ));
    }

    @Override
    public void eliminate() {
        super.eliminate();
        timer.stop();
    }

    @Override
    public boolean collide(Collidable collidable) {
        if (isPainted) {
            return collidable instanceof GeoShapeModel;
        } else {
            return !(collidable instanceof GeoShapeModel);
        }
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