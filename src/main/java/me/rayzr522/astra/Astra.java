package me.rayzr522.astra;

import me.rayzr522.astra.data.Score;
import me.rayzr522.astra.data.ScoreManager;
import me.rayzr522.astra.entity.Message;
import me.rayzr522.astra.entity.Player;
import me.rayzr522.astra.entity.Star;
import me.rayzr522.astra.manager.InputManager;
import me.rayzr522.astra.manager.UIManager;
import me.rayzr522.astra.sound.Song;
import me.rayzr522.astra.type.GameObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Astra extends JPanel implements Runnable {
    private static final long serialVersionUID = -88667578292361415L;

    // Display
    private final Font uiFont;
    private final int width = Start.WIDTH;
    private final int height = Start.HEIGHT;
    private final int sidebarWidth;
    private final int textSize;

    // Managers
    private final ScoreManager scoreManager;
    private final InputManager inputManager;
    private final UIManager uiManager;
    private List<GameObject> gameObjects;

    // Resources
    private Song currentSong = Song.RIVER;

    // State
    private boolean paused = false;
    private boolean running = false;
    private boolean gameStarted = false;

    // Timing
    private long startTime;
    private long elapsedTime;
    private long lastScoreUpdateTime;
    private long pausedStartTime;
    private long pausedTime;
    private long lastFrameUpdate;
    private int pointsToAdd;
    private int frames;
    private int fps;

    // Logic
    private int level;
    private int numberOfClicks;
    private int numStars;
    private int score;

    Astra() {
        this.sidebarWidth = (int) Math.floor(width / 4);
        this.textSize = (int) Math.floor(height * 0.026041667);

        Dimension size = new Dimension(width, height);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        lastFrameUpdate = System.nanoTime();

        scoreManager = new ScoreManager();
        scoreManager.load();
        inputManager = new InputManager(this);
        uiManager = new UIManager(this);

        gameObjects = new ArrayList<>();
        gameObjects.add(new Player(this));

        currentSong.getSong().loop();
        uiFont = new Font("Roboto", Font.PLAIN, textSize);

        gameObjects.add(
                new Message(
                        textSize, textSize,
                        () -> "SCORE: " + score,
                        Message.Align.LEFT, Color.white, uiFont
                )
        );
        gameObjects.add(
                new Message(
                        textSize, textSize * 2.5,
                        () -> "LEVEL: " + level,
                        Message.Align.LEFT, Color.white, uiFont
                )
        );
        gameObjects.add(
                new Message(
                        textSize, textSize * 4,
                        () -> "FPS: " + fps,
                        Message.Align.LEFT, Color.white, uiFont
                )
        );
    }

    public static Image loadImage(String location) {
        try {
            return ImageIO.read(Astra.class.getResource(location));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start() {
        if (running) return;
        running = true;

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
        }
    }

    private void update() {
        if (inputManager.isMousePressed()) {
            numberOfClicks++;
        }

        if (inputManager.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            togglePause();
        }

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

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (!paused) {
            g2d.setColor(new Color(30, 150, 230));
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, sidebarWidth, height);

            gameObjects.forEach(gameObject -> gameObject.render(g2d));

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

    public boolean save(String name) {
        Score newScore = new Score(name, score);
        boolean highScore = scoreManager.getScores().stream().noneMatch(score -> score.getScore() >= newScore.getScore());

        scoreManager.addScore(newScore);
        scoreManager.save();

        return highScore;
    }


    private void startLevel() {
        if (gameStarted) {
            pausedTime = 0;
            lastScoreUpdateTime = System.currentTimeMillis();

            int timeBonus = (int) Math.floor(level / 7);
            if (timeBonus < 1) timeBonus = 1;
            timeBonus = (timeBonus * 4) - (int) (Math.floor(elapsedTime / 5000));

            int clickBonus = numStars - numberOfClicks;
            if (timeBonus < 0) timeBonus = 0;
            if (clickBonus < 0) clickBonus = 0;

            pointsToAdd += timeBonus + clickBonus;
            numberOfClicks = 0;

            int finalTimeBonus = timeBonus;
            Message timeBonusMessage = new Message(
                    textSize, textSize * 5.5,
                    () -> "Time Bonus: +" + finalTimeBonus,
                    Message.Align.LEFT, Color.white, uiFont
            );

            timeBonusMessage.destroy(2000);

            int finalClickBonus = clickBonus;
            Message clickBonusMessage = new Message(
                    textSize, textSize * 7.0,
                    () -> "Click Bonus: +" + finalClickBonus,
                    Message.Align.LEFT, Color.white, uiFont
            );

            clickBonusMessage.destroy(2000);

            gameObjects.add(timeBonusMessage);
            gameObjects.add(clickBonusMessage);
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


        Color color = new Color(255, 255, 255, 170);
        Font font = new Font("Menlo", Font.PLAIN, textSize * 2);

        Message message = new Message(
                sidebarWidth + getGameWidth() / 2,
                textSize * 2,
                () -> "LEVEL " + level,
                Message.Align.CENTER,
                color,
                font
        );

        message.destroy(2000);

        gameObjects.add(message);
    }

    private boolean isLevelFinished() {
        return !gameStarted || gameObjects.stream().noneMatch(gameObject -> gameObject instanceof Star && gameObject.isAlive());
    }


    private void togglePause() {
        paused = !paused;
        if (paused) {
            pausedStartTime = System.currentTimeMillis();
            currentSong.getSong().stop();
        } else {
            currentSong.getSong().loop();
            pausedStartTime = 0;
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