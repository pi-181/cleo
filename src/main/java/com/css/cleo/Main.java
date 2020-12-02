package com.css.cleo;

import com.css.cleo.util.Keyboard;

public class Main {
    public static void main(String[] args) {
        Keyboard.init();
        new CleoApplication().start();
    }
}
