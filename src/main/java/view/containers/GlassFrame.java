package view.containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static controller.constants.DimensionConstants.SCREEN_SIZE;
import static controller.constants.ShrinkConstants.MINIMIZE_DELAY;

public final class GlassFrame extends JFrame {
    private static GlassFrame INSTANCE;

    private GlassFrame() throws HeadlessException {
        super();
        try {
            minimizeAll();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        setUndecorated(true);
        setBackground(new Color(1, 0, 0, 1));
        setSize(SCREEN_SIZE.getValue().width, SCREEN_SIZE.getValue().height);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    public static void minimizeAll() throws AWTException {
        Robot r = new Robot();
        r.setAutoDelay((int) MINIMIZE_DELAY.getValue());
        r.keyPress(KeyEvent.VK_WINDOWS);
        r.keyPress(KeyEvent.VK_D);
        r.keyRelease(KeyEvent.VK_D);
        r.keyRelease(KeyEvent.VK_WINDOWS);
    }

    public static void setupHUI() {
        //TODO setup game HUI
    }

    public static GlassFrame getGlassFrame() {
        if (INSTANCE == null) INSTANCE = new GlassFrame();
        return INSTANCE;
    }

}
