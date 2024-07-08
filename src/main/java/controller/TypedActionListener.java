package controller;

import java.awt.event.ActionListener;

public abstract class TypedActionListener implements ActionListener {
    public enum Side {LEFT, RIGHT, TOP, BOTTOM, CENTER}

    public abstract static class ShrinkActionListener extends TypedActionListener {
        public Side side;

        public ShrinkActionListener(Side side) {
            this.side = side;
        }
    }

    public abstract static class MoveActionListener extends TypedActionListener {
    }

    public abstract static class RotateActionListener extends TypedActionListener {
    }

    public abstract static class DecelerateActionListener extends TypedActionListener {
    }

    public abstract static class ImpactActionListener extends TypedActionListener {
    }
}
