package com.css.cleo.ui;

import com.css.cleo.Main;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

public class CleoTrayIcon {
    private final TrayIcon trayIcon;
    private final PopupMenu trayMenu;

    public CleoTrayIcon(String resourceName) throws IllegalStateException {
        this(Toolkit.getDefaultToolkit().getImage(Main.class.getResource(resourceName)));
    }

    public CleoTrayIcon(Image icon) throws IllegalStateException {
        if (!SystemTray.isSupported())
            throw new IllegalStateException("Tray not supported on this desktop");

        this.trayMenu = new PopupMenu();
        this.trayIcon = new TrayIcon(icon, "Cleo", trayMenu);
        trayIcon.setImageAutoSize(true);

        addIcon(trayIcon);
    }

    protected void addIcon(TrayIcon trayIcon) {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void addMenuItem(MenuItem menuItem) {
        trayMenu.add(menuItem);
    }

    public void addMenuItem(String name, ActionListener actionListener) {
        final MenuItem menuItem = new MenuItem(name);
        menuItem.addActionListener(actionListener);
        addMenuItem(menuItem);
    }

    public void displayMessage(String caption, String message, TrayIcon.MessageType type) {
        trayIcon.displayMessage(caption, message, type);
    }

    public void addDoubleClickListener(ActionListener listener) {
        this.trayIcon.addActionListener(listener);
    }

    public void addMouseListener(MouseListener listener) {
        this.trayIcon.addMouseListener(listener);
    }

    public void addMouseListener(MouseMotionListener listener) {
        this.trayIcon.addMouseMotionListener(listener);
    }

}
