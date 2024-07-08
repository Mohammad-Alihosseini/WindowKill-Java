package model.collision;

import model.MotionPanelModel;
import model.Profile;
import model.characters.CollectibleModel;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.entities.AttackTypes;
import model.entities.Entity;
import model.movement.Direction;
import model.projectiles.BulletModel;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.geom.Point2D;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.ImpactConstants.IMPACT_RADIUS;
import static controller.constants.ImpactConstants.IMPACT_SCALE;
import static model.MotionPanelModel.mainMotionPanelModel;
import static model.Utils.*;
import static model.characters.GeoShapeModel.allShapeModelsList;

public final class Collision implements Runnable {
    private static Collision INSTANCE = null;

    public static Collision getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Collision();
        return INSTANCE;
    }

    /**
     * @return a thread-safe hashmap of post-collision data of all GeoShapes (direction,scale,rotation)
     */
    public static ConcurrentHashMap<GeoShapeModel, Triple<Direction, Float, Float>> evaluateMovementEffects(CollisionState state, boolean artificialImpact, float wavePower) {
        ConcurrentHashMap<GeoShapeModel, Triple<Direction, Float, Float>> out = new ConcurrentHashMap<>();
        for (GeoShapeModel shapeModel : allShapeModelsList) {
            if (shapeModel instanceof BulletModel) continue;
            if (shapeModel instanceof CollectibleModel) {
                if (state.collidable1 != ((CollectibleModel) shapeModel).ancestor && state.collidable2 != ((CollectibleModel) shapeModel).ancestor) continue;
            }
            float distance = (float) shapeModel.getMovement().getAnchor().distance(state.collisionPoint);
            float scale = (distance > IMPACT_RADIUS.getValue()) ? 0 : (IMPACT_SCALE.getValue() * IMPACT_RADIUS.getValue() - distance) / IMPACT_RADIUS.getValue();
            Direction direction = new Direction(relativeLocation(shapeModel.getMovement().getAnchor(), state.collisionPoint));
            float rotate = 0;
            if (!artificialImpact) {
                if (shapeModel == state.collidable1) {
                    direction = state.directionOfCollidable1;
                    scale *= state.scale1;
                    rotate = scale * state.torque1;
                }
                if (shapeModel == state.collidable2) {
                    direction = state.directionOfCollidable2;
                    scale *= state.scale2;
                    rotate = scale * state.torque2;
                }
            }
            out.put(shapeModel, new MutableTriple<>(direction, wavePower * scale, rotate));
        }
        return out;
    }

    public static void emitImpactWave(CollisionState state, boolean artificialImpact, float wavePower) {
        ConcurrentHashMap<GeoShapeModel, Triple<Direction, Float, Float>> collisionData = evaluateMovementEffects(state, artificialImpact, wavePower);
        for (GeoShapeModel shapeModel : collisionData.keySet()) {
            shapeModel.getMovement().impact(collisionData.get(shapeModel).getLeft(), wavePower * collisionData.get(shapeModel).getMiddle());
            if (collisionData.get(shapeModel).getRight() != 0) shapeModel.getMovement().angularSpeed = collisionData.get(shapeModel).getRight();
        }
    }

    public static void emitImpactWave(Point2D collisionPoint, float wavePower) {
        emitImpactWave(new CollisionState(collisionPoint), true, wavePower);
    }

    public static void emitImpactWave(CollisionState state) {
        emitImpactWave(state, false, 1);
    }

    public static void evaluatePhysicalEffects(CollisionState state) {
        if (state.collidable1 instanceof Entity && state.collidable2 instanceof Entity && state.collisionPoint != null) {
            boolean melee2_1 = state.collidable2.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                    (state.collidable1 instanceof EpsilonModel || state.collidable2 instanceof EpsilonModel);
            boolean melee1_2 = state.collidable1.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                    (state.collidable1 instanceof EpsilonModel || state.collidable2 instanceof EpsilonModel);
            if (melee1_2 && melee2_1) return;
            if (((Entity) state.collidable1).vulnerable) {
                if (state.collidable2 instanceof BulletModel || state.collidable1 instanceof CollectibleModel || melee2_1) {
                    ((Entity) state.collidable2).damage((Entity) state.collidable1, AttackTypes.MELEE);
                }
            }
            if (((Entity) state.collidable2).vulnerable) {
                if (state.collidable1 instanceof BulletModel || state.collidable2 instanceof CollectibleModel || melee1_2) {
                    ((Entity) state.collidable1).damage((Entity) state.collidable2, AttackTypes.MELEE);
                }
            }
            if (state.collidable1 instanceof EpsilonModel && state.collidable2 instanceof CollectibleModel) {
                Profile.getCurrent().currentGameXP += ((CollectibleModel) state.collidable2).getValue();
            }
            if (state.collidable2 instanceof EpsilonModel && state.collidable1 instanceof CollectibleModel) {
                Profile.getCurrent().currentGameXP += ((CollectibleModel) state.collidable1).getValue();
            }
        }
    }

    @Override
    public void run() {
        Collidable.CreateAllGeometries();
        CopyOnWriteArrayList<CollisionState> collisionStates = new CopyOnWriteArrayList<>();
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            for (int j = i + 1; j < Collidable.collidables.size(); j++) {
                CollisionState state;
                state = Collidable.collidables.get(i).checkCollision(Collidable.collidables.get(j));
                if (state != null) collisionStates.add(state);
            }
        }
        for (CollisionState state : collisionStates) {
            if (state.collidable1 instanceof BulletModel) ((BulletModel) state.collidable1).eliminate();
            if (state.collidable2 instanceof BulletModel) ((BulletModel) state.collidable2).eliminate();
            evaluatePhysicalEffects(state);
            boolean bullet_motionPanel = areInstancesOf(state.collidable1, state.collidable2, MotionPanelModel.class, BulletModel.class);
            boolean epsilon_collectible = areInstancesOf(state.collidable1, state.collidable2, EpsilonModel.class, CollectibleModel.class);
            if (bullet_motionPanel) mainMotionPanelModel.extend(roundPoint(state.collisionPoint));
            else if (!epsilon_collectible) emitImpactWave(state);
        }
    }
}