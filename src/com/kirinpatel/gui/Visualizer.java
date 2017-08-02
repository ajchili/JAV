package com.kirinpatel.gui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer extends JPanel {

    private static int numOfBars = 20;
    private static final int BASE_HEIGHT = 5;
    private static int heights[] = new int[numOfBars];
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
                heights = new int[numOfBars];
            } else if (e.getPreciseWheelRotation() == -1.0) {
                numOfBars++;
                heights = new int[numOfBars];
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
        for (int i = 0; i < numOfBars; i++) {
            g.setColor(new Color(45, 255 - ((255 / numOfBars) * i), (255 / numOfBars) * i));
            drawBar(g, i, 2);
        }
    }

    private void drawBar(Graphics g, int index, int spacing) {
        int remainder = (getWidth() % numOfBars);
        int width = (getWidth() - remainder) / numOfBars;
        if (heights[index] > BASE_HEIGHT) {
            heights[index]--;
        } else if (heights[index] < 1024){
            heights[index] = BASE_HEIGHT;
        }
        g.fillRect(width * index + spacing * index - remainder / 2, getHeight() - heights[index], width, heights[index]);
    }

    void calculateBarHeight() {
        for (int i = 0; i < numOfBars; i++) {
            float scale = 2.5f;
            int previousHeight = heights[i];
            int height = BASE_HEIGHT;

            ArrayList<Integer> values = new ArrayList<>();
            for (int j = numOfBars * i; j < numOfBars * i + numOfBars; j++) {
                if (j < 1024 && Visualizer.length == 1024) {
                    values.add((int) Visualizer.audioSteam[j]);
                } else {
                    break;
                }
            }

            if (values.size() > 0) {
                int a = 0;
                for (int b : values) {
                    a += b;
                }
                a /= values.size();
                height += Math.abs(a * scale);
            }

            if (height > previousHeight) {
                heights[i] = height;
            }
        }
        repaint();
    }
}
