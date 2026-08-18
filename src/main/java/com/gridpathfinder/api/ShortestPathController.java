package com.gridpathfinder.api;

import com.gridpathfinder.domain.ShortestPathResult;
import com.gridpathfinder.domain.Algorithm;
import com.gridpathfinder.domain.PathSearchResult;
import com.gridpathfinder.service.PathSearchService;
import com.gridpathfinder.service.ShortestPathService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/paths")
public class ShortestPathController {
    private final ShortestPathService shortestPathService;
    private final PathSearchService pathSearchService;

    public ShortestPathController(ShortestPathService shortestPathService,
                                  PathSearchService pathSearchService) {
        this.shortestPathService = shortestPathService;
        this.pathSearchService = pathSearchService;
    }

    @PostMapping("/shortest")
    public ShortestPathResult findShortestPath(@Valid @RequestBody ShortestPathRequest request) {
        return shortestPathService.findShortestPath(request.grid(), request.start(), request.destination());
    }

    @PostMapping("/search")
    public PathSearchResult search(@Valid @RequestBody PathSearchRequest request) {
        return pathSearchService.search(request.grid(), request.start(), request.destination(), request.algorithm());
    }

    @PostMapping("/compare")
    public Map<Algorithm, PathSearchResult> compare(@Valid @RequestBody ShortestPathRequest request) {
        return pathSearchService.compare(request.grid(), request.start(), request.destination());
    }
}
