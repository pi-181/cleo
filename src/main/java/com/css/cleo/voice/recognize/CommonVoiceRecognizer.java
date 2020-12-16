package com.css.cleo.voice.recognize;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.recognizer.Recognizer;

import java.lang.reflect.Field;
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
    private final Recognizer recognizerInternal;

    protected BiConsumer<VoiceRecognizer, SpeechResult> resultCallback;
    protected boolean initialized = false;
    protected boolean enabled = false;
    protected boolean destroy = false;

    protected boolean handling = false;
    protected Thread handlingThread;

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
        try {
            Field recognizer = AbstractSpeechRecognizer.class.getDeclaredField("recognizer");
            recognizer.setAccessible(true);
            this.recognizerInternal = (Recognizer) recognizer.get(speechRecognizer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setup();
    }

    protected void setup() {
        handlingThread = new Thread(() -> {
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
                recognizer.handling = true;
                while (recognizer.enabled && (result = speechRecognizer.getResult()) != null) {
                    try {
                        if (!enabled)
                            break;
                        resultCallback.accept(this, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                recognizer.handling = false;
            }
        });
        handlingThread.setDaemon(true);
        handlingThread.start();
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

        if (enabled) {
            busyWaitUntil(Recognizer.State.DEALLOCATED);
            startRecognition();
            busyWaitUntil(Recognizer.State.READY);
        } else {
            busyWaitUntil(Recognizer.State.READY);
            stopRecognition();
            busyWaitUntil(Recognizer.State.DEALLOCATED);
        }

        lock.lock();
        enableCondition.signalAll();
        lock.unlock();
    }

    private void busyWaitUntil(Recognizer.State until) {
        Recognizer.State state;
        while ((state = recognizerInternal.getState()) != until) {
            if (state == Recognizer.State.ERROR)
                throw new RuntimeException("Recognizer errored");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
