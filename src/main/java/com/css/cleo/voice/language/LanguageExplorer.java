package com.css.cleo.voice.language;

import java.io.File;

public class LanguageExplorer {
    private final File languageDirectory;
    private final LanguageConfiguration languageConfiguration;

    public LanguageExplorer(File languageDirectory, LanguageConfiguration languageConfiguration) {
        this.languageDirectory = languageDirectory;
        this.languageConfiguration = languageConfiguration;
    }

    public boolean isReady() {
        File acousticModelDir = getAcousticModelDir();
        return getDictionary().exists()
                && getGrammarsDir().exists()
                && acousticModelDir.exists()
                && acousticModelDir.listFiles().length != 0;
    }

    public File getDictionary() {
        return getFile("dictionary.dict");
    }

    public File getAcousticModelDir() {
        return getFile("hmm");
    }

    public File getGrammarsDir() {
        return getFile("grammars");
    }

    private File getFile(String fileName) {
        Language language = languageConfiguration.getLanguage();
        return new File(new File(languageDirectory, language.getName()), fileName);
    }

}
