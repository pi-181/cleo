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
        try {
            File temp = Files.createTempDirectory("cleo-downloader").toFile();

            var dictionaryZip = new File(temp, "dictionary.zip");
            File dictionary = languageExplorer.getDictionary();
            if (!dictionary.exists()) {
                currentState.accept("Downloading dictionary...");
                setup(language.getDictionaryLink(), dictionaryZip, dictionary.getParentFile(), progressConsumer);
            }

            var grammarZip = new File(temp, "grammar.zip");
            File grammarsDir = languageExplorer.getGrammarsDir();
            if (!grammarsDir.exists()) {
                currentState.accept("Downloading grammar model...");
                setup(language.getGrammarLink(), grammarZip, grammarsDir, progressConsumer);
            }

            var acousticModelZip = new File(temp, "acousticModel.zip");
            File acousticModelDir = languageExplorer.getAcousticModelDir();
            if (!acousticModelDir.exists()) {
                currentState.accept("Downloading acoustic model...");
                setup(language.getAcousticModelLink(), acousticModelZip, acousticModelDir, progressConsumer);
            }
            onDone.run();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
        }
    }

    private void setup(String url, File zipLoc, File unzipDir, Consumer<Double> progressConsumer) {
        new FileDownloader(url, zipLoc, progressConsumer).run();
        FileUtil.unzip(zipLoc, unzipDir);
    }

}
