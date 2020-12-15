package com.css.cleo.os;

import java.util.Optional;

public interface FeatureFactory {
    Optional<OsFeature> getNativeFeature();
}
