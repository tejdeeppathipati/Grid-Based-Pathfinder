package com.gridpathfinder.service;

import com.gridpathfinder.domain.Position;

import java.util.List;

final class GridValidator {
    private GridValidator() {
    }

    static void validate(List<List<Integer>> grid) {
        if (grid == null || grid.isEmpty()) {
            throw new IllegalArgumentException("Grid must contain at least one row");
        }
        if (grid.getFirst() == null || grid.getFirst().isEmpty()) {
            throw new IllegalArgumentException("Grid rows must contain at least one cell");
        }
        int width = grid.getFirst().size();
        for (int row = 0; row < grid.size(); row++) {
            if (grid.get(row) == null || grid.get(row).size() != width) {
                throw new IllegalArgumentException("Grid must be rectangular; invalid row: " + row);
            }
        }
    }

    static void validateTraversablePosition(List<List<Integer>> grid, Position position, String name) {
        if (position.row() < 0 || position.row() >= grid.size()
                || position.column() < 0 || position.column() >= grid.getFirst().size()) {
            throw new IllegalArgumentException(name + " is outside the grid");
        }
        if (grid.get(position.row()).get(position.column()) == null) {
            throw new IllegalArgumentException(name + " must be a traversable cell");
        }
    }
}
