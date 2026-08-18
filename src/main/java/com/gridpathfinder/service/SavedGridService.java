package com.gridpathfinder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gridpathfinder.api.SavedGridRequest;
import com.gridpathfinder.api.SavedGridResponse;
import com.gridpathfinder.api.SavedGridSearchRequest;
import com.gridpathfinder.domain.PathSearchResult;
import com.gridpathfinder.domain.Position;
import com.gridpathfinder.domain.ResourceNotFoundException;
import com.gridpathfinder.persistence.SavedGrid;
import com.gridpathfinder.persistence.SavedGridRepository;
import com.gridpathfinder.persistence.SearchHistory;
import com.gridpathfinder.persistence.SearchHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SavedGridService {
    private static final TypeReference<List<List<Integer>>> GRID_TYPE = new TypeReference<>() {};
    private final SavedGridRepository grids;
    private final SearchHistoryRepository history;
    private final PathSearchService pathSearch;
    private final ObjectMapper objectMapper;

    public SavedGridService(SavedGridRepository grids, SearchHistoryRepository history,
                            PathSearchService pathSearch, ObjectMapper objectMapper) {
        this.grids = grids; this.history = history; this.pathSearch = pathSearch;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public SavedGridResponse create(SavedGridRequest request) {
        GridValidator.validate(request.grid());
        Instant now = Instant.now();
        return response(grids.save(new SavedGrid(UUID.randomUUID(), request.name().trim(),
                write(request.grid()), now)));
    }

    @Transactional(readOnly = true)
    public Page<SavedGridResponse> list(Pageable pageable) {
        return grids.findAll(pageable).map(this::response);
    }

    @Transactional(readOnly = true)
    public SavedGridResponse get(UUID id) { return response(entity(id)); }

    @Transactional
    public SavedGridResponse update(UUID id, SavedGridRequest request) {
        GridValidator.validate(request.grid());
        SavedGrid grid = entity(id);
        grid.update(request.name().trim(), write(request.grid()), Instant.now());
        return response(grid);
    }

    @Transactional
    public void delete(UUID id) { grids.delete(entity(id)); }

    @Transactional
    public PathSearchResult search(UUID id, SavedGridSearchRequest request) {
        SavedGrid grid = entity(id);
        List<List<Integer>> cells = read(grid.getGridJson());
        PathSearchResult result = pathSearch.search(cells, request.start(), request.destination(), request.algorithm());
        history.save(new SearchHistory(UUID.randomUUID(), id, result.algorithm(),
                request.start().row(), request.start().column(), request.destination().row(),
                request.destination().column(), result.totalCost(), result.steps(), result.visitedCells(),
                result.durationNanos(), write(result.path()), Instant.now()));
        return result;
    }

    @Transactional(readOnly = true)
    public Page<SearchHistory> history(UUID id, Pageable pageable) {
        entity(id);
        return history.findByGridId(id, pageable);
    }

    private SavedGrid entity(UUID id) {
        return grids.findById(id).orElseThrow(() -> new ResourceNotFoundException("Grid not found: " + id));
    }

    private SavedGridResponse response(SavedGrid grid) {
        return new SavedGridResponse(grid.getId(), grid.getName(), read(grid.getGridJson()),
                grid.getCreatedAt(), grid.getUpdatedAt());
    }

    private String write(Object value) {
        try { return objectMapper.writeValueAsString(value); }
        catch (JsonProcessingException exception) { throw new IllegalStateException("Could not serialize grid", exception); }
    }

    private List<List<Integer>> read(String value) {
        try { return objectMapper.readValue(value, GRID_TYPE); }
        catch (JsonProcessingException exception) { throw new IllegalStateException("Could not deserialize grid", exception); }
    }
}
