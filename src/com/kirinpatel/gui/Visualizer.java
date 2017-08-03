package com.kirinpatel.gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer extends JPanel {

    private static Visualizer INSTANCE;
    private static AtomicBoolean isInstanceSet = new AtomicBoolean(false);

    public static Visualizer getInstance() {
        if (isInstanceSet.get()) {
            return INSTANCE;
        }
        throw new IllegalStateException("Visualizer has not been set!");
    }

    public Visualizer () {
        if (isInstanceSet.compareAndSet(false, true)) {
            INSTANCE = new Visualizer();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Reset graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw visualizer
    }
}
