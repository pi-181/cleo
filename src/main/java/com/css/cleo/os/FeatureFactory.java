package com.css.cleo.os;

import java.util.Optional;

public final class FeatureFactory {

    public Optional<OsFeature> getNativeFeature() {
        return switch (OperationSystem.getOperationSystem()) {
            case WINDOWS -> Optional.of(new WindowsFeature());
            case LINUX -> Optional.of(new LinuxFeature());
            default -> Optional.empty();
        };
    }

}
