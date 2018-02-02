package me.rayzr522.astra.ui;

import java.awt.*;

public class ButtonLAF {
    private final Color back;
    private final Color text;
    private final Color borderColor;
    private final ButtonStyle buttonStyle;
    private final int borderWidth;
    private final Font font;

    public ButtonLAF(Color back, Color text, Color borderColor, ButtonStyle buttonStyle, int borderWidth, Font font) {
        this.back = back;
        this.text = text;
        this.borderColor = borderColor;
        this.buttonStyle = buttonStyle;
        this.borderWidth = borderWidth;
        this.font = font;
    }

    public Color getBackColor() {
        return back;
    }

    public Color getTextColor() {
        return text;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Font getFont() {
        return font;
    }

}
