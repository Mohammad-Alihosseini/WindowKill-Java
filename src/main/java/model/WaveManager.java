package model;

import model.characters.*;
import model.frames.MotionPanelModel;
import model.movement.Direction;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
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
import static controller.constants.WaveConstants.MAX_ENEMY_SPAWN_RADIUS;
import static controller.constants.WaveConstants.MIN_ENEMY_SPAWN_RADIUS;
import static model.Utils.*;

public class WaveManager {
    public static final Random random = new Random();
    public final List<Integer> waveCount = Profile.getCurrent().getWaveEnemyCount();
    private final List<GeoShapeModel> waveEntities = new CopyOnWriteArrayList<>();

    @NotNull
    private static Timer getNecropickTimer(GeoShapeModel model) {
        // todo radius of epsilon
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

    public void lockEnemies() {
        for (GeoShapeModel model : waveEntities) {
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
    }

    public void randomSpawn(int wave) {
        // try catch is needed because the random location maybe out of roaster
        for (int i = 0; i < waveCount.get(wave); i++) {
            try {
                Point location = roundPoint(addUpPoints(EpsilonModel.getINSTANCE().getAnchor(),
                        multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                                random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
                GeoShapeModel model;
                if (wave == 0) model = new ArchmireModel(location, getMainMotionPanelId(), random.nextBoolean());
                else {
                    model = switch (random.nextInt(0, 4)) {
                        case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                        case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                        case 2 -> new OmenoctModel(location, getMainMotionPanelId());
                        case 3 -> new NecropickModel(location, getMainMotionPanelId());
                        case 4 -> new ArchmireModel(location, getMainMotionPanelId(), random.nextBoolean());
                        case 5 -> new WyrmModel(location);
                        default -> null;
                    };
                }
                if (model != null) waveEntities.add(model);
            } catch (Exception e) {
                i--;
            }
        }
    }

    private void initiateWave(int wave) {
        randomSpawn(wave);
        lockEnemies();
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
                float length = showMessage(waveCount.size() - 1 - wave);
                if (wave < waveCount.size() - 1) initiateWave(wave + 1);
                else finishGame(length);
            }
        });
        waveTimer.start();
    }

    public void finishGame(float lastSceneTime) {
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
