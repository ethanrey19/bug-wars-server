package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.dto.request.TerrainRequest;
import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.service.TerrainService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class TerrainControllerTest {
    private final Terrain TERRAIN_1 = new Terrain(1L, "Test Terrain 1", "imagePath");
    private final Terrain TERRAIN_2 = new Terrain(2L, "Test Terrain 2", "imagePath");
    private final Terrain UPDATED_TERRAIN = new Terrain(1L, "Updated Terrain", "New imagePath");

    private TerrainService terrainService;
    private TerrainController terrainController;

    @BeforeEach
    public void beforeEachTest() {
        terrainService = Mockito.mock(TerrainService.class);
        terrainController = new TerrainController(terrainService);
    }

    @Test
    public void getAllTerrain_shouldReturnAllTerrain() {
        List<Terrain> expectedTerrain = new ArrayList<>();
        expectedTerrain.add(TERRAIN_1);
        expectedTerrain.add(TERRAIN_2);
        when(terrainService.getAllTerrain()).thenReturn(expectedTerrain);

        List<Terrain> listOfTerrain = terrainController.getAllTerrain();

        Assert.assertEquals(expectedTerrain, listOfTerrain);
    }

    @Test
    public void getTerrainById_shouldReturnCorrectTerrain() throws Exception {
        when(terrainService.getTerrainById(1L)).thenReturn(TERRAIN_1);

        Terrain retrievedTerrain = terrainController.getTerrainById(1L);

        Assert.assertEquals(TERRAIN_1, retrievedTerrain);
    }

    @Test
    public void postTerrain_shouldReturnCreatedTerrain() throws Exception {
        TerrainRequest request = new TerrainRequest("Test Terrain 1", "imagePath");
        when(terrainService.createNewTerrain(request)).thenReturn(TERRAIN_1);

        ResponseEntity<Terrain> createdTerrain = terrainController.postTerrain(request);

        Assert.assertEquals(TERRAIN_1, createdTerrain.getBody());
        Assert.assertEquals(HttpStatus.CREATED, createdTerrain.getStatusCode());
    }

    @Test
    public void updateTerrain_shouldReturnUpdatedTerrain() throws Exception {
        TerrainRequest request = new TerrainRequest("Test Terrain 1", "imagePath");
        when(terrainService.updateTerrain(1L, request)).thenReturn(TERRAIN_1);

        ResponseEntity<Terrain> updatedTerrain = terrainController.updateTerrain(1L, request);

        Assert.assertEquals(TERRAIN_1, updatedTerrain.getBody());
        Assert.assertEquals(HttpStatus.ACCEPTED, updatedTerrain.getStatusCode());
    }

    @Test
    public void deleteTerrain_shouldReturnDeletedTerrainMessage() throws Exception{
        when(terrainService.deleteTerrainById(1L)).thenReturn("Terrain successfully deleted");

        ResponseEntity<String> deletedTerrain = terrainController.deleteTerrain(1L);

        Assert.assertEquals("Terrain successfully deleted", deletedTerrain.getBody());
        Assert.assertEquals(HttpStatus.OK, deletedTerrain.getStatusCode());
    }
}
