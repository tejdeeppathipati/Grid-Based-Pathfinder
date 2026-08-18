package com.gridpathfinder.service;

import com.gridpathfinder.domain.Algorithm;
import com.gridpathfinder.domain.NoPathException;
import com.gridpathfinder.domain.PathSearchResult;
import com.gridpathfinder.domain.Position;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Service
public class PathSearchService {
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

    public PathSearchResult search(List<List<Integer>> grid, Position start,
                                   Position destination, Algorithm algorithm) {
        GridValidator.validate(grid);
        GridValidator.validateTraversablePosition(grid, start, "start");
        GridValidator.validateTraversablePosition(grid, destination, "destination");
        validateWeights(grid, algorithm);

        long started = System.nanoTime();
        int rows = grid.size();
        int columns = grid.getFirst().size();
        long[][] distance = new long[rows][columns];
        for (long[] row : distance) Arrays.fill(row, Long.MAX_VALUE);
        Position[][] previous = new Position[rows][columns];
        boolean[][] settled = new boolean[rows][columns];
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator
                .comparingLong(Node::priority).thenComparingLong(Node::distance)
                .thenComparingInt(node -> node.position().row())
                .thenComparingInt(node -> node.position().column()));
        distance[start.row()][start.column()] = 0;
        frontier.add(new Node(start, 0, heuristic(start, destination, algorithm)));
        int visited = 0;

        while (!frontier.isEmpty()) {
            Node node = frontier.remove();
            Position current = node.position();
            if (settled[current.row()][current.column()]) continue;
            settled[current.row()][current.column()] = true;
            visited++;
            if (current.equals(destination)) {
                List<Position> path = reconstruct(previous, destination);
                return new PathSearchResult(algorithm, path.size() - 1,
                        distance[current.row()][current.column()], visited,
                        System.nanoTime() - started, path);
            }

            for (int[] direction : DIRECTIONS) {
                int row = current.row() + direction[0];
                int column = current.column() + direction[1];
                if (!traversable(grid, row, column) || settled[row][column]) continue;
                long moveCost = algorithm == Algorithm.BFS ? 1 : grid.get(row).get(column);
                long candidate = Math.addExact(distance[current.row()][current.column()], moveCost);
                if (candidate < distance[row][column]) {
                    Position next = new Position(row, column);
                    distance[row][column] = candidate;
                    previous[row][column] = current;
                    frontier.add(new Node(next, candidate,
                            Math.addExact(candidate, heuristic(next, destination, algorithm))));
                }
            }
        }
        throw new NoPathException("No path connects start and destination");
    }

    public Map<Algorithm, PathSearchResult> compare(
            List<List<Integer>> grid, Position start, Position destination) {
        Map<Algorithm, PathSearchResult> results = new EnumMap<>(Algorithm.class);
        for (Algorithm algorithm : Algorithm.values()) {
            results.put(algorithm, search(grid, start, destination, algorithm));
        }
        return results;
    }

    private long heuristic(Position current, Position destination, Algorithm algorithm) {
        if (algorithm != Algorithm.ASTAR) return 0;
        return Math.abs(current.row() - destination.row())
                + Math.abs(current.column() - destination.column());
    }

    private void validateWeights(List<List<Integer>> grid, Algorithm algorithm) {
        if (algorithm == Algorithm.BFS) return;
        boolean invalid = grid.stream().flatMap(List::stream)
                .anyMatch(value -> value != null && value < 1);
        if (invalid) throw new IllegalArgumentException(
                "Dijkstra and A* require traversable cell weights of at least 1");
    }

    private boolean traversable(List<List<Integer>> grid, int row, int column) {
        return row >= 0 && row < grid.size() && column >= 0
                && column < grid.getFirst().size() && grid.get(row).get(column) != null;
    }

    private List<Position> reconstruct(Position[][] previous, Position destination) {
        List<Position> path = new ArrayList<>();
        for (Position current = destination; current != null;
             current = previous[current.row()][current.column()]) path.add(current);
        Collections.reverse(path);
        return path;
    }

    private record Node(Position position, long distance, long priority) {
    }
}
