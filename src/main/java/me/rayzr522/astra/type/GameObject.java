package me.rayzr522.astra.type;

import me.rayzr522.astra.manager.InputManager;
import me.rayzr522.astra.manager.TaskManager;

import java.awt.*;

public abstract class GameObject implements Tickable {
    private final int width;
    private final int height;

    private double x;
    private double y;
    private final Image image;

    private boolean alive = true;
    private Vector2D velocity = new Vector2D(0, 0);

    public GameObject(double x, double y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public Box getCollider() {
        return new Box(x, y, width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void destroy() {
        setAlive(false);
    }

    public void destroy(long millis) {
        TaskManager.INSTANCE.execute(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroy();
        });
    }

    public void input(InputManager input) {
    }

    public void render(Graphics2D g) {
        if (image != null) {
            g.drawImage(image, (int) Math.floor(x - width / 2.0), (int) Math.floor(y - height / 2.0), width, height, null);
        }
    }

    public void physics() {
        x += velocity.getX();
        y += velocity.getY();
    }

}
