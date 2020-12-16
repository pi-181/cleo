package com.css.cleo.command;

import com.css.cleo.os.OsFeature;
import com.css.cleo.voice.language.LanguageManager;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandDispatcher {
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final OsFeature osFeature;
    private final LanguageManager languageManager;

    public CommandDispatcher(OsFeature osFeature, LanguageManager languageManager) {
        this.osFeature = osFeature;
        this.languageManager = languageManager;
    }

    public boolean dispatch(SpeechResult result) {
        String hypothesis = result.getHypothesis();
        List<Command> commands = languageManager.getRules(hypothesis).stream()
                .map(commandRegistry::getCommand)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (commands.isEmpty() || commands.size() > 1)
            return false;

        commands.get(0).execute(osFeature, result);
        return true;
    }

}
