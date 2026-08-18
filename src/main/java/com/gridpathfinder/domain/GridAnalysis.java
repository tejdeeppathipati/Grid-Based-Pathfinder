package com.gridpathfinder.domain;

public record GridAnalysis(
        int rows,
        int columns,
        int traversableCells,
        int obstacleCells,
        double obstaclePercentage,
        Integer minimumWeight,
        Integer maximumWeight,
        double averageWeight
) {
}
