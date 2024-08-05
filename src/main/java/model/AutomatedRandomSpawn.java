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
import java.util.Objects;
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
            //TODO WYRM HAS PROBLEMS EITHER EXCLUDE IT OR RESOLVE THE PROBLEM.

            if (wave == 0) {
                model = switch (random.nextInt(0, 2)) {
                    case 0 -> constructClass(findClassByName(enemySubclasses, "SquarantineModel"));
                    case 1 -> constructClass(findClassByName(enemySubclasses, "TrigorathModel"));
                    default -> null;
                };
            } else if (wave == 1) {
                model = switch (random.nextInt(0, 4)) {
                    case 0 -> constructClass(findClassByName(enemySubclasses, "SquarantineModel"));
                    case 1 -> constructClass(findClassByName(enemySubclasses, "TrigorathModel"));
                    case 2 -> constructClass(findClassByName(enemySubclasses, "OmenoctModel"));
                    case 3 -> constructClass(findClassByName(enemySubclasses, "NecropickModel"));
                    default -> null;
                };
            } else {
                System.out.println(enemySubclasses.get(random.nextInt(0, enemySubclasses.size())).getName());
                model = constructClass(enemySubclasses.get(random.nextInt(0, enemySubclasses.size())));
            }
            if (model != null) {
                waveManager.addEnemyToWaveEntities(model);
                waveManager.lockEnemy(model);
            }
        } catch (Exception ignored) {
            dropOneEnemy(wave);
        }
    }

    private GeoShapeModel constructClass(Class<?> cls) {
        Objects.requireNonNull(cls);
        GeoShapeModel instance;
        Constructor<?> constructor = cls.getConstructors()[0];
        try {
            instance = (GeoShapeModel) constructor.newInstance(getDefaultValues(constructor.getParameterTypes(), cls));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    private Class<?> findClassByName(List<Class<?>> classes, String className) {
        for (Class<?> clazz : classes) {
            if (clazz.getName().contains(className) || clazz.getSimpleName().contains(className)) {
                return clazz;
            }
        }
        return null;
    }
}
