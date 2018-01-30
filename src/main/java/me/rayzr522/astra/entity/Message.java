package me.rayzr522.astra.entity;

import me.rayzr522.astra.type.GameObject;

import java.awt.*;
import java.util.function.Supplier;

public class Message extends GameObject {
    private final Supplier<String> message;
    private final Align align;
    private final Color color;
    private final Font font;

    public Message(double x, double y, Supplier<String> message, Align align, Color color, Font font) {
        super(x, y, 0, 0, null);

        this.message = message;
        this.align = align;
        this.color = color;
        this.font = font;
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(color);
        g.setFont(font);

        String text = message.get();

        double width = g.getFontMetrics().stringWidth(text) * 1.0;
        g.drawString(text, (int) (getX() - width * align.offset), (int) getY());
    }

    @Override
    public void tick() {

    }

    public enum Align {
        LEFT(0.0), CENTER(0.5), RIGHT(1.0);

        private final double offset;

        Align(double offset) {
            this.offset = offset;
        }
    }
}
