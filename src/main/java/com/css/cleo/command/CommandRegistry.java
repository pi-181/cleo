package com.css.cleo.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry {
    private final Map<String, Command> nameToCommandMap = new HashMap<>();

    public CommandRegistry() {
        registerCommands();
    }

    protected void registerCommands() {

    }

    public Optional<Command> getCommand(@NotNull String name) {
        return Optional.ofNullable(nameToCommandMap.get(name));
    }
}
