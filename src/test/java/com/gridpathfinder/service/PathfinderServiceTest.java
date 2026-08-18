package com.gridpathfinder.service;

import com.gridpathfinder.domain.NoPathException;
import com.gridpathfinder.domain.PathResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathfinderServiceTest {
    private final PathfinderService service = new PathfinderService();

    @Test
    void findsMaximumScorePathAroundObstacles() {
        List<List<Integer>> grid = List.of(
                List.of(5, 1, 2),
                row(4, null, 10),
                List.of(1, 20, 1)
        );

        PathResult result = service.findBestPath(grid);

        assertThat(result.startingColumn()).isEqualTo(2);
        assertThat(result.totalScore()).isEqualTo(32);
        assertThat(result.path()).extracting(cell -> cell.column()).containsExactly(2, 2, 1);
    }

    @Test
    void handlesNegativeScoresWithoutChoosingAnIncompletePath() {
        PathResult result = service.findBestPath(List.of(
                List.of(-5, -1),
                List.of(-2, -10)
        ));

        assertThat(result.totalScore()).isEqualTo(-3);
        assertThat(result.startingColumn()).isEqualTo(1);
    }

    @Test
    void choosesLeftmostPathWhenScoresTie() {
        PathResult result = service.findBestPath(List.of(
                List.of(1, 1),
                List.of(1, 1)
        ));

        assertThat(result.startingColumn()).isZero();
        assertThat(result.path()).extracting(cell -> cell.column()).containsExactly(0, 0);
    }

    @Test
    void rejectsNonRectangularGrid() {
        assertThatThrownBy(() -> service.findBestPath(List.of(List.of(1, 2), List.of(3))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rectangular");
    }

    @Test
    void reportsWhenNoCompletePathExists() {
        assertThatThrownBy(() -> service.findBestPath(List.of(List.of(1, 2), row(null, null))))
                .isInstanceOf(NoPathException.class);
    }

    private static List<Integer> row(Integer... values) {
        return java.util.Arrays.asList(values);
    }
}
