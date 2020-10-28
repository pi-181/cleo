package com.css.cleo;

import com.css.cleo.command.CommandDispatcher;
import com.css.cleo.os.FeatureFactory;
import com.css.cleo.os.OsFeature;
import com.css.cleo.ui.CleoTrayIcon;
import com.css.cleo.ui.window.VoiceView;
import com.css.cleo.util.Keyboard;
import com.css.cleo.voice.recognize.LiveVoiceRecognizer;
import com.css.cleo.voice.recognize.StreamVoiceRecognizer;
import com.css.cleo.voice.recognize.VoiceRecognizer;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;
import java.io.IOException;

public class CleoApplication {
    private final VoiceView voiceView = new VoiceView();
    private final OsFeature osFeature = FeatureFactory.getNativeFeature().orElseThrow();
    private final CleoTrayIcon tray = new CleoTrayIcon("/assets/icon/cleo-x256.png");
    private final CommandDispatcher commandDispatcher = new CommandDispatcher(osFeature);
    private final LiveVoiceRecognizer simpleVoiceRecognizer;

    public CleoApplication() throws IOException {
        final Configuration config = new Configuration();
        final String resAudioRec = "resource:/audio_recognition";
        config.setSampleRate(8000);
        config.setAcousticModelPath(resAudioRec + "/hmm/ru3");
        config.setDictionaryPath(resAudioRec + "/dictionary/ru.dic");
        config.setGrammarPath(resAudioRec + "/lang/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

        try {
            simpleVoiceRecognizer = new LiveVoiceRecognizer(config, this::onRecognize);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,
                    "Can't detect microphone device!", "No input device", JOptionPane.ERROR_MESSAGE);
            throw new Error("No microphone detected");
        }
    }

    public void start() {
        Keyboard.addDoubleReleasesHandler(500, NativeKeyEvent.VC_ALT, () -> voiceView.setVisible(true));

        tray.addDoubleClickListener(e -> voiceView.setVisible(true));
        tray.addMenuItem("Record", e -> voiceView.setVisible(true));
        tray.addMenuItem("Settings", e -> System.out.println("not done yet"));
        tray.addMenuItem("Exit", e -> System.exit(0));

        voiceView.setOnShow(() -> simpleVoiceRecognizer.setEnabled(true));
        voiceView.setOnHide(() -> simpleVoiceRecognizer.setEnabled(false));
    }

    private void onRecognize(VoiceRecognizer recognizer, SpeechResult result) {
        voiceView.setSpeechResult(result);
        commandDispatcher.dispatch(result);
    }

}
