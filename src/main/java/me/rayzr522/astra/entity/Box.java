package me.rayzr522.astra.entity;

public class Box {

	private double	x;
	private double	y;
	private double	width;
	private double	height;

	public Box(double x, double y, double width, double height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

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

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
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

		double otherLeft = otherX;
		double otherTop = otherY;
		double otherRight = otherX + otherWidth;
		double otherBottom = otherY + otherHeight;

		if (thisLeft >= otherLeft && thisLeft <= otherRight && thisTop >= otherTop && thisTop <= otherBottom) collide = true;
		if (thisRight >= otherLeft && thisRight <= otherRight && thisTop >= otherTop && thisTop <= otherBottom) collide = true;
		if (thisRight >= otherLeft && thisRight <= otherRight && thisBottom >= otherTop && thisBottom <= otherBottom) collide = true;
		if (thisLeft >= otherLeft && thisLeft <= otherRight && thisBottom >= otherTop && thisBottom <= otherBottom) collide = true;

		if (thisLeft <= otherLeft && thisRight >= otherRight && thisTop <= otherTop && thisBottom >= otherBottom) collide = true;

		if (thisLeft >= otherLeft && thisLeft <= otherRight && thisRight >= otherRight && thisTop <= otherTop && thisBottom >= otherBottom) collide = true;
		if (thisLeft <= otherLeft && thisRight >= otherRight && thisTop >= otherTop && thisTop <= otherBottom && thisBottom >= otherBottom) collide = true;
		if (thisLeft <= otherLeft && thisRight <= otherRight && thisRight >= otherLeft && thisTop <= otherTop && thisBottom >= otherBottom) collide = true;
		if (thisLeft <= otherLeft && thisRight >= otherRight && thisTop <= otherTop && thisBottom <= otherBottom && thisBottom >= otherTop) collide = true;

		return collide;

	}

}
