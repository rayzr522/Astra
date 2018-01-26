
package me.rayzr522.astra.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;

import me.rayzr522.astra.Start;
import me.rayzr522.astra.util.GraphicsUtils;

public class Button {

	private String			name;
	private ButtonLAF		laf;
	private FontMetrics		fontMetrics;

	private int				x;
	private int				y;
	private int				width;
	private int				height;

	private ActionListener	listener;

	public Button(String name, int x, int y, int width, int height, ActionListener listener) {

		this(name, ButtonLAF.DEFAULT, x, y, width, height, listener);

	}

	public Button(String name, ButtonLAF laf, int x, int y, int width, int height, ActionListener listener) {

		this.name = name;
		this.laf = laf;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.listener = listener;
		
		this.fontMetrics = Start.f.getFontMetrics(laf.getFont());

	}

	public void setX(int x) {

		this.x = x;

	}

	public int getX() {

		return x;

	}

	public void setY(int y) {

		this.y = y;

	}

	public int getY() {

		return y;

	}

	public void setPos(int x, int y) {

		this.x = x;
		this.y = y;

	}

	public void setWidth(int width) {

		this.width = width;

	}

	public int getWidth() {

		return width;

	}

	public void setHeight(int height) {

		this.height = height;

	}

	public int getHeight() {

		return height;

	}

	public void setSize(int width, int height) {

		this.width = width;
		this.height = height;

	}

	public void setButtonLAF(ButtonLAF laf) {

		this.laf = laf;

	}

	public ButtonLAF getButtonLAF() {

		return laf;

	}

	public boolean checkBounds(int mouseX, int mouseY) {

		int left = x;
		int top = y;
		int right = x + width;
		int bottom = y + height;

		if (mouseX > left && mouseX < right && mouseY > top && mouseY < bottom) {

		return true;

		}

		return false;

	}

	public void execute() {

		listener.actionPerformed(null);

	}

	public void draw(Graphics2D g) {

		GraphicsUtils.setAntialiasing(g, true);

		Color back = laf.getBackColor();
		Color text = laf.getTextColor();
		Color borderColor = laf.getBorderColor();
		int borderWidth = laf.getBorderWidth();
		Font font = laf.getFont();

		if (laf.getBorder() == ButtonLAF.BORDER_SQUARE) {

			g.setColor(borderColor);
			g.fillRect(x, y, width, height);
			g.setColor(back);
			g.fillRect(x + borderWidth, y + borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));

		} else if (laf.getBorder() == ButtonLAF.BORDER_ROUND) {

			g.setColor(borderColor);
			g.fillRoundRect(x, y, width, height, 20, 20);
			g.setColor(back);
			g.fillRoundRect(x + borderWidth, y + borderWidth, width - (borderWidth * 2), height - (borderWidth * 2), 20, 20);

		}

		g.setFont(font);
		g.setColor(text);

		int fontSize = font.getSize();
//		int textWidth = name.length() * fontSize;
		int textWidth = (int) fontMetrics.getStringBounds(name, g).getWidth();
		int textHeight = (int) fontMetrics.getStringBounds(name, g).getHeight();

		int textX = (width / 2) - (textWidth / 2);
		int textY = (height / 2) + (textHeight / 4);

		g.drawString(name, x + textX, y + textY);

	}

}
