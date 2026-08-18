package com.gridpathfinder.api;

import com.gridpathfinder.domain.Algorithm;
import com.gridpathfinder.domain.Position;
import jakarta.validation.constraints.NotNull;

public record SavedGridSearchRequest(@NotNull Position start, @NotNull Position destination,
                                     @NotNull Algorithm algorithm) {
}
