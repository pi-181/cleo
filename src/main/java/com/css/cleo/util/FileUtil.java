package com.css.cleo.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class FileUtil {

    @NotNull
    public static Collection<File> collectRecursively(@NotNull final Path root) {
        try {
            return Files.walk(root, FileVisitOption.FOLLOW_LINKS)
                    .map(Path::toFile)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    public static Collection<File> collectExecutables(@NotNull final Path dir, @NotNull final Predicate<File> filter) {
        final File directory = dir.toFile();
        if (!directory.isDirectory())
            return Collections.emptyList();

        return Arrays.stream(directory.listFiles())
                .filter(filter)
                .collect(Collectors.toList());
    }

}
