package com.css.cleo.ui.window;

import com.css.cleo.ui.window.components.GradientJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VoiceView extends JFrame {
    private final JPanel rootPanel;
    private final GradientJPanel contentPanel;

    public VoiceView() {
        setUndecorated(true);
        setDefaultLookAndFeelDecorated(false);

        setBackground(new Color(0, 0, 0, 0));
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(new Color(19, 19, 19, 233));

        final EventListener eventListener = new EventListener();
        addMouseListener(eventListener);
        addKeyListener(eventListener);

        setContentPane(rootPanel);
        setSize(screenSize);
        setLocationRelativeTo(null);

        contentPanel = new GradientJPanel(new GridBagLayout());
        rootPanel.add(contentPanel, new GridBagConstraints());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        contentPanel.setColor1(new Color(177, 29, 119));
        contentPanel.setColor2(new Color(50, 136, 202));

        final Dimension contentSize = new Dimension(Math.max(800, screenSize.width / 2), 100);
        contentPanel.setMaximumSize(contentSize);
        contentPanel.setPreferredSize(contentSize);
        contentPanel.setMinimumSize(contentSize);

        setAlwaysOnTop(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setFocusable(true);
            setFocusableWindowState(true);
            requestFocus();
        }
    }

    private class EventListener implements MouseListener, KeyListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!contentPanel.getBounds().contains(e.getX(), e.getY()))
                setVisible(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                setVisible(false);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
