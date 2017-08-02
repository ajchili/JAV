package com.kirinpatel.audio;

import com.kirinpatel.gui.Visualizer;
import com.kirinpatel.gui.Window;
import com.sun.istack.internal.Nullable;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AudioGrabber {

    private static Mixer.Info mixer;
    private final static AudioFormat FORMAT = new AudioFormat(
            41000,
            16,
            2,
            true,
            false);
    private static TargetDataLine line;
    private static AudioInputStream stream;
    private static AudioGrabber INSTANCE;
    private static AtomicBoolean isInstanceSet = new AtomicBoolean(false);

    @Nullable
    public static AudioGrabber setInstance() {
        if (isInstanceSet.compareAndSet(false, true)) {
            INSTANCE = new AudioGrabber();
            return INSTANCE;
        }
        return null;
    }

    public static AudioGrabber getInstance() {
        if (isInstanceSet.get()) {
            return INSTANCE;
        }
        throw new IllegalStateException("AudioGrabber has not been set!");
    }

    private AudioGrabber() {
        for (Mixer.Info mixer : AudioSystem.getMixerInfo()) {
            if (mixer.getName().contains("Port CABLE Output")) {
                AudioGrabber.mixer = mixer;
                break;
            }
        }

        if (AudioGrabber.mixer == null) {
            Window.getInstance().dispose();
            throw new IllegalStateException("Unable to get CABLE Output!");
        }

        try {
            line = AudioSystem.getTargetDataLine(FORMAT);
            line.open();
        } catch (LineUnavailableException e) {
            Window.getInstance().dispose();
            throw new IllegalStateException("Unable to read from CABLE Output!");
        }

        new Thread(() -> {
            line.start();
            byte[] data = new byte[1024];

            while(Window.getInstance().isVisible()) {
                Visualizer.getInstance().setAudioSteam(data, line.read(data, 0, data.length));
            }

            line.stop();
        }).start();
    }
}
