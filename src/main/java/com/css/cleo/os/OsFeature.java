package com.css.cleo.os;

import com.css.cleo.util.GuiUtil;
import org.jetbrains.annotations.NotNull;

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
                GuiUtil.showError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Reboots operation system if possible.
     */
    void reboot();

    /**
     * Shutdowns operation system if possible.
     */
    void shutdown();

    /**
     * Hibernates operation system if possible.
     */
    void hibernate();

}
