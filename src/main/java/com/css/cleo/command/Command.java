package com.css.cleo.command;

import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.SpeechResult;

public interface Command {
    void execute(OsFeature osFeature, SpeechResult result);
}
