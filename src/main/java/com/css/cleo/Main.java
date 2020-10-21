package com.css.cleo;

import com.css.cleo.voice.recognize.StreamVoiceRecognizer;
import edu.cmu.sphinx.api.Configuration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        setTrayIcon();

        final Configuration config = new Configuration();
        final String resAudioRec = "resource:/audio_recognition";
        config.setSampleRate(8000);
        config.setAcousticModelPath(resAudioRec + "/hmm/ru3");
        config.setDictionaryPath(resAudioRec + "/dictionary/ru.dic");
        config.setGrammarPath(resAudioRec + "/lang/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

        final Supplier<InputStream> supplier =
                () -> Main.class.getResourceAsStream("/audio_recognition/test/audio1.wav");

        final StreamVoiceRecognizer simpleVoiceRecognizer = new StreamVoiceRecognizer(config, supplier.get(), (rec, res) -> {
            final String hypothesis = res.getHypothesis();
            System.out.format("Hypothesis: %s\n", hypothesis);
            if (hypothesis.contains("теленок"))
                rec.setEnabled(false);
        });

        simpleVoiceRecognizer.setEnabled(true);
        simpleVoiceRecognizer.waitForDone();
        simpleVoiceRecognizer.setInputStream(supplier.get());
        simpleVoiceRecognizer.setEnabled(true);
    }

    private static void setTrayIcon() {
        if (!SystemTray.isSupported())
            return;

        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(e -> System.exit(0));
        trayMenu.add(item);

        URL imageURL = Main.class.getResource("/assets/icon/cleo-x256.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        TrayIcon trayIcon = new TrayIcon(icon, "Cleo", trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage("Cleo", "Application started!", TrayIcon.MessageType.INFO);
    }
}
