package controller;

import view.containers.MotionPanelView;
import view.menu.PauseMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import static controller.UserInputHandler.MovementAction.MovementDirection.*;
import static controller.UserInterfaceController.*;
import static controller.constants.EntityConstants.SHOTS_PER_SECOND;
import static view.containers.GlassFrame.getGlassFrame;

public final class UserInputHandler {
    public static final InputMap inputMap = new InputMap();
    public static final ActionMap actionMap = new ActionMap();
    public static boolean MOVE_UP_IND;
    public static boolean MOVE_DOWN_IND;
    public static boolean MOVE_LEFT_IND;
    public static boolean MOVE_RIGHT_IND;
    public static boolean SHOOT_IND;
    private static UserInputHandler INSTANCE;
    public long lastShootingTime = System.nanoTime();
    public long shootTimeDiffCapture = 0;
    int UP_KEYCODE = KeyEvent.VK_W;
    int DOWN_KEYCODE = KeyEvent.VK_S;
    int LEFT_KEYCODE = KeyEvent.VK_A;
    int RIGHT_KEYCODE = KeyEvent.VK_D;
    int SKILL_KEYCODE = KeyEvent.VK_SHIFT;
    int PAUSE_KEYCODE = KeyEvent.VK_ESCAPE;

    private UserInputHandler() {
        inputMap.put(KeyStroke.getKeyStroke(UP_KEYCODE, 0, false), MOVE_UP + "pressed");
        actionMap.put(MOVE_UP + "pressed", new MovementAction(MOVE_UP, true));
        inputMap.put(KeyStroke.getKeyStroke(UP_KEYCODE, 0, true), MOVE_UP + "released");
        actionMap.put(MOVE_UP + "released", new MovementAction(MOVE_UP, false));

        inputMap.put(KeyStroke.getKeyStroke(DOWN_KEYCODE, 0, false), MOVE_DOWN + "pressed");
        actionMap.put(MOVE_DOWN + "pressed", new MovementAction(MOVE_DOWN, true));
        inputMap.put(KeyStroke.getKeyStroke(DOWN_KEYCODE, 0, true), MOVE_DOWN + "released");
        actionMap.put(MOVE_DOWN + "released", new MovementAction(MOVE_DOWN, false));

        inputMap.put(KeyStroke.getKeyStroke(LEFT_KEYCODE, 0, false), MOVE_LEFT + "pressed");
        actionMap.put(MOVE_LEFT + "pressed", new MovementAction(MOVE_LEFT, true));
        inputMap.put(KeyStroke.getKeyStroke(LEFT_KEYCODE, 0, true), MOVE_LEFT + "released");
        actionMap.put(MOVE_LEFT + "released", new MovementAction(MOVE_LEFT, false));

        inputMap.put(KeyStroke.getKeyStroke(RIGHT_KEYCODE, 0, false), MOVE_RIGHT + "pressed");
        actionMap.put(MOVE_RIGHT + "pressed", new MovementAction(MOVE_RIGHT, true));
        inputMap.put(KeyStroke.getKeyStroke(RIGHT_KEYCODE, 0, true), MOVE_RIGHT + "released");
        actionMap.put(MOVE_RIGHT + "released", new MovementAction(MOVE_RIGHT, false));

        inputMap.put(KeyStroke.getKeyStroke(PAUSE_KEYCODE, 0, true), PAUSE);
        actionMap.put(PAUSE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {PauseMenu.getINSTANCE().togglePanel();}
        });

        inputMap.put(KeyStroke.getKeyStroke(SKILL_KEYCODE, 0, true), SKILL);
        actionMap.put(SKILL, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireSkill();
            }
        });

        getGlassFrame().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isGameRunning()) {
                    long now = System.nanoTime();
                    if (now - lastShootingTime >= TimeUnit.SECONDS.toNanos(1) / SHOTS_PER_SECOND.getValue()) {
                        SHOOT_IND = true;
                        lastShootingTime = now;
                    }
                }
            }
        });
    }

    public static UserInputHandler getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new UserInputHandler();
        return INSTANCE;
    }

    public static Point getMouseLocation() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public void setupInputHandler(MotionPanelView motionPanelView) {
        motionPanelView.setInputMap(JComponent.WHEN_FOCUSED, UserInputHandler.inputMap);
        motionPanelView.setActionMap(UserInputHandler.actionMap);
        motionPanelView.requestFocus();
    }

    public static class MovementAction extends AbstractAction {
        MovementDirection movementDirection;
        boolean pressed;

        public MovementAction(MovementDirection movementDirection, boolean pressed) {
            this.movementDirection = movementDirection;
            this.pressed = pressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isGameRunning()) {
                switch (movementDirection) {
                    case MOVE_UP -> MOVE_UP_IND = pressed;
                    case MOVE_DOWN -> MOVE_DOWN_IND = pressed;
                    case MOVE_LEFT -> MOVE_LEFT_IND = pressed;
                    case MOVE_RIGHT -> MOVE_RIGHT_IND = pressed;
                    case SHOOT -> SHOOT_IND = pressed;
                }
            }
        }

        public enum MovementDirection {MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, SHOOT, PAUSE, SKILL}
    }
}
