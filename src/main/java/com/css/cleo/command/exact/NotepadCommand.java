package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import com.css.cleo.util.GuiUtil;
import edu.cmu.sphinx.api.SpeechResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NotepadCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        try {
            Desktop.getDesktop().edit(File.createTempFile("notepad", ".txt"));
        } catch (IOException e) {
            GuiUtil.showError(e.getMessage());
            e.printStackTrace();
        }
    }
}
