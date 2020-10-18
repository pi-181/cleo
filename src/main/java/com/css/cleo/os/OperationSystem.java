package com.css.cleo.os;

import java.util.Locale;

public enum OperationSystem {
    MAC_OS("mac", "darwin"),
    WINDOWS("win"),
    LINUX("nux"),
    SOLARIS("sunos"),
    OTHER;

    private final String[] names;

    OperationSystem(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public static OperationSystem getOperationSystem() {
        final OperationSystem[] values = values();
        final String osName = System.getProperty("os.name", "generic")
                .toLowerCase(Locale.ENGLISH);

        for (OperationSystem value : values) {
            for (String name : value.names) {
                if (osName.contains(name)) return value;
            }
        }

        return OTHER;
    }
}
