package me.rayzr522.astra;

import me.rayzr522.astra.data.Score;
import me.rayzr522.astra.data.ScoreManager;
import me.rayzr522.astra.entity.GameObject;
import me.rayzr522.astra.entity.Player;
import me.rayzr522.astra.entity.Star;
import me.rayzr522.astra.sound.Song;
import me.rayzr522.astra.ui.UIManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Astra extends JPanel implements Runnable {
    private static final long serialVersionUID = -88667578292361415L;
    private static int score;
    private int pointsToAdd;
    private boolean paused = false;
    private Song currentSong = Song.RIVER;
    private long startTime;
    private long levelStartTime;
    private long elapsedTime;
    private long lastScoreUpdateTime;
    private long pausedStartTime;
    private long pausedTime;
    private long lastFrameUpdate;
    private int frames;
    private int fps;
    private int width;
    private int height;
    private int sidebarWidth;
    private int textSize;
    private int level;
    private int numberOfClicks;
    private int msgDisplayTime;
    private int numStars;
    private BufferedImage background;
    private String[] messages;
    private boolean running = false;
    private boolean alreadyUnPaused = false;
    private boolean gameStarted = false;
    private boolean oldPausedValue = false;

    private ScoreManager scoreManager;
    private InputManager inputManager;
    private UIManager uiManager;
    private List<GameObject> gameObjects;

    Astra(int width, int height) {
        Dimension size = new Dimension(width, height);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        this.width = width;
        this.height = height;

        this.sidebarWidth = (int) Math.floor(width / 4);
        this.textSize = (int) Math.floor(height * 0.026041667);

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

        Path saveFile = dataDirectory.resolve("save.data");
        scoreManager = new ScoreManager(saveFile);
        scoreManager.load();

        inputManager = new InputManager(this);
        uiManager = new UIManager(this);

        msgDisplayTime = 5000;
        messages = new String[10];

        for (int i = 0; i < messages.length; i++)
            messages[i] = "";

        Image backgroundImage = loadImage("/textures/air.png");
        assert backgroundImage != null;

        int tileSize = backgroundImage.getWidth(null);
        background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y <= height / tileSize; y++) {
            for (int x = 0; x <= width / tileSize; x++) {
                background.getGraphics().drawImage(backgroundImage, x * tileSize, y * tileSize, null);
            }
        }

        lastFrameUpdate = System.nanoTime();


        gameObjects = new ArrayList<>();
        gameObjects.add(new Player(this));
    }

    public static Image loadImage(String location) {
        try {
            return ImageIO.read(Astra.class.getResource(location));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void update() {
        if (inputManager.isMousePressed()) {
            numberOfClicks++;
        }

        if (inputManager.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            paused = !paused;
        }

        pauseHandling();

        if (paused && pausedStartTime != 0) pausedTime = System.currentTimeMillis() - pausedStartTime;

        if (gameStarted) {
            elapsedTime = System.currentTimeMillis() - startTime - pausedTime;
        }

        if (isLevelFinished() && !paused) {
            startLevel();
        }

        if (!paused) {
            gameObjects.forEach(gameObject -> gameObject.input(inputManager));
            gameObjects.forEach(GameObject::tick);
            gameObjects = gameObjects.stream().filter(GameObject::isAlive).collect(Collectors.toList());

            gameObjects.forEach(GameObject::physics);

            if (pointsToAdd > 0) {
                if (System.currentTimeMillis() - lastScoreUpdateTime - pausedTime >= 50) {
                    lastScoreUpdateTime = System.currentTimeMillis();
                    score++;
                    pointsToAdd--;
                }
            }
        }

        uiManager.tick();
        inputManager.tick();

        repaint();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean save(String name) {
        Score newScore = new Score(name, score);
        boolean highScore = scoreManager.getScores().stream().noneMatch(score -> score.getScore() >= newScore.getScore());

        scoreManager.addScore(newScore);
        scoreManager.save();

        return highScore;
    }


    public void start() {
        if (running) return;
        running = true;

        Thread thread = new Thread(this);
        thread.start();
    }

    private void startLevel() {
        levelStartTime = System.currentTimeMillis();
        if (gameStarted) {
            pausedTime = 0;
            lastScoreUpdateTime = levelStartTime;
            int timeBonus = (int) Math.floor(level / 7);
            if (timeBonus < 1) timeBonus = 1;
            timeBonus = (timeBonus * 4) - (int) (Math.floor(elapsedTime / 5000));
            int clickBonus = numStars - numberOfClicks;
            if (timeBonus < 0) timeBonus = 0;
            if (clickBonus < 0) clickBonus = 0;
            messages[0] = "Time Bonus: +" + timeBonus;
            messages[1] = "Click Bonus: +" + clickBonus;
            pointsToAdd += timeBonus + clickBonus;
            numberOfClicks = 0;
        }

        gameStarted = true;
        startTime = System.currentTimeMillis();
        level++;

        numStars = (int) Math.floor(level / 2);
        if (numStars == 0) numStars = 1;

        int value = (int) Math.floor(level / 4);
        if (value < 1) value = 1;

        for (int i = 0; i < numStars; i++) {
            gameObjects.add(new Star(value, getGameWidth(), getGameHeight(), sidebarWidth));
        }
    }

    private boolean isLevelFinished() {
        return !gameStarted || gameObjects.stream().noneMatch(gameObject -> gameObject instanceof Star && gameObject.isAlive());
    }


    private void pauseHandling() {
        if (!oldPausedValue && paused) {
            pausedStartTime = System.currentTimeMillis();
        }

        if (paused) {
            currentSong.getSong().stop();
            alreadyUnPaused = false;
        } else {
            if (!alreadyUnPaused) currentSong.getSong().loop();
            pausedStartTime = 0;
            alreadyUnPaused = true;
        }

        oldPausedValue = paused;
    }

    @Override
    public void run() {
        while (running) {
            update();
        }
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (!paused) {
            g2d.drawImage(background, 0, 0, null);

            gameObjects.forEach(gameObject -> gameObject.render(g2d));

            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, sidebarWidth, height);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("Roboto", Font.PLAIN, textSize));
            g2d.drawString("SCORE: " + score, textSize, textSize);
            g2d.drawString("LEVEL: " + level, textSize, textSize + (int) (textSize * 1.5));
            g2d.drawString("FPS: " + fps, textSize, textSize + (int) (textSize * 1.5) * 2);

            if (System.currentTimeMillis() - levelStartTime - pausedTime <= msgDisplayTime && level > 1) {
                for (int i = 0; i < messages.length; i++) {
                    g2d.drawString(messages[i], textSize, (int) (i * (textSize * 1.5)) + (int) (((textSize * 1.5) * 3 + textSize)));
                }
            }

            if (System.currentTimeMillis() - levelStartTime - pausedTime <= msgDisplayTime) {
                int gameWidth = width - sidebarWidth;
                Color color = new Color(255, 255, 255, 170);
                g2d.setColor(color);
                g2d.setFont(new Font("Menlo", Font.PLAIN, textSize * 2));
                String text = "LEVEL " + level;
                g2d.drawString(text, sidebarWidth + ((gameWidth / 2) - ((text.length() / 2) * textSize * 2)), textSize * 2);
            }

            uiManager.render(g2d);
        } else {

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Menlo", Font.PLAIN, width / 8));

            int textWidth = g2d.getFontMetrics().stringWidth("PAUSED");
            g2d.drawString("PAUSED", (width - textWidth) / 2, (height / 2));

            g2d.setColor(Color.white);
            g2d.setFont(new Font("Menlo", Font.PLAIN, textSize));

            List<Score> scores = scoreManager.getScores();
            scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

            for (int i = 0; i < Math.min(scores.size(), 10); i++) {
                Score score = scores.get(i);
                int x = 10;
                int y = 30 + (i * textSize * 2);
                g2d.drawString(i + 1 + ". " + score.getName() + " - " + score.getScore(), x, y);
            }
        }

        frames++;
        if (System.nanoTime() - lastFrameUpdate >= 1000000000) {
            lastFrameUpdate = System.nanoTime();
            fps = frames;
            frames = 0;
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addPoints(int amount) {
        pointsToAdd += amount;
    }

    public int getSidebarWidth() {
        return sidebarWidth;
    }

    public int getGameWidth() {
        return width - sidebarWidth;
    }

    public int getGameHeight() {
        return height;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }
}