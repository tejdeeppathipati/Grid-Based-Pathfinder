package com.gridpathfinder.service;

import com.gridpathfinder.domain.GridAnalysis;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GridAnalysisServiceTest {
    private final GridAnalysisService service = new GridAnalysisService();

    @Test
    void summarizesGridDimensionsObstaclesAndWeights() {
        GridAnalysis result = service.analyze(List.of(
                Arrays.asList(2, null),
                List.of(-2, 6)
        ));

        assertThat(result.rows()).isEqualTo(2);
        assertThat(result.columns()).isEqualTo(2);
        assertThat(result.traversableCells()).isEqualTo(3);
        assertThat(result.obstacleCells()).isOne();
        assertThat(result.obstaclePercentage()).isEqualTo(25.0);
        assertThat(result.minimumWeight()).isEqualTo(-2);
        assertThat(result.maximumWeight()).isEqualTo(6);
        assertThat(result.averageWeight()).isEqualTo(2.0);
    }

    @Test
    void handlesGridContainingOnlyObstacles() {
        GridAnalysis result = service.analyze(List.of(Arrays.asList(null, null)));

        assertThat(result.traversableCells()).isZero();
        assertThat(result.minimumWeight()).isNull();
        assertThat(result.maximumWeight()).isNull();
        assertThat(result.averageWeight()).isZero();
    }
}
