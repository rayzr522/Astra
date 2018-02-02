package me.rayzr522.astra.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {
    private List<Score> scores = new ArrayList<>();
    private final Path saveFile;

    public ScoreManager() {
        Path dataDirectory = new File(System.getProperty("user.home")).toPath().resolve(".astra");
        if (!Files.isDirectory(dataDirectory)) {
            System.out.println("Making directory at '" + dataDirectory.toAbsolutePath() + "'");
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to create data directory!");
                System.exit(1);
            }
        }

        this.saveFile = dataDirectory.resolve("save.data");
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
