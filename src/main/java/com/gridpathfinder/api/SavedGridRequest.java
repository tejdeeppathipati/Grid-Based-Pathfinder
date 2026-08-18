package com.gridpathfinder.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SavedGridRequest(
        @NotBlank @Size(max = 120) String name,
        @NotEmpty List<List<Integer>> grid
) {
}
