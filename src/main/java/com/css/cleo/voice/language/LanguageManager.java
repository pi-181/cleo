package com.css.cleo.voice.language;

import com.css.cleo.voice.recognize.LiveVoiceRecognizer;
import com.css.cleo.voice.recognize.VoiceRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import main.java.com.goxr3plus.jsfggrammarparser.parser.JSGFGrammarParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LanguageManager {
    private final File languageDirectory;

    private final Configuration defaultConfig;
    private final BiConsumer<VoiceRecognizer, SpeechResult> resultCallback;

    private final LanguageConfiguration languageConfiguration;
    private final LanguageExplorer languageExplorer;
    private final LanguageDownloader languageDownloader;
    private LiveVoiceRecognizer voiceRecognizer;
    private List<String> allRules;

    public LanguageManager(File languageDirectory,
                           Configuration defaultConfig,
                           BiConsumer<VoiceRecognizer, SpeechResult> resultCallback) {
        this.languageDirectory = languageDirectory;
        this.defaultConfig = defaultConfig;
        this.resultCallback = resultCallback;

        languageDirectory.mkdirs();

        this.languageConfiguration = new LanguageConfiguration(new File(languageDirectory, "lang.properties"));
        languageConfiguration.load();

        this.languageExplorer = new LanguageExplorer(languageDirectory, languageConfiguration);
        this.languageDownloader = new LanguageDownloader(languageExplorer);

        setLanguage(languageConfiguration.getLanguage());
    }

    public void setLanguage(Language language) {
        this.languageConfiguration.setLanguage(language);

        final String outPrefix = "file:/";
        defaultConfig.setAcousticModelPath(outPrefix + languageExplorer.getAcousticModelDir().getAbsolutePath());
        defaultConfig.setDictionaryPath(outPrefix + languageExplorer.getDictionary().getAbsolutePath());
        defaultConfig.setGrammarPath(outPrefix + languageExplorer.getGrammarsDir().getAbsolutePath());

        allRules = JSGFGrammarParser.getAllGrammarRules(languageExplorer.getRootGrammarFile(), true);
    }

    @NotNull
    public Language getLanguage() {
        return this.languageConfiguration.getLanguage();
    }

    public List<String> getRules(String... text) {
        try {
            List<String> list = JSGFGrammarParser.getRulesContainingWords(
                    new FileInputStream(languageExplorer.getRootGrammarFile()),
                    Arrays.asList(text),
                    true
            );

            if (list.size() == allRules.size())
                return Collections.emptyList();

            return list;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public LanguageExplorer getLanguageExplorer() {
        return languageExplorer;
    }

    public File getLanguageDirectory() {
        return languageDirectory;
    }

    public LanguageDownloader getLanguageDownloader() {
        return languageDownloader;
    }

    public void update(Consumer<String> currentState,
                       Consumer<Double> progressConsumer,
                       Consumer<Exception> exceptionConsumer,
                       Consumer<Exception> voiceExceptionConsumer,
                       Runnable onDone) {
        Runnable init = () -> {
            try {
                LanguageManager.this.voiceRecognizer = new LiveVoiceRecognizer(defaultConfig, resultCallback);
            } catch (IOException | IllegalArgumentException e) {
                voiceExceptionConsumer.accept(e);
                return;
            }
            onDone.run();
        };

        if (!languageExplorer.isReady())
            languageDownloader.downloadLanguage(getLanguage(), currentState, progressConsumer, exceptionConsumer, () -> {
                init.run();
                onDone.run();
            });
        else init.run();
    }

    public LiveVoiceRecognizer getVoiceRecognizer() {
        return voiceRecognizer;
    }
}
