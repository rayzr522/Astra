package me.rayzr522.astra.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {
    private List<Score> scores = new ArrayList<>();
    private Path saveFile;

    public ScoreManager(Path saveFile) {
        this.saveFile = saveFile;
    }

    public void load() {
        try {
            scores = Files.readAllLines(saveFile).stream()
                    .map(Score::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.write(saveFile, scores.stream()
                    .map(Score::serialize)
                    .collect(Collectors.joining("\n"))
                    .getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }
}
