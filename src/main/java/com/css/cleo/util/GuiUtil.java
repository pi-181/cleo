package com.css.cleo.util;

import javax.swing.*;

public class GuiUtil {
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message,
                "An error occurred", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(null, message) == 0;
    }
}
