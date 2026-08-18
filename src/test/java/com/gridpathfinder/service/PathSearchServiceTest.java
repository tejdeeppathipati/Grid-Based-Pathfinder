package com.gridpathfinder.service;

import com.gridpathfinder.domain.Algorithm;
import com.gridpathfinder.domain.PathSearchResult;
import com.gridpathfinder.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathSearchServiceTest {
    private final PathSearchService service = new PathSearchService();

    @Test
    void dijkstraPrefersLowerCostOverFewerSteps() {
        List<List<Integer>> grid = List.of(
                List.of(1, 9, 1),
                List.of(1, 1, 1)
        );
        PathSearchResult result = service.search(grid, new Position(0, 0),
                new Position(0, 2), Algorithm.DIJKSTRA);

        assertThat(result.totalCost()).isEqualTo(4);
        assertThat(result.steps()).isEqualTo(4);
    }

    @Test
    void aStarReturnsSameOptimalCostAsDijkstra() {
        List<List<Integer>> grid = List.of(List.of(1, 3, 1), List.of(1, 1, 1));
        PathSearchResult dijkstra = service.search(grid, new Position(0, 0),
                new Position(0, 2), Algorithm.DIJKSTRA);
        PathSearchResult aStar = service.search(grid, new Position(0, 0),
                new Position(0, 2), Algorithm.ASTAR);

        assertThat(aStar.totalCost()).isEqualTo(dijkstra.totalCost());
    }
}
