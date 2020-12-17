package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.SpeechResult;

public class ShowTimeCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        osFeature.executeBrowser("https://time.is/");
    }
}
