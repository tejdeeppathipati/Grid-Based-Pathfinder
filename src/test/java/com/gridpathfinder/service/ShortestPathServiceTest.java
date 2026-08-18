package com.gridpathfinder.service;

import com.gridpathfinder.domain.NoPathException;
import com.gridpathfinder.domain.Position;
import com.gridpathfinder.domain.ShortestPathResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestPathServiceTest {
    private final ShortestPathService service = new ShortestPathService();

    @Test
    void findsShortestPathAroundObstacles() {
        List<List<Integer>> grid = List.of(
                row(1, 1, 1, 1),
                row(null, null, 1, null),
                row(1, 1, 1, 1)
        );

        ShortestPathResult result = service.findShortestPath(
                grid, new Position(0, 0), new Position(2, 0));

        assertThat(result.algorithm()).isEqualTo("BFS");
        assertThat(result.steps()).isEqualTo(6);
        assertThat(result.path()).startsWith(new Position(0, 0)).endsWith(new Position(2, 0));
        assertThat(result.visitedCells()).isPositive();
    }

    @Test
    void returnsZeroStepsWhenStartEqualsDestination() {
        ShortestPathResult result = service.findShortestPath(
                List.of(List.of(1)), new Position(0, 0), new Position(0, 0));

        assertThat(result.steps()).isZero();
        assertThat(result.visitedCells()).isOne();
    }

    @Test
    void rejectsAnObstacleAsAStartPosition() {
        assertThatThrownBy(() -> service.findShortestPath(
                List.of(row(null, 1)), new Position(0, 0), new Position(0, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("start");
    }

    @Test
    void reportsWhenDestinationCannotBeReached() {
        assertThatThrownBy(() -> service.findShortestPath(
                List.of(row(1, null, 1)), new Position(0, 0), new Position(0, 2)))
                .isInstanceOf(NoPathException.class);
    }

    private static List<Integer> row(Integer... values) {
        return Arrays.asList(values);
    }
}
