package com.css.cleo.voice.language;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LanguageConfiguration {
    private static final String LANG_KEY = "lang";

    private final Properties properties = new Properties();
    private final File configFile;

    private Language language;

    public LanguageConfiguration(File configFile) {
        this.configFile = configFile;
        load();
    }

    public void load() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            language = parseLanguage();
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (!configFile.exists())
            configFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.setProperty(LANG_KEY, language.name().toUpperCase());
            properties.store(fos, "This properties file describes current language settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private Language parseLanguage() {
        String lang = properties.getProperty(LANG_KEY);
        if (lang == null)
            lang = "unknown";

        Language language;

        try {
            language = Language.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e) {
            language = Language.defaultLanguage();
        }

        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

}
