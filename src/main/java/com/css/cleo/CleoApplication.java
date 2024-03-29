package com.css.cleo;

import com.css.cleo.command.CommandDispatcher;
import com.css.cleo.os.CommonFeatureFactory;
import com.css.cleo.os.FeatureFactory;
import com.css.cleo.ui.CleoTrayIcon;
import com.css.cleo.ui.window.VoiceView;
import com.css.cleo.util.GuiUtil;
import com.css.cleo.util.Keyboard;
import com.css.cleo.voice.language.LanguageManager;
import com.css.cleo.voice.recognize.VoiceRecognizer;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class CleoApplication {
    private final File dataRoot = new File(System.getenv("APPDATA"), "Cleo");

    private final VoiceView voiceView = new VoiceView();
    private final FeatureFactory featureFactory = new CommonFeatureFactory();
    private final CleoTrayIcon tray = new CleoTrayIcon("/assets/icon/cleo-x256.png");
    private final CommandDispatcher commandDispatcher;
    private final LanguageManager languageManager;

    public CleoApplication() {
        final Configuration config = new Configuration();
        config.setSampleRate(24000);
        config.setUseGrammar(true);
        config.setGrammarName("text");

        languageManager = new LanguageManager(new File(dataRoot, "languages"), config, this::onRecognize);
        commandDispatcher = new CommandDispatcher(featureFactory.getNativeFeature().orElseThrow(), languageManager);

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
        GuiUtil.showError("Can't detect microphone device!");
        System.exit(1);
        throw new Error("No microphone detected");
    }

    public void start() {
        Keyboard.addDoubleReleasesHandler(500, NativeKeyEvent.VC_ALT, () -> voiceView.restore(true));

        tray.addDoubleClickListener(e -> voiceView.restore(true));
        tray.addMenuItem("Record", e -> voiceView.restore(true));
        tray.addMenuItem("Settings", e -> System.out.println("not done yet"));
        tray.addMenuItem("Exit", e -> System.exit(0));

        voiceView.setOnShow(() -> languageManager.getVoiceRecognizer().setEnabled(true));
        voiceView.setOnHide(() -> languageManager.getVoiceRecognizer().setEnabled(false));
    }

    private void onRecognize(VoiceRecognizer recognizer, SpeechResult result) {
        System.out.println(result.getHypothesis() + " - " + result.getResult());
        voiceView.setSpeechResult(result);
        boolean success = commandDispatcher.dispatch(result);
        voiceView.setSuccess(success);
        if (!success)
            return;

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        voiceView.restore(false);
    }

}
