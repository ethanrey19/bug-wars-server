package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.repository.TerrainRepository;
import net.crusadergames.bugwars.service.TerrainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TerrainController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TerrainControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerrainService terrainService;

    @MockBean
    private TerrainRepository terrainRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Terrain TERRAIN_1 = new Terrain(1L, "Test Terrain 1", "imagePath");
    private final Terrain TERRAIN_2 = new Terrain(2L, "Test Terrain 2", "imagePath");

//    @Test
//    public void getAllTerrain_returnTerrainIfSuccess() throws Exception {
//        Terrain terrain1 = new Terrain(1L, "Test Terrain 1", "imagePath");
//        Terrain terrain2 = new Terrain(2L, "Test Terrain 2", "imagePath");
//        List<Terrain> expectedTerrain = new ArrayList<>();
//        expectedTerrain.add(terrain1);
//        expectedTerrain.add(terrain2);
//
//        when(terrainService.getAllTerrain()).thenReturn(expectedTerrain);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/terrain")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expectedTerrain)));
//    }
}
