package com.gridpathfinder.domain;

import java.util.List;

public record ShortestPathResult(String algorithm, int steps, int visitedCells, List<Position> path) {
    public ShortestPathResult {
        path = List.copyOf(path);
    }
}
