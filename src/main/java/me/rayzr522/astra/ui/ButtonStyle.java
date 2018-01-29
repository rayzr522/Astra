package me.rayzr522.astra.ui;

import java.awt.*;

public enum ButtonStyle {
    SQUARE {
        @Override
        public void render(Button button, Graphics2D g) {
            Color borderColor = button.getLAF().getBorderColor();
            Color back = button.getLAF().getBackColor();
            int x = button.getX();
            int y = button.getY();
            int width = button.getWidth();
            int height = button.getHeight();
            int borderWidth = button.getLAF().getBorderWidth();

            g.setColor(borderColor);
            g.fillRect(x, y, width, height);
            g.setColor(back);
            g.fillRect(x + borderWidth, y + borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        }
    },
    ROUND {
        @Override
        public void render(Button button, Graphics2D g) {
            Color borderColor = button.getLAF().getBorderColor();
            Color back = button.getLAF().getBackColor();
            int x = button.getX();
            int y = button.getY();
            int width = button.getWidth();
            int height = button.getHeight();
            int borderWidth = button.getLAF().getBorderWidth();

            g.setColor(borderColor);
            g.fillRoundRect(x, y, width, height, 20, 20);
            g.setColor(back);
            g.fillRoundRect(x + borderWidth, y + borderWidth, width - (borderWidth * 2), height - (borderWidth * 2), 20, 20);
        }
    };

    public abstract void render(Button button, Graphics2D g);
}
