package view.base;

import view.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.FilePaths.SLIDER_UI_PATH;
import static controller.constants.UIConstants.*;
import static view.Utils.bufferedImageClone;
import static view.Utils.darkenImage;

public class SliderB extends JSlider {
    public BufferedImage imageSave;
    public Container container;
    public JLabel backgroundLabel;
    public ButtonB labelButton;

    public SliderB(Container container, float minimum, float maximum, float current, String name) {
        this.container = container;
        try {
            imageSave = ImageIO.read(new File(SLIDER_UI_PATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setFocusable(false);
        setOpaque(false);
        setMinimum((int) (SLIDER_PRECISION_SCALE.getValue() * minimum));
        setMaximum((int) (SLIDER_PRECISION_SCALE.getValue() * maximum));
        setValue((int) (SLIDER_PRECISION_SCALE.getValue() * current));
        setupLabel(name);
        setMinorTickSpacing((int) ((getMaximum() - getMinimum()) / SLIDER_MINOR_SPACINGS_NUMBER.getValue()));
        setMajorTickSpacing((int) (getMaximum() - getMinimum() / SLIDER_MAJOR_SPACINGS_NUMBER.getValue()));
        addChangeListener(e -> repaint());
    }

    public void setupLabel(String name) {
        labelButton = new ButtonB(ButtonB.ButtonType.small_field_button, name, (int) SLIDER_LABEL_WIDTH.getValue(),
                SLIDER_LABEL_FONT_SIZE.getValue(), true, false);
    }

    public void setupSliderB() {
        float scale = (float) getWidth() / imageSave.getWidth();
        Dimension desiredDimension = new Dimension((int) (imageSave.getWidth() * scale), (int) (imageSave.getHeight() * scale));
        BufferedImage resized = Utils.toBufferedImage(imageSave.getScaledInstance(desiredDimension.width, desiredDimension.height, Image.SCALE_SMOOTH));
        this.imageSave = bufferedImageClone(resized);
        backgroundLabel = new JLabel(new ImageIcon(resized));
        setUI(new customSliderUI(this));
        fireStateChanged();
    }

    public void progress(float progress) {
        BufferedImage progressBar = bufferedImageClone(imageSave);
        Rectangle rectangle = new Rectangle((int) (progress * progressBar.getWidth()), 0,
                (int) ((1 - progress) * progressBar.getWidth()), progressBar.getHeight());
        darkenImage(progressBar, rectangle);
        backgroundLabel.setIcon(new ImageIcon(progressBar));
    }

    public float getPreciseValue() {
        return super.getValue() / SLIDER_PRECISION_SCALE.getValue();
    }

    public void setPreciseValue(float value) {
        super.setValue((int) (value * SLIDER_PRECISION_SCALE.getValue()));
    }

    @Override
    public void repaint() {
        super.repaint();
        if (container != null && container.getLayout() instanceof GridBagLayout) {
            GridBagConstraints constraints = ((GridBagLayout) container.getLayout()).getConstraints(this);
            if (constraints.gridx >= 0 || constraints.gridy >= 0) {
                if (backgroundLabel != null) {
                    progress((float) (getValue() - getMinimum()) / (getMaximum() - getMinimum()));
                    container.add(backgroundLabel, constraints);
                } else setupSliderB();
            }
        }
        revalidate();
    }

    public static class customSliderUI extends BasicSliderUI {

        public customSliderUI(JSlider sliderB) {
            super(sliderB);
            this.thumbRect = new Rectangle(5, 10);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.translate(thumbRect.x, thumbRect.y);
            graphics2D.setColor(SCI_FI_BLUE.darker().darker());
            graphics2D.fillRect(0, 0, thumbRect.width, thumbRect.height);
            graphics2D.dispose();
        }
    }
}
