package com.css.cleo.os;

import com.css.cleo.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LinuxFeature implements OsFeature {
    private static final Collection<Path> START_MENU_PATHS = List.of(
            Paths.get("usr", "share", "applications")
    );

    /**
     * Returns executable files that are
     * accessed from command line or terminal.
     *
     * @return not nullable executable files or symlinks list.
     */
    @Override
    public @NotNull Collection<File> getTools() {
        final String pathEnv = System.getenv("PATH");

        final List<Path> pathValues =
                Arrays.stream(pathEnv.split(Pattern.quote(":")))
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
     * @return not nullable executable files or desktop list.
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
        unsupported();
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVolume(int volume) {
        unsupported();
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMasterVolume() {
        unsupported();
        throw new UnsupportedOperationException();
    }

    private boolean isExecutable(File file) {
        try {
            final String s = Files.probeContentType(file.toPath());
            if (!s.startsWith("application/"))
                return false;

            return s.endsWith("x-executable")
                    || s.endsWith("x-elf")
                    || s.endsWith("x-sh");
        } catch (IOException e) {
            return false;
        }
    }

    private void unsupported() {
        JOptionPane.showMessageDialog(null, "This feature is not supported on Linux",
                "Unsupported", JOptionPane.ERROR_MESSAGE);
    }

}
