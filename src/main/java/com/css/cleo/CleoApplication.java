package com.css.cleo;

import com.css.cleo.os.FeatureFactory;
import com.css.cleo.os.OsFeature;
import com.css.cleo.ui.CleoTrayIcon;
import com.css.cleo.ui.window.VoiceView;
import com.css.cleo.util.Keyboard;
import com.css.cleo.voice.recognize.StreamVoiceRecognizer;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import edu.cmu.sphinx.api.Configuration;

import java.io.IOException;

public class CleoApplication {
    private final VoiceView voiceView = new VoiceView();
    private final OsFeature osFeature = FeatureFactory.getNativeFeature().orElseThrow();
    private final CleoTrayIcon tray = new CleoTrayIcon("/assets/icon/cleo-x256.png");
    private final StreamVoiceRecognizer simpleVoiceRecognizer;

    public CleoApplication() throws IOException {
        Keyboard.addDoubleReleasesHandler(500, NativeKeyEvent.VC_ALT, () -> voiceView.setVisible(true));

        tray.addDoubleClickListener(e -> voiceView.setVisible(true));
        tray.addMenuItem("Record", e -> voiceView.setVisible(true));
        tray.addMenuItem("Settings", e -> System.out.println("not done yet"));
        tray.addMenuItem("Exit", e -> System.exit(0));

        final Configuration config = new Configuration();
        final String resAudioRec = "resource:/audio_recognition";
        config.setSampleRate(8000);
        config.setAcousticModelPath(resAudioRec + "/hmm/ru3");
        config.setDictionaryPath(resAudioRec + "/dictionary/ru.dic");
        config.setGrammarPath(resAudioRec + "/lang/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

         simpleVoiceRecognizer = new StreamVoiceRecognizer(config,
                 Main.class.getResourceAsStream("/audio_recognition/test/audio1.wav"),
                 (rec, res) -> {
            final String hypothesis = res.getHypothesis();
            System.out.format("Hypothesis: %s\n", hypothesis);
            if (hypothesis.contains("теленок"))
                rec.setEnabled(false);
        });
    }

    public void start() {
        simpleVoiceRecognizer.setEnabled(true);
        simpleVoiceRecognizer.waitForDoneUninterruptibly();
        simpleVoiceRecognizer.setInputStream(Main.class.getResourceAsStream("/audio_recognition/test/audio1.wav"));
        simpleVoiceRecognizer.setEnabled(true);
    }

}
