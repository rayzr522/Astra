package me.rayzr522.astra.entity;

import me.rayzr522.astra.Astra;
import me.rayzr522.astra.InputManager;
import me.rayzr522.astra.sound.Sounds;

import java.awt.*;

public class Player extends GameObject {
    private static final double speedMod = 96.0;
    private static Image IMAGE = Astra.loadImage("/textures/ball2.png");

    private final Astra game;

    private double mouseX;
    private double mouseY;

    public Player(Astra game) {
        super(game.getSidebarWidth() + game.getGameWidth() / 2, game.getGameHeight() / 2, IMAGE.getWidth(null), IMAGE.getHeight(null), IMAGE);

        // Prevent it from immediately moving or something
        mouseX = getX();
        mouseY = getY();

        this.game = game;
    }

    @Override
    public void input(InputManager input) {
        super.input(input);

        if (input.isMousePressed() && input.getMouseX() > game.getSidebarWidth()) {
            mouseX = input.getMouseX();
            mouseY = input.getMouseY();
        }
    }

    @Override
    public void tick() {
        setVelocity(new Vector2D((mouseX - getX()) / speedMod, (mouseY - getY()) / speedMod));

        game.addPoints(checkCollected());
    }

    private int checkCollected() {
        return game.getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Star)
                .map(gameObject -> (Star) gameObject)
                .filter(star -> getCollider().checkBounds(star.getCollider()))
                .map(star -> {
                    star.setAlive(false);
                    Sounds.beep.play();
                    return star.getValue();
                })
                .reduce(0, (a, b) -> a + b);
    }
}
