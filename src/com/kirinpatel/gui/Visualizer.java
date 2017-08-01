package com.kirinpatel.gui;

import com.sun.istack.internal.Nullable;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer extends Canvas {

    private static Visualizer INSTANCE;
    private static AtomicBoolean isInstanceSet = new AtomicBoolean(false);

    @Nullable
    static Visualizer setInstance() {
        if (isInstanceSet.compareAndSet(false, true)) {
            INSTANCE = new Visualizer();
            return INSTANCE;
        }
        return null;
    }

    public static Visualizer getInstance() {
        if (isInstanceSet.get()) {
            return INSTANCE;
        }
        throw new IllegalStateException("Visualizer has not been set!");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Reset graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
