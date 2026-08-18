package com.gridpathfinder.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SavedGridResponse(UUID id, String name, List<List<Integer>> grid,
                                Instant createdAt, Instant updatedAt) {
}
