package com.gridpathfinder.service;

import com.gridpathfinder.domain.NoPathException;
import com.gridpathfinder.domain.Position;
import com.gridpathfinder.domain.ShortestPathResult;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

@Service
public class ShortestPathService {
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

    /** Finds an unweighted four-directional shortest path using breadth-first search. */
    public ShortestPathResult findShortestPath(
            List<List<Integer>> grid, Position start, Position destination) {
        GridValidator.validate(grid);
        GridValidator.validateTraversablePosition(grid, start, "start");
        GridValidator.validateTraversablePosition(grid, destination, "destination");

        boolean[][] visited = new boolean[grid.size()][grid.getFirst().size()];
        Position[][] previous = new Position[grid.size()][grid.getFirst().size()];
        Deque<Position> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.row()][start.column()] = true;
        int visitedCells = 0;

        while (!queue.isEmpty()) {
            Position current = queue.removeFirst();
            visitedCells++;
            if (current.equals(destination)) {
                List<Position> path = reconstructPath(previous, destination);
                return new ShortestPathResult("BFS", path.size() - 1, visitedCells, path);
            }

            for (int[] direction : DIRECTIONS) {
                int row = current.row() + direction[0];
                int column = current.column() + direction[1];
                if (isTraversable(grid, row, column) && !visited[row][column]) {
                    visited[row][column] = true;
                    previous[row][column] = current;
                    queue.addLast(new Position(row, column));
                }
            }
        }
        throw new NoPathException("No path connects start and destination");
    }

    private boolean isTraversable(List<List<Integer>> grid, int row, int column) {
        return row >= 0 && row < grid.size()
                && column >= 0 && column < grid.getFirst().size()
                && grid.get(row).get(column) != null;
    }

    private List<Position> reconstructPath(Position[][] previous, Position destination) {
        List<Position> path = new ArrayList<>();
        for (Position current = destination; current != null;
             current = previous[current.row()][current.column()]) {
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }
}
