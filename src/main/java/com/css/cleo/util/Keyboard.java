package com.css.cleo.util;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class Keyboard {
    public static void init() {
        try {
            final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    public static void addDoubleReleasesHandler(int timeBetweenPress, int button, Runnable handler) {
        final NativeKeyListener nativeKeyListener = new NativeKeyListener() {
            private long last = 0;
            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() != button)
                    return;

                if (System.currentTimeMillis() - last < timeBetweenPress) {
                    handler.run();
                    last = 0;
                } else {
                    last = System.currentTimeMillis();
                }
            }
        };
        GlobalScreen.addNativeKeyListener(nativeKeyListener);
    }

}
