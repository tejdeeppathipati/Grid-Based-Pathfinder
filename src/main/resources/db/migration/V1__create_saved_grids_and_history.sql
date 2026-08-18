CREATE TABLE saved_grids (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    grid_json TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE search_history (
    id UUID PRIMARY KEY,
    grid_id UUID NOT NULL,
    algorithm VARCHAR(20) NOT NULL,
    start_row INTEGER NOT NULL,
    start_column INTEGER NOT NULL,
    destination_row INTEGER NOT NULL,
    destination_column INTEGER NOT NULL,
    total_cost BIGINT NOT NULL,
    steps INTEGER NOT NULL,
    visited_cells INTEGER NOT NULL,
    duration_nanos BIGINT NOT NULL,
    path_json TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_history_grid FOREIGN KEY (grid_id) REFERENCES saved_grids(id) ON DELETE CASCADE
);

CREATE INDEX idx_search_history_grid_created ON search_history(grid_id, created_at DESC);
