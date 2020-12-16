package com.css.cleo.ui.window;

import com.css.cleo.ui.window.components.GradientJPanel;
import com.css.cleo.ui.window.components.PlaceholderJTextField;
import edu.cmu.sphinx.api.SpeechResult;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VoiceView extends JFrame {
    private final JPanel rootPanel;
    private final GradientJPanel contentPanel;
    private final PlaceholderJTextField textField;
    private Runnable onShow;
    private Runnable onHide;

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
        contentPanel.setRounded(true);

        final Dimension contentSize = new Dimension(Math.max(800, screenSize.width / 2), 50);
        contentPanel.setMaximumSize(contentSize);
        contentPanel.setPreferredSize(contentSize);
        contentPanel.setMinimumSize(contentSize);

        textField = new PlaceholderJTextField();
        contentPanel.add(textField, new GridBagConstraints());
        final Dimension fieldSize = new Dimension(contentSize.width - 20, contentSize.height - 20);
        textField.setMaximumSize(fieldSize);
        textField.setPreferredSize(fieldSize);
        textField.setMinimumSize(fieldSize);
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.addKeyListener(eventListener);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Default", Font.PLAIN, 20));
        textField.setPlaceholder("What we will to do?");
        textField.setDisabledTextColor(new Color(67, 67, 67));
        textField.setEditable(false);
        textField.setForeground(Color.BLACK);

        setAlwaysOnTop(true);
    }

    public void setSuccess(boolean success) {
        if (success) {
            textField.setForeground(Color.GREEN);
        } else {
            textField.setForeground(Color.RED);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setFocusable(true);
            setFocusableWindowState(true);
            requestFocus();
            textField.requestFocus();
            if (onShow != null)
                onShow.run();
        } else if (onHide != null)
            onHide.run();
    }

    public void restore(boolean visible) {
        if (visible) {
            textField.setForeground(Color.BLACK);
            setSpeechResult(null);
        }
        setVisible(visible);
    }

    public void setSpeechResult(@Nullable SpeechResult speechResult) {
        if (speechResult == null) {
            textField.setText("");
            return;
        }

        textField.setText(speechResult.getHypothesis());
    }

    public void setOnShow(Runnable onShow) {
        this.onShow = onShow;
    }

    public void setOnHide(Runnable onHide) {
        this.onHide = onHide;
    }

    private class EventListener implements MouseListener, KeyListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!contentPanel.getBounds().contains(e.getX(), e.getY()))
                restore(false);
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
                restore(false);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
