package com.css.cleo.command;

import com.css.cleo.os.OsFeature;
import edu.cmu.sphinx.api.SpeechResult;

public class CommandDispatcher {
    private final OsFeature osFeature;

    public CommandDispatcher(OsFeature osFeature) {
        this.osFeature = osFeature;
    }

    public boolean dispatch(SpeechResult result) {
        return false;
    }

}
