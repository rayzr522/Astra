package me.rayzr522.astra.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
    private AudioClip clip;

    Sound(String location) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(location));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.stop();
        clip.play();
    }

    public void stop() {
        clip.stop();
    }

    public void loop() {
        clip.loop();
    }
}
