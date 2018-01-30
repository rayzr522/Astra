package me.rayzr522.astra.manager;

import me.rayzr522.astra.Astra;
import me.rayzr522.astra.Start;
import me.rayzr522.astra.type.Tickable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputManager implements Tickable {
    private boolean mouseDown = false;
    private boolean mousePressed = false;
    private int mouseX = -1;
    private int mouseY = -1;

    private boolean[] keyDown = createEmptyBoolArray();
    private boolean[] keyPressed = createEmptyBoolArray();

    public InputManager(Astra game) {
        game.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDown = mousePressed = true;
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDown = mousePressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        // Key code
        Start.f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyDown[e.getKeyCode()] = keyPressed[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyDown[e.getKeyCode()] = keyPressed[e.getKeyCode()] = false;
            }
        });
    }

    @Override
    public void tick() {
        mousePressed = false;
        keyPressed = createEmptyBoolArray();
    }

    private boolean[] createEmptyBoolArray() {
        boolean[] output = new boolean[65536];
        for (int i = 0; i < output.length; i++) {
            output[i] = false;
        }
        return output;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean isKeyDown(int keyCode) {
        return keyDown[keyCode];
    }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
