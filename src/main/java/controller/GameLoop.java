package controller;

import model.MotionPanelModel;
import model.Profile;
import model.WaveManager;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.collision.Collidable;
import model.collision.Collision;
import view.characters.GeoShapeView;
import view.containers.MotionPanelView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static controller.UserInterfaceController.*;
import static controller.constants.DimensionConstants.Dimension2DConstants.MAIN_MOTIONPANEL_DIMENSION;
import static controller.constants.ViewConstants.BASE_PAINT_OPACITY;
import static model.MotionPanelModel.allMotionPanelModelsList;
import static model.MotionPanelModel.mainMotionPanelModel;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static view.containers.GlassFrame.getGlassFrame;
import static view.containers.GlassFrame.setupHUI;
import static view.containers.MotionPanelView.allMotionPanelViewsList;
import static view.containers.MotionPanelView.mainMotionPanelView;

public final class GameLoop implements Runnable {
    private static GameLoop INSTANCE = null;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private long updateTimeDiffCapture = 0;
    private long frameTimeDiffCapture = 0;
    private long currentTime;
    private long lastFrameTime;
    private long lastUpdateTime;
    private long timeSaveDiffCapture = 0;
    private long timeSave;
    private volatile String FPS_UPS = "";

    public static void updateView() {
        for (MotionPanelView motionPanelView : allMotionPanelViewsList) {
            int[] properties = getMotionPanelProperties(motionPanelView.viewId);
            motionPanelView.setBounds(properties[0], properties[1], properties[2], properties[3]);
            for (GeoShapeView shapeView : motionPanelView.shapeViews) {
                shapeView.rotatedIcon.opacity = BASE_PAINT_OPACITY.getValue() + getHealthScale(shapeView.viewId) / 2;
                shapeView.vertexLocations = getGeoShapeVertices(shapeView.viewId);
            }
        }
        getGlassFrame().repaint();
    }

    public static void updateModel() {
        Collision.getINSTANCE().run();
        for (MotionPanelModel motionPanelModel : allMotionPanelModelsList) {
            for (ActionListener actionListener : motionPanelModel.deformationListeners) {
                actionListener.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            }
        }
        for (GeoShapeModel model : allShapeModelsList) {
            for (ActionListener actionListener : model.getMovement().getMoveListeners()) {
                if (model.getMovement().getMoveListeners().contains(actionListener)) {
                    actionListener.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
                }
            }
        }
    }

    public static GameLoop getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GameLoop();
        return INSTANCE;
    }

    @Override
    public void run() {
        running.set(true);
        exit.set(false);
        initializeGame();
        setupHUI();

        int frames = 0, ticks = 0;
        double deltaU = 0, deltaF = 0;
        currentTime = System.nanoTime();
        lastFrameTime = currentTime;
        lastUpdateTime = currentTime;
        timeSave = currentTime;
        double timePerFrame = (double) TimeUnit.SECONDS.toNanos(1) / Profile.getCurrent().FPS;
        double timePerUpdate = (double) TimeUnit.SECONDS.toNanos(1) / Profile.getCurrent().UPS;

        new Thread(() -> {
            while (!exit.get()) {
                if (!FPS_UPS.equals("")) {
                    System.out.println(FPS_UPS);
                    FPS_UPS = "";
                }
            }
        }) {{
            setDaemon(true);
        }}.start();

        while (!exit.get()) {
            if (running.get()) {
                currentTime = System.nanoTime();
                if (deltaU >= 1) {
                    updateModel();
                    ticks++;
                    deltaU--;
                }
                if (deltaF >= 1) {
                    updateView();
                    frames++;
                    deltaF--;
                }
                if (currentTime - lastFrameTime > timePerFrame) {
                    deltaF += (currentTime - lastFrameTime - timePerFrame) / timePerFrame;
                    updateView();
                    frames++;
                    lastFrameTime = currentTime;
                }
                if (currentTime - lastUpdateTime > timePerUpdate) {
                    deltaU += (currentTime - lastUpdateTime - timePerUpdate) / timePerUpdate;
                    updateModel();
                    ticks++;
                    lastUpdateTime = currentTime;
                }
                if (currentTime - timeSave >= TimeUnit.SECONDS.toNanos(1)) {
                    FPS_UPS = "FPS: " + frames + " | UPS:" + ticks;
                    frames = 0;
                    ticks = 0;
                    timeSave = currentTime;
                }
            }
        }
    }

    public void initializeGame() {
        mainMotionPanelModel = new MotionPanelModel(MAIN_MOTIONPANEL_DIMENSION.getValue());
        playGameTheme(mainMotionPanelView);
        EpsilonModel.getINSTANCE();
        UserInputHandler.getINSTANCE().setupInputHandler(mainMotionPanelView);
        mainMotionPanelView.requestFocus();
        new WaveManager().start();
    }

    public void forceExitGame() {
        exit.set(true);
    }

    public void toggleGameLoop() {
        if (mainMotionPanelView == null || !mainMotionPanelView.isVisible()) new Thread(this) {{
            setDaemon(true);
        }}.start();
        else {
            if (running.get()) {
                long now = System.nanoTime();
                updateTimeDiffCapture = now - lastUpdateTime;
                frameTimeDiffCapture = now - lastFrameTime;
                timeSaveDiffCapture = now - timeSave;
                UserInputHandler.getINSTANCE().shootTimeDiffCapture = now - UserInputHandler.getINSTANCE().lastShootingTime;
                for (Collidable collidable : new CopyOnWriteArrayList<Collidable>() {{
                    addAll(allMotionPanelModelsList);
                    addAll(allShapeModelsList);
                }}) {
                    collidable.setPositionUpdateTimeDiffCapture(now - collidable.getLastPositionUpdateTime());
                }
            }
            if (!running.get()) {
                currentTime = System.nanoTime();
                lastUpdateTime = currentTime - updateTimeDiffCapture;
                lastFrameTime = currentTime - frameTimeDiffCapture;
                timeSave = currentTime - timeSaveDiffCapture;
                UserInputHandler.getINSTANCE().lastShootingTime = currentTime - UserInputHandler.getINSTANCE().shootTimeDiffCapture;
                for (Collidable collidable : new CopyOnWriteArrayList<Collidable>() {{
                    addAll(allMotionPanelModelsList);
                    addAll(allShapeModelsList);
                }}) {
                    collidable.setLastPositionUpdateTime(currentTime - collidable.getPositionUpdateTimeDiffCapture());
                }
            }
        }
        running.set(!running.get());
    }

    public boolean isRunning() {return running.get();}

    public boolean isOn() {
        return !exit.get();
    }
    public void setRunning(boolean running){this.running.set(running);}
}
