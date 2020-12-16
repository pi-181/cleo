package com.css.cleo.command;

import com.css.cleo.command.exact.OpenBrowserCommand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry {
    private final Map<String, Command> nameToCommandMap = new HashMap<>();

    public CommandRegistry() {
        registerCommands();
    }

    protected void registerCommands() {
        nameToCommandMap.put("open_browser", new OpenBrowserCommand());
    }

    public Optional<Command> getCommand(@NotNull String name) {
        return Optional.ofNullable(nameToCommandMap.get(name));
    }
}
