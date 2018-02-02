package me.rayzr522.astra.type;

public class Box {

    private final double x;
    private final double y;
    private final int width;
    private final int height;

    public Box(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean checkBounds(Box box) {

        boolean collide = false;

        double thisLeft = x;
        double thisTop = y;
        double thisRight = x + width;
        double thisBottom = y + height;

        double otherX = box.getX();
        double otherY = box.getY();
        double otherWidth = box.getWidth();
        double otherHeight = box.getHeight();

        double otherRight = otherX + otherWidth;
        double otherBottom = otherY + otherHeight;

        if (thisLeft >= otherX && thisLeft <= otherRight && thisTop >= otherY && thisTop <= otherBottom)
            collide = true;
        if (thisRight >= otherX && thisRight <= otherRight && thisTop >= otherY && thisTop <= otherBottom)
            collide = true;
        if (thisRight >= otherX && thisRight <= otherRight && thisBottom >= otherY && thisBottom <= otherBottom)
            collide = true;
        if (thisLeft >= otherX && thisLeft <= otherRight && thisBottom >= otherY && thisBottom <= otherBottom)
            collide = true;

        if (thisLeft <= otherX && thisRight >= otherRight && thisTop <= otherY && thisBottom >= otherBottom)
            collide = true;

        if (thisLeft >= otherX && thisLeft <= otherRight && thisRight >= otherRight && thisTop <= otherY && thisBottom >= otherBottom)
            collide = true;
        if (thisLeft <= otherX && thisRight >= otherRight && thisTop >= otherY && thisTop <= otherBottom && thisBottom >= otherBottom)
            collide = true;
        if (thisLeft <= otherX && thisRight <= otherRight && thisRight >= otherX && thisTop <= otherY && thisBottom >= otherBottom)
            collide = true;
        if (thisLeft <= otherX && thisRight >= otherRight && thisTop <= otherY && thisBottom <= otherBottom && thisBottom >= otherY)
            collide = true;

        return collide;
    }

}
