package com.gridpathfinder.service;

import com.gridpathfinder.domain.GridAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridAnalysisService {
    public GridAnalysis analyze(List<List<Integer>> grid) {
        GridValidator.validate(grid);
        int traversable = 0;
        int obstacles = 0;
        long weightSum = 0;
        Integer minimum = null;
        Integer maximum = null;

        for (List<Integer> row : grid) {
            for (Integer value : row) {
                if (value == null) {
                    obstacles++;
                } else {
                    traversable++;
                    weightSum += value;
                    minimum = minimum == null ? value : Math.min(minimum, value);
                    maximum = maximum == null ? value : Math.max(maximum, value);
                }
            }
        }

        int cells = traversable + obstacles;
        double obstaclePercentage = obstacles * 100.0 / cells;
        double average = traversable == 0 ? 0 : (double) weightSum / traversable;
        return new GridAnalysis(grid.size(), grid.getFirst().size(), traversable, obstacles,
                obstaclePercentage, minimum, maximum, average);
    }
}
