package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

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
            throw new IllegalStateException("You can't state of destroyed recognizer.");

        this.enabled = enabled;
        if (enabled)
            startRecognition();
        else
            stopRecognition();

        lock.lock();
        enableCondition.signalAll();
        lock.unlock();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public BiConsumer<VoiceRecognizer, SpeechResult> getResultCallback() {
        return resultCallback;
    }

    @Override
    public void setResultCallback(BiConsumer<VoiceRecognizer, SpeechResult> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    public void destroy() {
        setEnabled(false);
        this.destroy = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroy;
    }

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

    protected abstract void startRecognition();

    protected abstract void stopRecognition();

}
