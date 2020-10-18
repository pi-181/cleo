package com.css.cleo.os;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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
}
