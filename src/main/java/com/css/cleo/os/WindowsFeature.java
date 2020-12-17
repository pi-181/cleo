package com.css.cleo.os;

import com.css.cleo.util.FileUtil;
import com.css.cleo.util.GuiUtil;
import mslinks.ShellLink;
import mslinks.ShellLinkException;
import org.jetbrains.annotations.NotNull;

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
    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown /r");
        } catch (IOException e) {
            GuiUtil.showError(e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        try {
            Runtime.getRuntime().exec("shutdown /s");
        } catch (IOException e) {
            GuiUtil.showError(e.getMessage());
        }
    }

    @Override
    public void hibernate() {
        try {
            Runtime.getRuntime().exec("shutdown /h");
        } catch (IOException e) {
            GuiUtil.showError(e.getMessage());
        }
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
