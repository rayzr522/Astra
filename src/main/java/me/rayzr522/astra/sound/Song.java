
package me.rayzr522.astra.sound;

public enum Song {

	RIVER(Sounds.river),
	LAZY(Sounds.lazy),
	FUNK(Sounds.funk);

	public Sound	song;

	Song(Sound song) {

		this.song = song;
	}

}
