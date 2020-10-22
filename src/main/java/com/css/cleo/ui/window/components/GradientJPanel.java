package com.css.cleo.ui.window.components;

import javax.swing.*;
import java.awt.*;

public class GradientJPanel extends JPanel {
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private Color color1 = Color.white;
    private Color color2 = Color.white;

    private boolean rounded = false;
    private int arcWidth = 30;
    private int arcHeight = 30;

    public GradientJPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        super.setBackground(TRANSPARENT_COLOR);
    }

    /**
     * Create a new buffered JPanel with the specified layout manager
     *
     * @param layout the LayoutManager to use
     */
    public GradientJPanel(LayoutManager layout) {
        this(layout, true);
    }

    /**
     * Creates a new <code>JPanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>JPanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered a boolean, true for double-buffering, which
     *                         uses additional memory space to achieve fast, flicker-free
     *                         updates
     */
    public GradientJPanel(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public GradientJPanel() {
        this(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        final int height = getHeight();
        final int width = getWidth();
        final GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
        g2d.setPaint(gp);

        if (rounded)
            g2d.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
        else
            g2d.fillRect(0, 0, width, height);
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    @Override
    public void setBackground(Color bg) {
        setColor1(bg);
        setColor2(bg);
    }
}
