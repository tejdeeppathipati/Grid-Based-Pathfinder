package com.gridpathfinder.api;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PathRequest(@NotEmpty(message = "grid must contain at least one row") List<List<Integer>> grid) {
}
