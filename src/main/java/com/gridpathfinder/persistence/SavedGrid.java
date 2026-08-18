package com.gridpathfinder.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "saved_grids")
public class SavedGrid {
    @Id private UUID id;
    @Column(nullable = false, length = 120) private String name;
    @Column(name = "grid_json", nullable = false, columnDefinition = "TEXT") private String gridJson;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @Version private long version;

    protected SavedGrid() {
    }

    public SavedGrid(UUID id, String name, String gridJson, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.gridJson = gridJson;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void update(String name, String gridJson, Instant updatedAt) {
        this.name = name;
        this.gridJson = gridJson;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getGridJson() { return gridJson; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
