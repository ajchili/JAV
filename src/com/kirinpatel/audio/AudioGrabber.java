package com.kirinpatel.audio;

import com.sun.istack.internal.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class AudioGrabber {

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
}
