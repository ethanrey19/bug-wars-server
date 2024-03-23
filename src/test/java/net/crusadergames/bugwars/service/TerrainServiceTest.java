package net.crusadergames.bugwars.service;

import com.github.dockerjava.api.exception.NotFoundException;
import net.crusadergames.bugwars.dto.request.TerrainRequest;
import net.crusadergames.bugwars.exceptions.NameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.NameBlankException;
import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.repository.TerrainRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TerrainServiceTest {
    private final Terrain TERRAIN_1 = new Terrain(1L, "Test Terrain 1", "imagePath");
    private final Terrain TERRAIN_2 = new Terrain(2L, "Test Terrain 2", "imagePath");
    private final Terrain UPDATED_TERRAIN = new Terrain(1L, "Updated Terrain", "New imagePath");

    private TerrainService terrainService;
    private TerrainRepository terrainRepository;

    @BeforeEach
    public void beforeEachTest() {
        terrainRepository = Mockito.mock(TerrainRepository.class);
        terrainService = new TerrainService(terrainRepository);
    }

    @Test
    public void getAllTerrain_shouldReturnListOfTerrain() {
        Terrain terrain1 = new Terrain(1L, "Test Terrain 1", "imagePath");
        Terrain terrain2 = new Terrain(2L, "Test Terrain 2", "imagePath");
        List<Terrain> expected = new ArrayList<>();
        expected.add(terrain1);
        expected.add(terrain2);
        when(terrainRepository.findAll()).thenReturn(expected);

        List<Terrain> actual = terrainService.getAllTerrain();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getTerrainById_shouldReturnCorrectTerrain() throws Exception{
        when(terrainRepository.findById(any())).thenReturn(Optional.of(TERRAIN_1));

        Terrain terrain = terrainService.getTerrainById(1L);

        Assert.assertNotNull(terrain);
        Assert.assertEquals(terrain.getId(), TERRAIN_1.getId());
        Assert.assertEquals(terrain, TERRAIN_1);
    }

    @Test
    public void getTerrainById_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
            when(terrainRepository.findById(any())).thenReturn(Optional.empty());

            terrainService.getTerrainById(3L);
        });
    }

    @Test
    public void createNewTerrain_shouldReturnCreatedTerrain() throws Exception {
        TerrainRequest terrainRequest = new TerrainRequest("Test Terrain 1", "imagePath");
        when(terrainRepository.save(any())).thenReturn(TERRAIN_1);

        Terrain createdTerrain = terrainService.createNewTerrain(terrainRequest);

        Assert.assertNotNull(createdTerrain);
        Assert.assertEquals(createdTerrain.getId(), TERRAIN_1.getId());
        Assert.assertEquals(createdTerrain, TERRAIN_1);
    }

    @Test
    public void createNewTerrain_shouldThrowNameBlankException() {
        Assert.assertThrows(NameBlankException.class, () -> {
            terrainService.createNewTerrain(new TerrainRequest("", ""));
        });
    }

    @Test
    public void createNewTerrain_shouldThrowNameAlreadyExistsException() {
        Assert.assertThrows(NameAlreadyExistsException.class, () -> {
           when(terrainRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(TERRAIN_1));
            terrainService.createNewTerrain(new TerrainRequest("Test Terrain 1", "imagePath"));
        });
    }

    @Test
    public void updateTerrain_shouldReturnNewTerrain() throws Exception {
        when(terrainRepository.findById(any())).thenReturn(Optional.of(TERRAIN_1));
        TerrainRequest oldTerrainRequest = new TerrainRequest("Test Terrain 1", "imagePath");
        TerrainRequest newTerrainRequest = new TerrainRequest("Updated Terrain", "New imagePath");
        terrainService.createNewTerrain(oldTerrainRequest);

        Terrain terrain = terrainService.updateTerrain(1L, newTerrainRequest);

        Assert.assertNotNull(terrain);
        Assert.assertEquals(terrain.getId(), UPDATED_TERRAIN.getId());
        Assert.assertEquals(terrain, UPDATED_TERRAIN);
    }

    @Test
    public void updateTerrain_shouldThrowNameBlankException() {
        Assert.assertThrows(NameBlankException.class, () -> {
            terrainService.updateTerrain(1L, new TerrainRequest("", ""));
        });
    }

    @Test
    public void updateTerrain_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
            when(terrainRepository.findById(any())).thenReturn(Optional.empty());
            terrainService.updateTerrain(3L, new TerrainRequest("Updated Terrain", "New imagePath"));
        });
    }

    @Test
    public void updateTerrain_shouldThrowNameAlreadyExistsException() {
        Assert.assertThrows(NameAlreadyExistsException.class, () -> {
            when(terrainRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(TERRAIN_1));
            terrainService.createNewTerrain(new TerrainRequest("Test Terrain 1", "imagePath"));
            terrainService.updateTerrain(1L, new TerrainRequest("Test Terrain 1", "imagePath"));
        });
    }

    @Test
    public void deleteTerrain_shouldReturnString() throws Exception{
        TerrainRequest terrainRequest = new TerrainRequest("Test Terrain 1", "imagePath");
        when(terrainRepository.save(any())).thenReturn(TERRAIN_1);
        when(terrainRepository.findById(any())).thenReturn(Optional.of(TERRAIN_1));
        Terrain createdTerrain = terrainService.createNewTerrain(terrainRequest);
        String string = terrainService.deleteTerrainById(createdTerrain.getId());
        Assert.assertNotNull(string);
        Assert.assertEquals(string, "Terrain successfully deleted");
    }

    @Test
    public void deleteTerrain_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
           terrainService.deleteTerrainById(3L);
        });
    }
}
