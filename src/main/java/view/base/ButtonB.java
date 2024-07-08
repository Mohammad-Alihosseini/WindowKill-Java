package view.base;

import view.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.FilePaths.BUTTONS_UI_PATH;
import static controller.constants.UIConstants.*;
import static view.containers.GlassFrame.getGlassFrame;

public class ButtonB extends JButton {
    public Font plainFont = ORBITRON_FONT.deriveFont(Font.PLAIN, PLAIN_FONT_SIZE.getValue());
    public Font boldFont = ORBITRON_FONT.deriveFont(Font.BOLD, BOLD_FONT_SIZE.getValue());
    boolean dummyButton;
    boolean alignToRight;

    public ButtonB(ButtonType type, String text, int desiredWidth, float fontSizeScale, boolean alignToRight) {
        this(type, text, desiredWidth, fontSizeScale, false, alignToRight);
    }

    public ButtonB(ButtonType type, String text, int desiredWidth, boolean alignToRight) {
        this(type, text, desiredWidth, 1, alignToRight);
    }

    public ButtonB(ButtonType type, String text, int desiredWidth) {
        this(type, text, desiredWidth, 1);
    }

    public ButtonB(ButtonType type, String text, int desiredWidth, float fontSizeScale) {
        this(type, text, desiredWidth, fontSizeScale, false);
    }

    public ButtonB(ButtonType type, String text, int desiredWidth, float fontSizeScale, boolean dummyButton, boolean alignToRight) {
        this.dummyButton = dummyButton;
        this.alignToRight = alignToRight;
        plainFont = plainFont.deriveFont(plainFont.getSize() * fontSizeScale);
        boldFont = boldFont.deriveFont(boldFont.getSize() * fontSizeScale);
        if (dummyButton) {
            setBorderPainted(false);
            setFocusPainted(false);
        }

        String imagePath = BUTTONS_UI_PATH.getValue() + type.name() + ".png";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        float scale = (float) desiredWidth / image.getWidth();
        Dimension desiredDimension = new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
        BufferedImage resized = Utils.toBufferedImage(image.getScaledInstance(desiredDimension.width, desiredDimension.height, Image.SCALE_SMOOTH));

        setFocusable(false);
        setOpaque(true);
        setFont(plainFont);
        setBorderPainted(false);
        setForeground(SCI_FI_BLUE);
        setPreferredSize(desiredDimension);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setDoubleBuffered(true);

        setText(text);
        setIcon(new ImageIcon(resized));
        setHorizontalTextPosition(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!dummyButton) {
                    setFont(boldFont);
                    setText(text);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!dummyButton) {
                    setFont(plainFont);
                    setText(text);
                }
            }
        });
    }

    @Override
    public void setText(String text) {
        if (!dummyButton && alignToRight) text = alignToRight(text);
        super.setText(text);
    }

    public String alignToRight(String text) {
        String out = text;
        while ((float) getGlassFrame().getGraphics().getFontMetrics(getFont()).stringWidth(out) / getPreferredSize().width < TEXT_SCALE.getValue())
            out = " " + out;
        return out;
    }

    public enum ButtonType {
        menu_button, small_menu_button, small_field_button, category0, category1, category2, category3, acquired_skill, unacquired_skill, active_skill
    }
}
