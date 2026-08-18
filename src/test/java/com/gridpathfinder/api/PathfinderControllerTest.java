package com.gridpathfinder.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gridpathfinder.service.PathfinderService;
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

@WebMvcTest(PathfinderController.class)
class PathfinderControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private PathfinderService pathfinderService;

    @Test
    void returnsPathResultAsJson() throws Exception {
        when(pathfinderService.findBestPath(any())).thenReturn(
                new com.gridpathfinder.domain.PathResult(0, 3,
                        List.of(new com.gridpathfinder.domain.Cell(0, 0, 3))));

        mockMvc.perform(post("/api/v1/paths/best")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PathRequest(List.of(List.of(3))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startingColumn").value(0))
                .andExpect(jsonPath("$.totalScore").value(3))
                .andExpect(jsonPath("$.path[0].value").value(3));
    }

    @Test
    void returnsProblemDetailsForEmptyGrid() throws Exception {
        mockMvc.perform(post("/api/v1/paths/best")
                        .contentType("application/json")
                        .content("{\"grid\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"));
    }
}
