package view.containers;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public interface TopElement {
    JFrame getFrame();

    default void pinOnTop() {
        if (this instanceof Component) {
            if (getFrame() != null && Arrays.stream(getFrame().getContentPane().getComponents()).toList().contains(this)) {
                for (Component component : getFrame().getContentPane().getComponents()) {
                    if (getFrame().getContentPane().getComponentZOrder(component) < getFrame().getContentPane().getComponentZOrder((Component) this)) {
                        getFrame().getContentPane().setComponentZOrder(component, getFrame().getContentPane().getComponentZOrder(component) + 1);
                    }
                }
                getFrame().getContentPane().setComponentZOrder((Component) this, 0);
                getFrame().repaint();
            }
        }
    }
}
