package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.SpeechResult;

import java.util.function.BiConsumer;

public interface VoiceRecognizer {

    /**
     * Changes state of voice recorder, if
     * value is same that returns {@link VoiceRecognizer#isEnabled}
     * nothing will be changed.
     *
     * @param enabled is true if currently listening
     */
    void setEnabled(boolean enabled);

    /**
     * Returns current state of VoiceRecognizer.
     *
     * @return true in case where recognizer is working and listening user.
     */
    boolean isEnabled();

    /**
     * Returns current callback in recognizer.
     *
     * @return functional interface that handles current result
     */
    BiConsumer<VoiceRecognizer, SpeechResult> getResultCallback();

    /**
     * Changes current callback of recognizer.
     *
     * @param resultCallback callback that will handle results
     */
    void setResultCallback(BiConsumer<VoiceRecognizer, SpeechResult> resultCallback);

    /**
     * Destroys recognizer freeing memory and stopping thread,
     * after destroy recognizer can't be used more.
     */
    void destroy();

    /**
     * Checks for destroyed recognizer or not.
     */
    boolean isDestroyed();

    /**
     * Waits uninterruptibly for time of working recognizer.
     */
    void waitForDoneUninterruptibly();

    /**
     * Waits for time of working recognizer.
     */
    void waitForDone() throws InterruptedException;

}
