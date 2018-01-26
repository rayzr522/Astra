
package me.rayzr522.astra.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip	clip;

	public Sound(String location) {

		try {
			clip = Applet.newAudioClip(Sound.class.getResource(location));
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void play() {

		try {

			new Thread() {

				public void run() {

					clip.play();

				}

			}.start();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void stop() {

		try {

			new Thread() {

				public void run() {

					clip.stop();

				}

			}.start();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void loop() {

		try {

			new Thread() {

				public void run() {

					clip.loop();

				}

			}.start();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
