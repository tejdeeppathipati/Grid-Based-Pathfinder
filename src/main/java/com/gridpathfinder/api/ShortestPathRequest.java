package com.gridpathfinder.api;

import com.gridpathfinder.domain.Position;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ShortestPathRequest(
        @NotEmpty(message = "grid must contain at least one row") List<List<Integer>> grid,
        @NotNull(message = "start is required") Position start,
        @NotNull(message = "destination is required") Position destination
) {
}
