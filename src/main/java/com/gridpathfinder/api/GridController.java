package com.gridpathfinder.api;

import com.gridpathfinder.domain.GridAnalysis;
import com.gridpathfinder.service.GridAnalysisService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/grids")
public class GridController {
    private final GridAnalysisService gridAnalysisService;

    public GridController(GridAnalysisService gridAnalysisService) {
        this.gridAnalysisService = gridAnalysisService;
    }

    @PostMapping("/analysis")
    public GridAnalysis analyze(@Valid @RequestBody PathRequest request) {
        return gridAnalysisService.analyze(request.grid());
    }
}
