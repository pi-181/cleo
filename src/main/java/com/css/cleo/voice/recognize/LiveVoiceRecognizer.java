package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.IOException;
import java.util.function.BiConsumer;

public class LiveVoiceRecognizer extends CommonVoiceRecognizer<LiveSpeechRecognizer> {

    public LiveVoiceRecognizer(Configuration config,
                               BiConsumer<VoiceRecognizer, SpeechResult> resultCallback) throws IOException {
        super(resultCallback, new LiveSpeechRecognizer(config));
    }

    @Override
    protected void startRecognition() {
        super.speechRecognizer.startRecognition(true);
    }

    @Override
    protected void stopRecognition() {
        super.speechRecognizer.stopRecognition();
    }
}
