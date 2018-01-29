package me.rayzr522.astra.sound;

public enum Song {
    RIVER(Sounds.river),
    LAZY(Sounds.lazy),
    FUNK(Sounds.funk);

    private Sound song;

    Song(Sound song) {
        this.song = song;
    }

    public Sound getSong() {
        return song;
    }
}
