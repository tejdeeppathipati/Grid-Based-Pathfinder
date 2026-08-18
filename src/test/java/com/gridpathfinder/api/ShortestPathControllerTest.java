package com.gridpathfinder.api;

import com.gridpathfinder.domain.Position;
import com.gridpathfinder.domain.ShortestPathResult;
import com.gridpathfinder.service.ShortestPathService;
import com.gridpathfinder.service.PathSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortestPathController.class)
class ShortestPathControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private ShortestPathService shortestPathService;
    @MockitoBean private PathSearchService pathSearchService;

    @Test
    void returnsShortestPathAsJson() throws Exception {
        when(shortestPathService.findShortestPath(any(), any(), any())).thenReturn(
                new ShortestPathResult("BFS", 1, 2,
                        List.of(new Position(0, 0), new Position(0, 1))));

        mockMvc.perform(post("/api/v1/paths/shortest")
                        .contentType("application/json")
                        .content("""
                                {"grid":[[1,1]],"start":{"row":0,"column":0},
                                 "destination":{"row":0,"column":1}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.algorithm").value("BFS"))
                .andExpect(jsonPath("$.steps").value(1))
                .andExpect(jsonPath("$.path[1].column").value(1));
    }

    @Test
    void validatesRequiredDestination() throws Exception {
        mockMvc.perform(post("/api/v1/paths/shortest")
                        .contentType("application/json")
                        .content("{\"grid\":[[1]],\"start\":{\"row\":0,\"column\":0}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("destination is required"));
    }
}
