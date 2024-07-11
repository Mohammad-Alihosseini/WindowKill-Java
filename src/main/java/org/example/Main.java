package org.example;

import controller.UserInterfaceController;
import view.menu.LoginPage;

import javax.swing.*;
import java.awt.*;

import static controller.constants.UIConstants.DEFAULT_FONT_SIZE;
import static controller.constants.UIConstants.ORBITRON_FONT;
import static view.containers.GlassFrame.getGlassFrame;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                System.setProperty("sun.java2d.opengl", "true");
                System.setProperty("awt.useSystemAAFontSettings", "on");
                Runtime.getRuntime().addShutdownHook(new Thread(UserInterfaceController::safeExitApplication, "Shutdown-thread"));
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.getLookAndFeelDefaults().put("defaultFont", ORBITRON_FONT.deriveFont(DEFAULT_FONT_SIZE.getValue()));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException e) {
                throw new AWTError("Failed to set AWT settings");
            }
            getGlassFrame();
            LoginPage.getINSTANCE().togglePanel();
        });
//        Geometry geometry = new GeometryFactory().createLineString(new Coordinate[]{
//                new Coordinate(1, 2),
//                new Coordinate(3, 4),
//                new Coordinate(5, 6),
//                new Coordinate(7, 8),
//                new Coordinate(9, 10)});
//        Coordinate[] coordinates = geometry.getCoordinates();
//        System.out.println(coordinates.length);
    }
}