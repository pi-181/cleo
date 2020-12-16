package com.css.cleo.command;

import com.css.cleo.command.exact.OpenBrowserCommand;
import com.css.cleo.command.exact.RebootCommand;
import com.css.cleo.command.exact.ShutdownCommand;
import com.css.cleo.command.exact.SleepCommand;
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
        nameToCommandMap.put("power_shutdown", new ShutdownCommand());
        nameToCommandMap.put("power_restart", new RebootCommand());
        nameToCommandMap.put("power_hibernate", new SleepCommand());
    }

    public Optional<Command> getCommand(@NotNull String name) {
        return Optional.ofNullable(nameToCommandMap.get(name));
    }
}
