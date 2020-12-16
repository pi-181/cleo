package com.css.cleo.command;

import com.css.cleo.os.OsFeature;
import com.css.cleo.voice.language.LanguageManager;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.Optional;

public class CommandDispatcher {
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final OsFeature osFeature;
    private final LanguageManager languageManager;

    public CommandDispatcher(OsFeature osFeature, LanguageManager languageManager) {
        this.osFeature = osFeature;
        this.languageManager = languageManager;
    }

    public boolean dispatch(SpeechResult result) {
        for (String rule : languageManager.getRules(result.getHypothesis())) {
            Optional<Command> command = commandRegistry.getCommand(rule);
            if (command.isEmpty())
                continue;

            command.get().execute(osFeature, result);
            return true;
        }

        return false;
    }

}
