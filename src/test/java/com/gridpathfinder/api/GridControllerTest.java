package com.gridpathfinder.api;

import com.gridpathfinder.domain.GridAnalysis;
import com.gridpathfinder.service.GridAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GridController.class)
class GridControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private GridAnalysisService gridAnalysisService;

    @Test
    void returnsGridAnalysisAsJson() throws Exception {
        when(gridAnalysisService.analyze(any())).thenReturn(
                new GridAnalysis(2, 2, 3, 1, 25, -2, 6, 2));

        mockMvc.perform(post("/api/v1/grids/analysis")
                        .contentType("application/json")
                        .content("{\"grid\":[[2,null],[-2,6]]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").value(2))
                .andExpect(jsonPath("$.obstaclePercentage").value(25))
                .andExpect(jsonPath("$.minimumWeight").value(-2));
    }
}
