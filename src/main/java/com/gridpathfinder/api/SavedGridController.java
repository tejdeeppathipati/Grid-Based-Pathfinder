package com.gridpathfinder.api;

import com.gridpathfinder.domain.PathSearchResult;
import com.gridpathfinder.persistence.SearchHistory;
import com.gridpathfinder.service.SavedGridService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/saved-grids")
public class SavedGridController {
    private final SavedGridService service;

    public SavedGridController(SavedGridService service) { this.service = service; }

    @PostMapping
    ResponseEntity<SavedGridResponse> create(@Valid @RequestBody SavedGridRequest request) {
        SavedGridResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/saved-grids/" + created.id())).body(created);
    }

    @GetMapping
    Page<SavedGridResponse> list(Pageable pageable) { return service.list(pageable); }

    @GetMapping("/{id}")
    SavedGridResponse get(@PathVariable UUID id) { return service.get(id); }

    @PutMapping("/{id}")
    SavedGridResponse update(@PathVariable UUID id, @Valid @RequestBody SavedGridRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/searches")
    PathSearchResult search(@PathVariable UUID id, @Valid @RequestBody SavedGridSearchRequest request) {
        return service.search(id, request);
    }

    @GetMapping("/{id}/searches")
    Page<SearchHistory> history(@PathVariable UUID id, Pageable pageable) {
        return service.history(id, pageable);
    }
}
