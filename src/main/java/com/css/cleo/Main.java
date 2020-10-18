package com.css.cleo;

import com.css.cleo.os.FeatureFactory;
import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Configuration config = new Configuration();
        final String resAudioRec = "resource:/audio_recognition";
        config.setSampleRate(8000);
        config.setAcousticModelPath(resAudioRec + "/hmm/ru3");
        config.setDictionaryPath(resAudioRec + "/dictionary/ru.dic");
        config.setGrammarPath(resAudioRec + "/lang/");
        config.setUseGrammar(true);
        config.setGrammarName("text");

        final StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(config);

        final File file = new File("L:/IdeaProjects/cleo/src/main/resources/audio_recognition/test/audio1.wav");
        final FileInputStream fileInputStream = new FileInputStream(file);
        recognizer.startRecognition(fileInputStream);

        SpeechResult result;
        while ((result = recognizer.getResult()) != null)
            System.out.format("Hypothesis: %s\n", result.getHypothesis());

        recognizer.stopRecognition();
    }
}
