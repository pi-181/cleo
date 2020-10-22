package com.css.cleo;

import com.css.cleo.util.Keyboard;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Keyboard.init();
        new CleoApplication().start();
    }
}
