package me.rayzr522.astra;

import javax.swing.*;
import java.awt.*;

public class Start {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final JFrame f = new JFrame("Astra");

    public static void main(String[] args) {
        Image dockIconImage = Astra.loadImage("/textures/icon.png");
        f.setIconImage(dockIconImage);

        Astra game = new Astra();

        f.add(game);
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setBackground(Color.black);

        f.setLocationRelativeTo(null);
        f.setVisible(true);

        game.start();
    }
}