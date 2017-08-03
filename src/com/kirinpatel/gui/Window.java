package com.kirinpatel.gui;

import com.kirinpatel.audio.AudioGrabber;
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

        add(new Visualizer());
        new AudioGrabber();

        setVisible(true);

        new Thread(() -> {
            while (isVisible()) {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
