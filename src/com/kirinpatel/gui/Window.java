package com.kirinpatel.gui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Window extends JFrame {

    private static Window INSTANCE;
    private static AtomicBoolean isInstanceSet = new AtomicBoolean(false);

    @Nullable
    public static Window setInstance() {
        if (isInstanceSet.compareAndSet(false, true)) {
            INSTANCE = new Window();
            return INSTANCE;
        }
        return null;
    }

    public static Window getInstance() {
        if (isInstanceSet.get()) {
            return INSTANCE;
        }
        throw new IllegalStateException("Window has not been set!");
    }

    private Window() {
        super("JAV");

        setMinimumSize(new Dimension(300, 200));
        setSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(Visualizer.setInstance());

        setVisible(true);

        new Thread(() -> {
            while (Window.getInstance().isVisible()) {
                try {
                    Thread.sleep(1000 / 60);
                    Visualizer.getInstance().repaint();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
