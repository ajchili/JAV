package com.kirinpatel.gui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer extends JPanel {

    private static int numOfBars = 40;
    private static byte[] audioSteam;
    private static int length;
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

    private Visualizer() {
        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() == 1.0 && numOfBars > 20) {
                numOfBars--;
            } else if (e.getPreciseWheelRotation() == -1.0) {
                numOfBars++;
            }
            repaint();
        });
    }

    public void setAudioSteam(byte[] stream, int length) {
        Visualizer.audioSteam = stream;
        Visualizer.length = length;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Reset graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw visualizer
        g.setColor(Color.WHITE);
        for (int i = 0; i < numOfBars; i++) {
            drawBar(g, i, 2);
        }
    }

    private void drawBar(Graphics g, int index, int spacing) {
        int remainder = (getWidth() % numOfBars);
        int width = (getWidth() - remainder) / numOfBars;
        g.fillRect(width * index + spacing * index - remainder / 2, getHeight() - 10, width, 10);
    }
}
