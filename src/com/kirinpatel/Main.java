package com.kirinpatel;

import com.kirinpatel.audio.AudioGrabber;
import com.kirinpatel.gui.Window;

public class Main {

    public static void main(String[] args) {
        Window.setInstance();
        AudioGrabber.setInstance();
    }
}
