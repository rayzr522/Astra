package me.rayzr522.astra.data;

public class Score {
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public static Score parse(String line) {
        String[] args = line.split("//");

        // Old format support
        if (args.length == 3) {
            return new Score(args[1], Integer.parseInt(args[2]));
        } else {
            return new Score(args[0], Integer.parseInt(args[1]));
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String serialize() {
        return String.format("%s//%d", name, score);
    }
}
