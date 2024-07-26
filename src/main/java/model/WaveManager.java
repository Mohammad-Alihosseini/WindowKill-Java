package model;

import model.characters.*;
import model.frames.MotionPanelModel;
import model.movement.Direction;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.operation.distance.DistanceOp;
import view.containers.MotionPanelView;
import view.menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.*;
import static controller.constants.EntityConstants.NECROPICK_DISTANCE_FROM_EPSILON;
import static controller.constants.WaveConstants.*;
import static model.Utils.*;
import static model.characters.Enemy.getNumOfKilledEnemies;
import static model.characters.Enemy.resetNumOfKilledEnemies;

public class WaveManager {
    public static final Random random = new Random();
    private static final List<Point> wyrmPoints = new CopyOnWriteArrayList<>(
            List.of(new Point(200, 200),
                    new Point(200, 800),
                    new Point(1500, 200),
                    new Point(1500, 800))
    );
    public final List<Integer> waveCount = Profile.getCurrent().getWaveEnemyCount();
    private final List<GeoShapeModel> waveEntities = new CopyOnWriteArrayList<>();

    @NotNull
    private static Timer getNecropickTimer(GeoShapeModel model) {
        Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(1), null);
        timer.addActionListener(e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) {
                Point2D target = new Point2D.Double(
                        EpsilonModel.getINSTANCE().getAnchor().getX() + NECROPICK_DISTANCE_FROM_EPSILON.getValue(),
                        EpsilonModel.getINSTANCE().getAnchor().getY() + NECROPICK_DISTANCE_FROM_EPSILON.getValue()
                );
                model.getMovement().lockOnTarget(target);
            } else {
                timer.stop();
            }
        });
        return timer;
    }

    // todo expensive cost to use this logic for target of Necropick
    @NotNull
    private static Point2D getInstantaneousTarget(GeoShapeModel model) {
        List<Point2D> vertices = getVertices();
        Coordinate[] coordinates = new Coordinate[vertices.size() + 1];
        for (int i = 0; i < vertices.size(); i++) coordinates[i] = toCoordinate(vertices.get(i));
        coordinates[vertices.size()] = toCoordinate(vertices.get(0));
        Geometry geometry = new GeometryFactory().createLineString(coordinates);
        Coordinate target = DistanceOp.nearestPoints(geometry, model.getGeometry())[0];
        return new Point2D.Double(target.x, target.y);
    }

    @NotNull
    private static List<Point2D> getVertices() {
        Point2D epsilon = EpsilonModel.getINSTANCE().getAnchor();
        int offset = NECROPICK_DISTANCE_FROM_EPSILON.getValue();
        return new CopyOnWriteArrayList<>(
                List.of(new Point2D.Double(epsilon.getX() + offset, epsilon.getY() + offset),
                        new Point2D.Double(epsilon.getX() + offset, epsilon.getY() - offset),
                        new Point2D.Double(epsilon.getX() - offset, epsilon.getY() - offset),
                        new Point2D.Double(epsilon.getX() - offset, epsilon.getY() + offset)
                ));
    }

    @NotNull
    private static Timer getOmenoctTimer(GeoShapeModel model, int offset, int side) {
        Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(1), null);
        timer.addActionListener(e -> {
            if (MotionPanelView.getMainMotionPanelView() != null) {
                Coordinate[] coordinates = MotionPanelModel.getMainMotionPanelModel().getGeometry().getCoordinates();
                Point2D target = getTargetSide(coordinates, offset, side);
                model.getMovement().lockOnTarget(target);
            } else {
                timer.stop();
            }
        });
        return timer;
    }

    public void start() {
        initiateWave(0);
    }

    public void lockEnemy(GeoShapeModel model) {
        if (model instanceof SquarantineModel || model instanceof TrigorathModel ||
                model instanceof ArchmireModel || model instanceof WyrmModel) {
            model.getMovement().lockOnTarget(EpsilonModel.getINSTANCE().getModelId());
        } else if (model instanceof OmenoctModel) {
            int offset = random.nextInt(50, 250);
            int side = random.nextInt(0, 4);
            Timer timer = getOmenoctTimer(model, offset, side);
            timer.start();
        } else if (model instanceof NecropickModel) {
            Timer timer = getNecropickTimer(model);
            timer.start();
        }
    }


    private void dropOneEnemy(int wave) {
        // try catch is needed because the random location maybe out of roaster
        try {
            Point location = roundPoint(addUpPoints(EpsilonModel.getINSTANCE().getAnchor(),
                    multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                            random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
            Point wyrmLocation = wyrmPoints.get(random.nextInt(0, 4));
            GeoShapeModel model;
            if (wave == 0) {
                model = switch (random.nextInt(0, 2)) {
                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                    default -> null;
                };
            } else if (wave == 1) {
                model = switch (random.nextInt(0, 4)) {
                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                    case 2 -> new OmenoctModel(location, getMainMotionPanelId());
                    case 3 -> new NecropickModel(location, getMainMotionPanelId());
                    default -> null;
                };
            } else {
                model = switch (random.nextInt(0, 6)) {
                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                    case 2 -> new OmenoctModel(location, getMainMotionPanelId());
                    case 3 -> new NecropickModel(location, getMainMotionPanelId());
                    case 4 -> new ArchmireModel(location, getMainMotionPanelId(), random.nextBoolean());
                    case 5 -> new WyrmModel(wyrmLocation);
                    default -> null;
                };
            }
            if (model != null) {
                waveEntities.add(model);
                lockEnemy(model);
            }
        } catch (Exception ignored) {
            dropOneEnemy(wave);
        }
    }

    private void randomSpawn(int wave) {
        dropOneEnemy(wave);
        Timer spawnTimer = new Timer((int) TimeUnit.SECONDS.toMillis(ENEMY_DROP_DELAY_SECONDS.getValue()), null);
        spawnTimer.addActionListener(e -> {
            // a threshold for maximum number of enemies is set to 5 * (wave+1)
            if (getNumOfKilledEnemies() < waveCount.get(wave) && waveEntities.size() < (5 * (wave + 1))) {
                dropOneEnemy(wave);
            } else {
                spawnTimer.stop();
            }
        });
        spawnTimer.start();
    }

    private void initiateWave(int wave) {
        randomSpawn(wave);
        Timer waveTimer = new Timer(10, null);
        waveTimer.addActionListener(e -> {
            boolean waveFinished = true;
            for (GeoShapeModel shapeModel : waveEntities) {
                if (shapeModel.getHealth() > 0) {
                    waveFinished = false;
                    break;
                }
            }
            if (waveFinished) {
                waveTimer.stop();
                waveEntities.clear();
                resetNumOfKilledEnemies();
                float length = showMessage(waveCount.size() - 1 - wave);
                if (wave < waveCount.size() - 1) initiateWave(wave + 1);
                else finishGame(length);
            }
        });
        waveTimer.start();
    }

    private void finishGame(float lastSceneTime) {
        Timer timer = new Timer((int) TimeUnit.NANOSECONDS.toMillis((long) lastSceneTime), e -> {
            exitGame();
            Profile.getCurrent().saveXP();
            MainMenu.flushINSTANCE();
            MainMenu.getINSTANCE().togglePanel();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
