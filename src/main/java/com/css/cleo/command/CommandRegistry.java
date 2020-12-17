package com.css.cleo.command;

import com.css.cleo.command.exact.*;
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
        nameToCommandMap.put("open_notepad", new NotepadCommand());
        nameToCommandMap.put("open_gmail", new OpenGmailCommand());
        nameToCommandMap.put("show_time", new ShowTimeCommand());
        nameToCommandMap.put("close_program", new CloseProgramCommand());
        nameToCommandMap.put("close", new CloseOverlayCommand());
    }

    public Optional<Command> getCommand(@NotNull String name) {
        return Optional.ofNullable(nameToCommandMap.get(name));
    }
}
