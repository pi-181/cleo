package com.css.cleo.os;

import com.css.cleo.util.FileUtil;
import mslinks.ShellLink;
import mslinks.ShellLinkException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WindowsFeature implements OsFeature {
    private static final Collection<Path> START_MENU_PATHS = List.of(
            Paths.get(System.getenv("ProgramData"), "Microsoft", "Windows", "Start Menu", "Programs"),
            Paths.get(System.getenv("AppData"), "Microsoft", "Windows", "Start Menu", "Programs")
    );

    /**
     * Returns executable files that are
     * accessed from command line or terminal.
     *
     * @return not nullable executable files or shortcut list.
     */
    @Override
    public @NotNull Collection<File> getTools() {
        final String pathEnv = System.getenv("PATH");

        final List<Path> pathValues =
                Arrays.stream(pathEnv.split(Pattern.quote(";")))
                        .map(Paths::get)
                        .collect(Collectors.toList());

        Set<File> tools = new HashSet<>();
        for (Path path : pathValues) {
            tools.addAll(FileUtil.collectExecutables(path, this::isExecutable));
        }

        return tools;
    }

    /**
     * Returns executable files that are
     * desktop applications.
     *
     * @return not nullable executable files or shortcut list.
     */
    @Override
    public @NotNull Collection<File> getDesktopApps() {
        final Set<File> apps = new HashSet<>();

        for (Path path : START_MENU_PATHS)
            apps.addAll(FileUtil.collectRecursively(path));

        return apps;
    }

    @Override
    public void muteVolume(boolean mute) {
        try {
            Runtime.getRuntime().exec("cmd mutesysvolume " + (mute ? 1 : 0));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public int getMasterVolume() {
        return 0;
    }

    private boolean isExecutable(File file) {
        if (!file.canExecute())
            return false;

        final String name = file.getName();
        if (name.endsWith(".exe"))
            return true;

        if (name.endsWith(".lnk")) {
            try {
                final ShellLink shellLink = new ShellLink(file);
                final String target = shellLink.resolveTarget();
                return target.endsWith(".exe") && new File(target).canExecute();
            } catch (IOException | ShellLinkException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

}
