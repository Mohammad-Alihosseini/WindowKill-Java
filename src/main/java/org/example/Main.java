package org.example;

import controller.UserInterfaceController;
import model.entities.Skill;
import view.menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

import static controller.constants.UIConstants.ORBITRON_FONT;
import static model.JsonOperator.JsonInitiate;
import static view.containers.GlassFrame.getGlassFrame;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        JsonInitiate();
        EventQueue.invokeLater(() -> {
            try {
                System.setProperty("sun.java2d.opengl", "true");
                System.setProperty("awt.useSystemAAFontSettings", "on");
                Runtime.getRuntime().addShutdownHook(new Thread(UserInterfaceController::safeExitApplication, "Shutdown-thread"));
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.getLookAndFeelDefaults().put("defaultFont", ORBITRON_FONT.deriveFont(15f));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            getGlassFrame();
            Skill.initializeSkills();
            MainMenu.getINSTANCE().togglePanel();
        });
    }
}