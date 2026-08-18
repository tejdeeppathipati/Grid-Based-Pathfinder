package com.gridpathfinder.persistence;

import com.gridpathfinder.domain.Algorithm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id private UUID id;
    @Column(name = "grid_id", nullable = false) private UUID gridId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Algorithm algorithm;
    @Column(name = "start_row", nullable = false) private int startRow;
    @Column(name = "start_column", nullable = false) private int startColumn;
    @Column(name = "destination_row", nullable = false) private int destinationRow;
    @Column(name = "destination_column", nullable = false) private int destinationColumn;
    @Column(name = "total_cost", nullable = false) private long totalCost;
    @Column(nullable = false) private int steps;
    @Column(name = "visited_cells", nullable = false) private int visitedCells;
    @Column(name = "duration_nanos", nullable = false) private long durationNanos;
    @Column(name = "path_json", nullable = false, columnDefinition = "TEXT") private String pathJson;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected SearchHistory() {
    }

    public SearchHistory(UUID id, UUID gridId, Algorithm algorithm, int startRow, int startColumn,
                         int destinationRow, int destinationColumn, long totalCost, int steps,
                         int visitedCells, long durationNanos, String pathJson, Instant createdAt) {
        this.id = id; this.gridId = gridId; this.algorithm = algorithm;
        this.startRow = startRow; this.startColumn = startColumn;
        this.destinationRow = destinationRow; this.destinationColumn = destinationColumn;
        this.totalCost = totalCost; this.steps = steps; this.visitedCells = visitedCells;
        this.durationNanos = durationNanos; this.pathJson = pathJson; this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getGridId() { return gridId; }
    public Algorithm getAlgorithm() { return algorithm; }
    public int getStartRow() { return startRow; }
    public int getStartColumn() { return startColumn; }
    public int getDestinationRow() { return destinationRow; }
    public int getDestinationColumn() { return destinationColumn; }
    public long getTotalCost() { return totalCost; }
    public int getSteps() { return steps; }
    public int getVisitedCells() { return visitedCells; }
    public long getDurationNanos() { return durationNanos; }
    public String getPathJson() { return pathJson; }
    public Instant getCreatedAt() { return createdAt; }
}
