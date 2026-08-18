package com.gridpathfinder.domain;

import java.util.List;

public record PathResult(int startingColumn, long totalScore, List<Cell> path) {
    public PathResult {
        path = List.copyOf(path);
    }
}
