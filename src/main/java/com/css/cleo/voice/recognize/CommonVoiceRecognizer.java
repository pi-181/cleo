package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * Abstraction that hides framework
 * specific and adds some external functionality.
 *
 * @param <T> Voice Recognizer
 */
public abstract class CommonVoiceRecognizer<T extends AbstractSpeechRecognizer> implements VoiceRecognizer {
    protected final ReentrantLock lock = new ReentrantLock();
    protected final Condition initCondition = lock.newCondition();
    protected final Condition enableCondition = lock.newCondition();
    protected final Condition doneCondition = lock.newCondition();
    protected final T speechRecognizer;
    protected BiConsumer<VoiceRecognizer, SpeechResult> resultCallback;

    protected boolean initialized = false;
    protected boolean enabled = false;
    protected boolean destroy = false;

    /**
     * Constructs VoiceRecognizer object.
     *
     * @param resultCallback   function interface that will handle result of recognizing
     * @param speechRecognizer object that will recognize voice
     */
    public CommonVoiceRecognizer(BiConsumer<VoiceRecognizer, SpeechResult> resultCallback,
                                 T speechRecognizer) {
        this.resultCallback = resultCallback;
        this.speechRecognizer = speechRecognizer;
        setup();
    }

    protected void setup() {
        final Thread thread = new Thread(() -> {
            final CommonVoiceRecognizer<T> recognizer = CommonVoiceRecognizer.this;
            while (!recognizer.destroy) {
                lock.lock();

                final boolean wasInitialized = recognizer.initialized;
                if (!wasInitialized) {
                    recognizer.initialized = true;
                    initCondition.signalAll();
                }

                if (!recognizer.enabled) {
                    if (wasInitialized)
                        doneCondition.signalAll();
                    enableCondition.awaitUninterruptibly();
                    if (recognizer.destroy) {
                        lock.unlock();
                        return;
                    }
                }
                lock.unlock();

                SpeechResult result;
                while (recognizer.enabled && (result = speechRecognizer.getResult()) != null)
                    resultCallback.accept(this, result);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Changes state of VoiceRecognizer.
     *
     * @param enabled true should start listening, to stop if will false
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (!initialized) {
            lock.lock();
            initCondition.awaitUninterruptibly();
            lock.unlock();
        }

        if (this.enabled == enabled)
            return;

        if (destroy && enabled)
            throw new IllegalStateException("You can't change state of destroyed recognizer.");

        this.enabled = enabled;
        if (enabled)
            startRecognition();
        else
            stopRecognition();

        lock.lock();
        enableCondition.signalAll();
        lock.unlock();
    }

    /**
     * @return current state of the recognizer
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return current handler of result
     */
    @Override
    public BiConsumer<VoiceRecognizer, SpeechResult> getResultCallback() {
        return resultCallback;
    }

    /**
     * Changes handler of voice recognizing result.
     *
     * @param resultCallback callback that will handle results
     */
    @Override
    public void setResultCallback(BiConsumer<VoiceRecognizer, SpeechResult> resultCallback) {
        this.resultCallback = resultCallback;
    }

    /**
     * Should be called in the end of usage,
     * stops recognizing end free memory if need.
     */
    @Override
    public void destroy() {
        setEnabled(false);
        this.destroy = true;
    }

    /**
     * @return current status of recognizer, is destroyed or not
     */
    @Override
    public boolean isDestroyed() {
        return destroy;
    }

    /**
     * Uninterruptibly blocks current thread until
     * recognizing voice will not be finished.
     */
    @Override
    public void waitForDoneUninterruptibly() {
        lock.lock();
        if (!enabled) {
            lock.unlock();
            return;
        }

        doneCondition.awaitUninterruptibly();
        lock.unlock();
    }

    /**
     * Blocks current thread until
     * recognizing voice will not be finished.
     *
     * @throws InterruptedException when interrupted
     */
    @Override
    public void waitForDone() throws InterruptedException {
        lock.lock();
        if (!enabled) {
            lock.unlock();
            return;
        }

        doneCondition.await();
        lock.unlock();
    }

    /**
     * Methods that starts voice recognizing
     */
    protected abstract void startRecognition();

    /**
     * Methods that stops voice recognizing
     */
    protected abstract void stopRecognition();

}
