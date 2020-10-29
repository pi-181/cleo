package com.css.cleo;

import com.css.cleo.command.CommandDispatcher;
import com.css.cleo.os.FeatureFactory;
import com.css.cleo.os.OsFeature;
import com.css.cleo.ui.CleoTrayIcon;
import com.css.cleo.ui.window.VoiceView;
import com.css.cleo.util.Keyboard;
import com.css.cleo.voice.language.LanguageManager;
import com.css.cleo.voice.recognize.VoiceRecognizer;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;
import java.io.File;
import java.util.function.Consumer;

public class CleoApplication {
    private final File dataRoot = new File(System.getenv("APPDATA"), "Cleo");

    private final VoiceView voiceView = new VoiceView();
    private final OsFeature osFeature = FeatureFactory.getNativeFeature().orElseThrow();
    private final CleoTrayIcon tray = new CleoTrayIcon("/assets/icon/cleo-x256.png");
    private final CommandDispatcher commandDispatcher = new CommandDispatcher(osFeature);
    private final LanguageManager languageManager;

    public CleoApplication() {
        final Configuration config = new Configuration();

        languageManager = new LanguageManager(new File(dataRoot, "languages"), config, this::onRecognize);
        config.setSampleRate(8000);
        config.setUseGrammar(true);
        config.setGrammarName("text");

        languageManager.update(
                System.out::println,
                new Consumer<>() {
                    private int last;

                    @Override
                    public void accept(Double aDouble) {
                        int v = (int) (aDouble * 100);
                        if (last - v > 70)
                            last = v;

                        if (v <= last)
                            return;
                        System.out.println(v);
                        last = v;
                    }
                },
                Exception::printStackTrace,
                this::onVoiceException,
                () -> System.out.println("done")
        );
    }

    public void onVoiceException(Exception e) {
        JOptionPane.showMessageDialog(null,
                "Can't detect microphone device!", "No input device", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
        throw new Error("No microphone detected");
    }

    public void start() {
        Keyboard.addDoubleReleasesHandler(500, NativeKeyEvent.VC_ALT, () -> voiceView.setVisible(true));

        tray.addDoubleClickListener(e -> voiceView.setVisible(true));
        tray.addMenuItem("Record", e -> voiceView.setVisible(true));
        tray.addMenuItem("Settings", e -> System.out.println("not done yet"));
        tray.addMenuItem("Exit", e -> System.exit(0));

        voiceView.setOnShow(() -> languageManager.getVoiceRecognizer().setEnabled(true));
        voiceView.setOnHide(() -> languageManager.getVoiceRecognizer().setEnabled(false));
    }

    private void onRecognize(VoiceRecognizer recognizer, SpeechResult result) {
        voiceView.setSpeechResult(result);
        commandDispatcher.dispatch(result);
    }

}
