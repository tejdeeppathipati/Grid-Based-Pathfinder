package com.gridpathfinder.domain;

import java.util.List;

public record PathSearchResult(
        Algorithm algorithm,
        int steps,
        long totalCost,
        int visitedCells,
        long durationNanos,
        List<Position> path
) {
    public PathSearchResult {
        path = List.copyOf(path);
    }
}
