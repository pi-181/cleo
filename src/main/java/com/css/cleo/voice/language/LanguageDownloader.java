package com.css.cleo.voice.language;

import com.css.cleo.util.FileDownloader;
import com.css.cleo.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

public class LanguageDownloader {
    private final LanguageExplorer languageExplorer;

    public LanguageDownloader(LanguageExplorer languageExplorer) {
        this.languageExplorer = languageExplorer;
    }

    public void downloadLanguage(Language language,
                                 Consumer<String> currentState,
                                 Consumer<Double> progressConsumer,
                                 Consumer<Exception> exceptionConsumer,
                                 Runnable onDone) {
        File temp;
        try {
            temp = Files.createTempDirectory("cleo-downloader").toFile();
        } catch (IOException e) {
            exceptionConsumer.accept(e);
            return;
        }

        System.out.println(temp.getAbsolutePath());

        var dictionaryZip = new File(temp, "dictionary.zip");
        var grammarZip = new File(temp, "grammar.zip");
        var acousticModelZip = new File(temp, "acousticModel.zip");

        currentState.accept("Downloading dictionary...");
        FileDownloader.downloadFile(language.getDictionaryLink(), dictionaryZip, progressConsumer, exceptionConsumer, out1 -> {
            currentState.accept("Downloading grammar...");
            FileDownloader.downloadFile(language.getGrammarLink(), grammarZip, progressConsumer, exceptionConsumer, out2 -> {
                currentState.accept("Downloading acoustic model...");
                FileDownloader.downloadFile(language.getAcousticModelLink(), acousticModelZip, progressConsumer, exceptionConsumer, out3 -> {
                    currentState.accept("Installing...");
                    install(dictionaryZip, grammarZip, acousticModelZip, onDone);
                });
            });
        });
    }

    private void install(File dictionaryZip, File grammarZip, File accentModelZip, Runnable onDone) {
        FileUtil.unzip(dictionaryZip, languageExplorer.getDictionary().getParentFile());
        FileUtil.unzip(grammarZip, languageExplorer.getGrammarsDir());
        FileUtil.unzip(accentModelZip, languageExplorer.getAcousticModelDir());
        onDone.run();
    }

}
