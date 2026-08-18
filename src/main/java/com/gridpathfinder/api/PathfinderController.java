package com.gridpathfinder.api;

import com.gridpathfinder.domain.PathResult;
import com.gridpathfinder.service.PathfinderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PathfinderController {
    private final PathfinderService pathfinderService;

    public PathfinderController(PathfinderService pathfinderService) {
        this.pathfinderService = pathfinderService;
    }

    @GetMapping
    public ResponseEntity<Void> index() {
        return ResponseEntity.status(302).location(URI.create("/actuator/health")).build();
    }

    @PostMapping("/paths/best")
    public PathResult findBestPath(@Valid @RequestBody PathRequest request) {
        return pathfinderService.findBestPath(request.grid());
    }
}
