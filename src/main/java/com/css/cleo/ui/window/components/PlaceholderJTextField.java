package com.css.cleo.ui.window.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("serial")
public class PlaceholderJTextField extends JTextField {
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private Shape shape;

    private String placeholder;
    private int length = 0;

    public PlaceholderJTextField(String placeholder) {
        setPlaceholder(placeholder);
        setOpaque(true);
    }

    public PlaceholderJTextField() {
        this(null);
    }

    /**
     * Gets text, returns placeholder if nothing specified
     */
    @Override
    public String getText() {
        String text = super.getText();

        if (text.trim().length() == 0 && placeholder != null) {
            text = placeholder;
        }

        return text;
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        {
            final Color background = getBackground();
            setBackground(TRANSPARENT_COLOR);
            super.paintComponent(g);
            setBackground(background);
        }

        if (super.getText().length() > 0 || placeholder == null)
            return;

        g2d.setColor(super.getDisabledTextColor());

        final FontMetrics fontMetrics = g.getFontMetrics();
        if (length == -1)
            length = fontMetrics.stringWidth(placeholder);

        int x = getHorizontalAlignment() == JTextField.CENTER ? (getSize().width - length) / 2 : 0;
        g2d.drawString(placeholder, x, fontMetrics.getMaxAscent() + getInsets().top);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
        return shape.contains(x, y);
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        this.length = -1;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}