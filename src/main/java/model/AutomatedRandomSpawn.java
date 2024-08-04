package model;

import model.characters.Enemy;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.characters.WyrmModel;
import model.movement.Direction;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.getMainMotionPanelId;
import static controller.constants.WaveConstants.MAX_ENEMY_SPAWN_RADIUS;
import static controller.constants.WaveConstants.MIN_ENEMY_SPAWN_RADIUS;
import static model.SubclassFinder.findEnemySubclasses;
import static model.Utils.*;

public class AutomatedRandomSpawn {
    public static final Random random = new Random();
    private static final List<Point> wyrmPoints = new CopyOnWriteArrayList<>(
            List.of(new Point(200, 200),
                    new Point(200, 800),
                    new Point(1500, 200),
                    new Point(1500, 800))
    );
    List<Class<?>> enemySubclasses;
    WaveManager waveManager;

    public AutomatedRandomSpawn(WaveManager waveManager) {
        this.waveManager = waveManager;

        // TODO RESOLVE HARD CODED PARTS.
        String charactersPackagePath = "model.characters";
        Class<?> enemyClass = Enemy.class;

        try {
            this.enemySubclasses = findEnemySubclasses(charactersPackagePath, enemyClass);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object[] getDefaultValues(Class<?>[] parameterTypes, Class<?> cls) {
        Point location = roundPoint(addUpPoints(EpsilonModel.getINSTANCE().getAnchor(),
                multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                        random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
        Point wyrmLocation = wyrmPoints.get(random.nextInt(0, 4));

        Object[] defaultValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(boolean.class)) {
                defaultValues[i] = random.nextBoolean();
            } else if (parameterTypes[i].equals(String.class)) {
                defaultValues[i] = getMainMotionPanelId();
            } else if (parameterTypes[i].equals(Point.class)) {
                if (cls.equals(WyrmModel.class)) defaultValues[i] = wyrmLocation;
                else defaultValues[i] = location;
            } else {
                defaultValues[i] = null;
            }
        }
        return defaultValues;
    }

    public void dropOneEnemy(int wave) {
        // try catch is needed because the random location maybe out of roaster
        try {
            GeoShapeModel model;
            model = constructClass(enemySubclasses.getFirst());
            //TODO MAKE MODEL BASE ON WAVE NUM AND ENEMY

//            if (wave == 0) {
//                model = switch (random.nextInt(0, 2)) {
//                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
//                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
//                    default -> null;
//                };
//            } else if (wave == 1) {
//                model = switch (random.nextInt(0, 4)) {
//                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
//                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
//                    case 2 -> new OmenoctModel(location, getMainMotionPanelId());
//                    case 3 -> new NecropickModel(location, getMainMotionPanelId());
//                    default -> null;
//                };
//            } else {
//                model = switch (random.nextInt(0, 6)) {
//                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
//                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
//                    case 2 -> new OmenoctModel(location, getMainMotionPanelId());
//                    case 3 -> new NecropickModel(location, getMainMotionPanelId());
//                    case 4 -> new ArchmireModel(location, getMainMotionPanelId(), random.nextBoolean());
//                    case 5 -> new WyrmModel(wyrmLocation);
//                    default -> null;
//                };
//            }
            if (model != null) {
                waveManager.addEnemyToWaveEntities(model);
                waveManager.lockEnemy(model);
            }
        } catch (Exception ignored) {
            dropOneEnemy(wave);
        }
    }

    private GeoShapeModel constructClass(Class<?> cls) {
        GeoShapeModel instance;
        Constructor<?> constructor = cls.getConstructors()[0];
        try {
            instance = (GeoShapeModel) constructor.newInstance(getDefaultValues(constructor.getParameterTypes(), cls));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
