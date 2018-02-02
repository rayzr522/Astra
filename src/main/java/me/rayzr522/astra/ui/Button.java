package me.rayzr522.astra.ui;

import me.rayzr522.astra.Start;

import java.awt.*;
import java.awt.event.ActionListener;

public class Button {
    private final String name;
    private final ButtonLAF laf;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final ActionListener listener;

    private final FontMetrics fontMetrics;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ButtonLAF getLAF() {
        return laf;
    }

    public boolean checkBounds(int mouseX, int mouseY) {
        int right = x + width;
        int bottom = y + height;

        return mouseX > x && mouseX < right && mouseY > y && mouseY < bottom;
    }

    public void execute() {
        listener.actionPerformed(null);
    }

    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        laf.getButtonStyle().render(this, g);

        Color text = laf.getTextColor();
        Font font = laf.getFont();

        g.setFont(font);
        g.setColor(text);

        int textWidth = (int) fontMetrics.getStringBounds(name, g).getWidth();
        int textHeight = (int) fontMetrics.getStringBounds(name, g).getHeight();

        int textX = (width / 2) - (textWidth / 2);
        int textY = (height / 2) + (textHeight / 4);

        g.drawString(name, x + textX, y + textY);
    }

}
