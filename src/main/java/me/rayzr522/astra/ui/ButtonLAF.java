package me.rayzr522.astra.ui;

import java.awt.Color;
import java.awt.Font;

public class ButtonLAF {

	public static int		BORDER_ROUND	= 1;
	public static int		BORDER_SQUARE	= 2;
	public static ButtonLAF	DEFAULT			= new ButtonLAF(new Color(25, 25, 75), new Color(255, 255, 255), new Color(25, 25, 25), BORDER_ROUND, 1, new Font("Monospace", Font.PLAIN, 20));

	private Color			back;
	private Color			text;
	private Color			borderColor;
	private int				border;
	private int				borderWidth;
	private Font			font;

	public ButtonLAF(Color back, Color text, Color borderColor, int border, int borderWidth, Font font) {

		this.back = back;
		this.text = text;
		this.borderColor = borderColor;
		this.border = border;
		this.borderWidth = borderWidth;
		this.font = font;

	}

	public Color getBackColor() {
		return back;
	}

	public void setBackColor(Color back) {
		this.back = back;
	}

	public Color getTextColor() {
		return text;
	}

	public void setTextColor(Color text) {
		this.text = text;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

}
