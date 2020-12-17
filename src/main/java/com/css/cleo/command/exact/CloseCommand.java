package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.SpeechResult;

public class CloseCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        // nothing to do... it will closed automatically
    }
}
