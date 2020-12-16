package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;

public class SleepCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        if (JOptionPane.showConfirmDialog(null, "Are u sure about sleep OS?") == 0)
            osFeature.hibernate();
    }
}
