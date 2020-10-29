package com.css.cleo.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static void unzip(@NotNull final File inputZip, @NotNull final File outputDir) {
        final String location = outputDir.getAbsolutePath();
        checkDir(location, "");

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(inputZip))) {
            ZipEntry ze;

            while ((ze = zin.getNextEntry()) != null) {
                System.out.println("Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    checkDir(location, ze.getName());
                    continue;
                }

                FileOutputStream fos = new FileOutputStream(location + File.separatorChar + ze.getName());
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                byte[] buffer = new byte[1024];
                int read;

                while ((read = zin.read(buffer)) != -1) {
                    bos.write(buffer, 0, read);
                }

                bos.close();
                zin.closeEntry();
                fos.close();
            }
        } catch (Exception e) {
            System.out.println("Unzipping failed...");
            e.printStackTrace();
        }
    }

    public static void checkDir(@NotNull final String location, @NotNull final String dir) {
        File file = new File(location + File.separatorChar + dir);
        if (!file.isDirectory())
            file.mkdirs();
    }

}
