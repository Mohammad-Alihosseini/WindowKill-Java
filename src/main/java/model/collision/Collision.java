package model.collision;

import model.Profile;
import model.characters.*;
import model.entities.AttackTypes;
import model.entities.Entity;
import model.frames.MotionPanelModel;
import model.movement.Direction;
import model.projectiles.BulletModel;
import model.projectiles.EnemyBullet;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.toggleMotionPanelView;
import static controller.constants.AbilityConstants.WRIT_OF_ASTRAPE_DAMAGE;
import static controller.constants.EntityConstants.DROWN_COOL_DOWN_SECONDS;
import static controller.constants.ImpactConstants.IMPACT_RADIUS;
import static controller.constants.ImpactConstants.IMPACT_SCALE;
import static model.Utils.*;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static model.frames.MotionPanelModel.getMainMotionPanelModel;

public final class Collision implements Runnable {
    private static Collision INSTANCE = null;
    private static long lastDrownTime = 0;
    private static boolean WritOfAstrape = false;

    public static Collision getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Collision();
        return INSTANCE;
    }

    /**
     * @return a thread-safe hashmap of post-collision data of all GeoShapes (direction,scale,rotation)
     */
    public static List<MovementState.ShapeMovementState> evaluateMovementEffects(MovementState.CollisionState state, boolean artificialImpact) {
        CopyOnWriteArrayList<MovementState.ShapeMovementState> out = new CopyOnWriteArrayList<>();
        for (GeoShapeModel shapeModel : allShapeModelsList) {
            if (shapeModel instanceof BulletModel || (shapeModel instanceof CollectibleModel collectibleModel &&
                    state.stateOf1.collidable != collectibleModel.ancestor && state.stateOf2.collidable != collectibleModel.ancestor))
                continue;

            float distance = (float) shapeModel.getMovement().getAnchor().distance(state.collisionPoint);
            float scale = (distance > IMPACT_RADIUS.getValue()) ? 0 : (IMPACT_SCALE.getValue() * IMPACT_RADIUS.getValue() - distance) / IMPACT_RADIUS.getValue();
            Direction direction = new Direction(relativeLocation(shapeModel.getMovement().getAnchor(), state.collisionPoint));
            float torque = 0;
            if (!artificialImpact) {
                if (shapeModel == state.stateOf1.collidable) {
                    direction = state.stateOf1.direction;
                    scale *= state.stateOf1.scale;
                    torque = scale * state.stateOf1.torque;
                }
                if (shapeModel == state.stateOf2.collidable) {
                    direction = state.stateOf2.direction;
                    scale *= state.stateOf2.scale;
                    torque = scale * state.stateOf2.torque;
                }
            }
            out.add(new MovementState.ShapeMovementState(shapeModel, direction, torque, scale));
        }
        return out;
    }

    public static void emitImpactWave(MovementState.CollisionState state, boolean artificialImpact, float wavePower) {
        List<MovementState.ShapeMovementState> collisionData = evaluateMovementEffects(state, artificialImpact);
        for (MovementState.ShapeMovementState movementState : collisionData) {
            movementState.geoShapeModel.getMovement().impact(movementState.direction, wavePower * movementState.scale);
            if (movementState.torque != 0)
                movementState.geoShapeModel.getMovement().setAngularSpeed(movementState.torque);
        }
    }

    public static void emitImpactWave(Point2D collisionPoint, float wavePower) {
        emitImpactWave(new MovementState.CollisionState(collisionPoint), true, wavePower);
    }

    public static void emitImpactWave(MovementState.CollisionState state) {
        emitImpactWave(state, false, 1);
    }

    public static void evaluatePhysicalEffects(MovementState.CollisionState state) {
        if (state.stateOf1.collidable instanceof Entity entity1 && state.stateOf2.collidable instanceof Entity entity2 && state.collisionPoint != null) {
            Pair<Boolean, Boolean> meleePair = checkMelee(state);
            if (meleePair.getLeft() && meleePair.getRight()) return;
            if (entity1.isVulnerable() && (state.stateOf2.collidable instanceof BulletModel || state.stateOf1.collidable instanceof CollectibleModel || meleePair.getRight())) {
                entity2.damage(entity1, AttackTypes.MELEE);
            }
            if (entity2.isVulnerable() && (state.stateOf1.collidable instanceof BulletModel || state.stateOf2.collidable instanceof CollectibleModel || meleePair.getLeft())) {
                entity1.damage(entity2, AttackTypes.MELEE);
            }
        }
    }

    public static void WritOfAstrape(MovementState.CollisionState state) {
        if (state.stateOf1.collidable instanceof EpsilonModel entity1 && state.stateOf2.collidable instanceof Entity entity2 && state.collisionPoint != null) {
            Pair<Boolean, Boolean> meleePair = checkMelee(state);
            if (meleePair.getLeft() && meleePair.getRight()) return;
            if (entity2.isVulnerable() && entity2 instanceof Enemy) {
                entity1.damage(entity2, (int) WRIT_OF_ASTRAPE_DAMAGE.getValue());
            }
        }
    }

    public static void evaluateDrownEffect(MovementState.CollisionState state) {
        long now = System.nanoTime();
        if (now - getLastDrownTime() >= TimeUnit.SECONDS.toNanos(DROWN_COOL_DOWN_SECONDS.getValue())) {
            boolean bool1 = state.stateOf1.collidable instanceof ArchmireModel
                    && state.stateOf2.collidable instanceof EpsilonModel;
            boolean bool2 = state.stateOf1.collidable instanceof EpsilonModel
                    && state.stateOf2.collidable instanceof ArchmireModel;
            boolean bool3 = state.stateOf1.collidable instanceof ArchmirePathModel
                    && state.stateOf2.collidable instanceof EpsilonModel;
            boolean bool4 = state.stateOf1.collidable instanceof EpsilonModel
                    && state.stateOf2.collidable instanceof ArchmirePathModel;

            if ((bool1 || bool2 || bool3 || bool4) && state.collisionPoint != null) {
                Entity entity1 = (Entity) state.stateOf1.collidable;
                Entity entity2 = (Entity) state.stateOf2.collidable;

                Pair<Boolean, Boolean> meleePair = checkMelee(state);
                if (meleePair.getLeft() && meleePair.getRight()) return;
                if (entity1.isVulnerable() && entity1 instanceof EpsilonModel) {
                    entity2.damage(entity1, AttackTypes.MELEE);
                }
                if (entity2.isVulnerable() && entity2 instanceof EpsilonModel) {
                    entity1.damage(entity2, AttackTypes.MELEE);
                }
            }
            setLastDrownTime(now);
        }
    }

    public static Pair<Boolean, Boolean> checkMelee(MovementState.CollisionState state) {
        boolean melee1to2 = state.stateOf1.collidable.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                (state.stateOf1.collidable instanceof EpsilonModel || state.stateOf2.collidable instanceof EpsilonModel);
        boolean melee2to1 = state.stateOf2.collidable.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                (state.stateOf1.collidable instanceof EpsilonModel || state.stateOf2.collidable instanceof EpsilonModel);
        return new MutablePair<>(melee1to2, melee2to1);
    }

    public static void resolveCollectiblePickup(MovementState.CollisionState state) {
        if (state.stateOf1.collidable instanceof EpsilonModel && state.stateOf2.collidable instanceof CollectibleModel) {
            Profile.getCurrent().setCurrentGameXP(Profile.getCurrent().getCurrentGameXP() + ((CollectibleModel) state.stateOf2.collidable).getValue());
        }
        if (state.stateOf2.collidable instanceof EpsilonModel && state.stateOf1.collidable instanceof CollectibleModel) {
            Profile.getCurrent().setCurrentGameXP(Profile.getCurrent().getCurrentGameXP() + ((CollectibleModel) state.stateOf1.collidable).getValue());
        }
    }

    public static void resolveToggleMotionPanelView(MovementState.CollisionState state) {
        try {
            if (state.stateOf1.collidable instanceof MotionPanelModel && state.stateOf2.collidable instanceof EnemyBullet entity2) {
                toggleMotionPanelView(entity2.getModelId(), EpsilonModel.getINSTANCE().getModelId());
            }
            if (state.stateOf2.collidable instanceof MotionPanelModel && state.stateOf1.collidable instanceof EnemyBullet entity1) {
                toggleMotionPanelView(entity1.getModelId(), EpsilonModel.getINSTANCE().getModelId());
            }
        } catch (Exception ignored) {
        }
    }

    public static long getLastDrownTime() {
        return lastDrownTime;
    }

    public static void setLastDrownTime(long newLastDrownTime) {
        lastDrownTime = newLastDrownTime;
    }

    public static boolean isWritOfAstrape() {
        return WritOfAstrape;
    }

    public static void setWritOfAstrape(boolean writOfAstrape) {
        WritOfAstrape = writOfAstrape;
    }

    @Override
    public void run() {
        Collidable.CreateAllGeometries();
        List<MovementState.CollisionState> collisionStates = getAllMomentaryCollisions();
        List<MovementState.CollisionState> OtherCollisions = getOtherCollisions();

        for (MovementState.CollisionState state : collisionStates) {
            boolean notNull = state.stateOf1 != null && state.stateOf2 != null;
            if (!notNull) continue;
            if (state.stateOf1.collidable instanceof BulletModel bulletModel) bulletModel.eliminate();
            if (state.stateOf2.collidable instanceof BulletModel bulletModel) bulletModel.eliminate();
            evaluatePhysicalEffects(state);
            resolveCollectiblePickup(state);
            if (isWritOfAstrape()) WritOfAstrape(state);
            boolean areBulletMotionPanel = areInstancesOf(state.stateOf1.collidable, state.stateOf2.collidable, MotionPanelModel.class, BulletModel.class);
            boolean areEpsilonCollectible = areInstancesOf(state.stateOf1.collidable, state.stateOf2.collidable, EpsilonModel.class, CollectibleModel.class);
            if (areBulletMotionPanel) getMainMotionPanelModel().extend(roundPoint(state.collisionPoint));
            else if (!areEpsilonCollectible) emitImpactWave(state);
        }
        for (MovementState.CollisionState state : OtherCollisions) {
            boolean notNull = state.stateOf1 != null && state.stateOf2 != null;
            if (!notNull) continue;
            resolveToggleMotionPanelView(state);
            if (isWritOfAstrape()) WritOfAstrape(state);
            evaluateDrownEffect(state);
        }
    }

    public List<MovementState.CollisionState> getAllMomentaryCollisions() {
        CopyOnWriteArrayList<MovementState.CollisionState> collisionStates = new CopyOnWriteArrayList<>();
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            for (int j = i + 1; j < Collidable.collidables.size(); j++) {
                MovementState.CollisionState state;
                state = Collidable.collidables.get(i).checkCollision(Collidable.collidables.get(j));
                if (state != null) collisionStates.add(state);
            }
        }
        return collisionStates;
    }

    public List<MovementState.CollisionState> getOtherCollisions() {
        CopyOnWriteArrayList<MovementState.CollisionState> collisionStates = new CopyOnWriteArrayList<>();
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            for (int j = i + 1; j < Collidable.collidables.size(); j++) {
                MovementState.CollisionState state;
                state = Collidable.collidables.get(i).checkOtherCollisions(Collidable.collidables.get(j));
                if (state != null) collisionStates.add(state);
            }
        }
        return collisionStates;
    }
}