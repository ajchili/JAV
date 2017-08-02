package com.kirinpatel.gui;

import com.kirinpatel.audio.AudioGrabber;
import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer extends JPanel {

    private static int state = 0;
    private static int numOfPoints = 20;
    private static final int BASE_HEIGHT = 5;
    private static int heights[] = new int[numOfPoints];
    private static byte[] bytes;
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
            state += (int) e.getPreciseWheelRotation();
            if (state < 0) state = 0;
            else if (state > 1) state = 1;
            repaint();
        });
    }

    public void setAudioSteam(byte[] stream) {
        bytes = stream;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Reset graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw visualizer
        for (int i = 0; i < numOfPoints; i++) {
            switch (state) {
                case 0:
                    drawBar(g, i, 0);
                    break;
                case 1:
                    drawLine(g, i);
                    break;
            }
        }
    }

    private void drawBar(Graphics g, int index, int spacing) {
        int colorIndex = (index * 255) / numOfPoints;
        g.setColor(new Color(255 - colorIndex / 2, 255 - colorIndex, colorIndex));

        int remainder = (getWidth() % numOfPoints);
        int width = (getWidth() - remainder) / numOfPoints;
        if (heights[index] > BASE_HEIGHT) {
            heights[index]--;
        } else if (heights[index] < AudioGrabber.SIZE){
            heights[index] = BASE_HEIGHT;
        }

        g.fillRect(width * index + remainder / 2, getHeight() / 2 + heights[index] / 2, width, -heights[index]);
    }

    private void drawLine(Graphics g, int index) {
        int colorIndex = (index * 255) / numOfPoints;
        g.setColor(new Color(45, 255 - colorIndex, colorIndex));
        int remainder = (getWidth() % numOfPoints);
        int width = (getWidth() - remainder) / numOfPoints;
        if (heights[index] > BASE_HEIGHT) {
            heights[index]--;
        } else if (heights[index] < AudioGrabber.SIZE){
            heights[index] = BASE_HEIGHT;
        }

        if (index < heights.length - 1) {
            g.drawLine(width * index + remainder / 2, getHeight() / 2 + heights[index], width * index + remainder / 2 + width, getHeight() / 2 - heights[index + 1]);
        } else {
            g.drawLine(width * index + remainder / 2, getHeight() / 2 + heights[index], width * index + remainder / 2 + width, getHeight() / 2 - heights[index]);
        }
    }

    void calculateBarHeight() {
        int range = AudioGrabber.SIZE / numOfPoints;
        for (int i = 0; i < numOfPoints; i++) {
            float scale = 3.75f;
            int previousHeight = heights[i];
            int height = 0;

            if (bytes == null) {
                return;
            }

            ArrayList<Integer> values = new ArrayList<>();
            for (int j = range * i; j < range * i + range; j++) {
                values.add((int) bytes[j]);
            }

            if (values.size() > 0) {
                int a = 0;
                for (int b : values) {
                    if (Math.abs(b) < 127 / 2) {
                        a += b;
                    }
                }
                height += (a / values.size()) * scale;
            }

            if (height > previousHeight) {
                heights[i] = height;
            }
        }
        repaint();
    }
}
