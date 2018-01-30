package me.rayzr522.astra.entity;

import me.rayzr522.astra.Astra;
import me.rayzr522.astra.manager.InputManager;
import me.rayzr522.astra.type.GameObject;

import java.awt.*;

public class Star extends GameObject {
    private static final Image IMAGE = Astra.loadImage("/textures/star.png");

    private int value;

    public Star(int value, int gameWidth, int gameHeight, int sidebarWidth) {
        super(30 + sidebarWidth + Math.random() * (gameWidth - 60), 30 + Math.random() * (gameHeight - 60), 15, 15, IMAGE);

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void input(InputManager input) {

    }

    @Override
    public void tick() {

    }
}
