package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

public class StreamVoiceRecognizer extends CommonVoiceRecognizer<StreamSpeechRecognizer> {
    private TimeFrame timeFrame = TimeFrame.INFINITE;
    private InputStream inputStream;

    public StreamVoiceRecognizer(Configuration config,
                                 InputStream inputStream,
                                 BiConsumer<VoiceRecognizer, SpeechResult> resultCallback) throws IOException {
        super(resultCallback, new StreamSpeechRecognizer(config));
        this.inputStream = inputStream;
    }

    @Override
    protected void startRecognition() {
        super.speechRecognizer.startRecognition(inputStream);
    }

    @Override
    protected void stopRecognition() {
        super.speechRecognizer.stopRecognition();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }
}
