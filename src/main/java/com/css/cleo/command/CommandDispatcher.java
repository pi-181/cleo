package com.css.cleo.command;

import com.css.cleo.os.OsFeature;
import com.css.cleo.voice.language.LanguageManager;
import edu.cmu.sphinx.api.SpeechResult;

public class CommandDispatcher {
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final OsFeature osFeature;
    private final LanguageManager languageManager;

    public CommandDispatcher(OsFeature osFeature, LanguageManager languageManager) {
        this.osFeature = osFeature;
        this.languageManager = languageManager;
    }

    public boolean dispatch(SpeechResult result) {
        boolean[] handled = new boolean[] { false };
        languageManager.getRules(result.getHypothesis())
                .forEach(r -> commandRegistry.getCommand(r).ifPresent(c -> {
                    c.execute(osFeature, result);
                    handled[0] = true;
                }));
        return handled[0];
    }

}
