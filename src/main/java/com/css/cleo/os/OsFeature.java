package com.css.cleo.os;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

public interface OsFeature {
    /**
     * Returns executable files that are
     * accessed from command line or terminal.
     *
     * @return not nullable executable files or
     * links (shortcut/symlink/desktop etc. depending on os) list.
     */
    @NotNull
    Collection<File> getTools();

    /**
     * Returns executable files that are
     * desktop applications.
     *
     * @return not nullable executable files or
     * links (shortcut/symlink/desktop etc.) list.
     */
    @NotNull
    Collection<File> getDesktopApps();

    /**
     * Opens in browser website by provided url.
     *
     * @param url address of page
     */
    default void executeBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
