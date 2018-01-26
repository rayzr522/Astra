package me.rayzr522.astra.entity;

import java.awt.Image;

import me.rayzr522.astra.Board;

public class Star {

	private int		value;
	private double	x;
	private double	y;

	private Image	image;
	private int		width;
	private int		height;
	private int		playWidth;
	private int		playHeight;
	private int		sidebarWidth;

	private boolean	alive;

	public Star(int value, int width, int height, int gameWidth, int gameHeight, int sidebarWidth) {

		image = Board.loadImage("/textures/star.png");
		this.width = width;
		this.height = height;
		this.playWidth = gameWidth - sidebarWidth;
		this.playHeight = gameHeight;
		this.sidebarWidth = sidebarWidth;

		x = (Math.random() + 0.001) * playWidth;
		if (x > playWidth - 30 - width) {

			x = playWidth - 30 - width;

		}
		if (x < 30) {

			x = 30;

		}

		y = (Math.random() + 0.001) * playHeight;
		if (y > playHeight - 30 - height) {

			y = playHeight - 30 - height;

		}
		if (y < 30) {

			y = 30;

		}

		x += sidebarWidth;

		this.value = value;

		alive = true;

	}

	public int getValue() {
		return value;
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

	public double getY() {
		return y;
	}

	public void setX(double x) {

		this.x = x;

	}

	public void setY(double y) {

		this.y = y;

	}

	public Image getImage() {
		return image;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
