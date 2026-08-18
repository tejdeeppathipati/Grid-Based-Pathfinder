package com.gridpathfinder.service;

import com.gridpathfinder.domain.Cell;
import com.gridpathfinder.domain.NoPathException;
import com.gridpathfinder.domain.PathResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathfinderService {
    private static final long UNREACHABLE = Long.MIN_VALUE;

    /**
     * Finds the maximum-score top-to-bottom path. A null cell is an obstacle and each
     * move goes to the next row in the same or an adjacent column. Ties choose the
     * leftmost start and then the leftmost next cell, making results reproducible.
     */
    public PathResult findBestPath(List<List<Integer>> grid) {
        GridValidator.validate(grid);

        int rows = grid.size();
        int columns = grid.getFirst().size();
        long[][] best = new long[rows][columns];
        int[][] nextColumn = new int[rows][columns];

        for (int column = 0; column < columns; column++) {
            best[rows - 1][column] = grid.get(rows - 1).get(column) == null
                    ? UNREACHABLE : grid.get(rows - 1).get(column);
            nextColumn[rows - 1][column] = -1;
        }

        for (int row = rows - 2; row >= 0; row--) {
            for (int column = 0; column < columns; column++) {
                Integer value = grid.get(row).get(column);
                best[row][column] = UNREACHABLE;
                nextColumn[row][column] = -1;
                if (value == null) {
                    continue;
                }

                for (int candidate = Math.max(0, column - 1);
                     candidate <= Math.min(columns - 1, column + 1); candidate++) {
                    if (best[row + 1][candidate] != UNREACHABLE
                            && (nextColumn[row][column] == -1
                            || best[row + 1][candidate] > best[row + 1][nextColumn[row][column]])) {
                        nextColumn[row][column] = candidate;
                    }
                }

                if (nextColumn[row][column] != -1) {
                    best[row][column] = Math.addExact(value, best[row + 1][nextColumn[row][column]]);
                }
            }
        }

        int start = -1;
        for (int column = 0; column < columns; column++) {
            if (best[0][column] != UNREACHABLE
                    && (start == -1 || best[0][column] > best[0][start])) {
                start = column;
            }
        }
        if (start == -1) {
            throw new NoPathException("No traversable path connects the top and bottom rows");
        }

        List<Cell> path = new ArrayList<>(rows);
        int column = start;
        for (int row = 0; row < rows; row++) {
            path.add(new Cell(row, column, grid.get(row).get(column)));
            column = nextColumn[row][column];
        }
        return new PathResult(start, best[0][start], path);
    }

}
