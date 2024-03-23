package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.dto.request.MapEntityRequest;
import net.crusadergames.bugwars.dto.request.TerrainRequest;
import net.crusadergames.bugwars.model.MapEntity;
import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.repository.MapEntityRepository;
import net.crusadergames.bugwars.service.MapEntityService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class MapEntityControllerTest {
    private final MapEntity ENTITY_1 = new MapEntity(1L, "Test Entity 1", "X", "imagePath");
    private final MapEntity ENTITY_2 = new MapEntity(2L, "Test Entity 2", "Y", "imagePath");
    private final MapEntity UPDATED_ENTITY = new MapEntity(1L, "Updated Entity", "Z", "New imagePath");

    private MapEntityService mapEntityService;
    private MapEntityController mapEntityController;

    @BeforeEach
    public void beforeEachTest() {
        mapEntityService = Mockito.mock(MapEntityService.class);
        mapEntityController = new MapEntityController(mapEntityService);
    }

    @Test
    public void getAllEntities_shouldReturnAllEntities() {
        List<MapEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(ENTITY_1);
        expectedEntities.add(ENTITY_2);
        when(mapEntityService.getAllEntities()).thenReturn(expectedEntities);

        List<MapEntity> listOfEntities = mapEntityController.getAllEntities();

        Assert.assertEquals(expectedEntities, listOfEntities);
    }

    @Test
    public void getEntityById_shouldReturnCorrectEntity() throws Exception {
        when(mapEntityService.getEntityById(1L)).thenReturn(ENTITY_1);

        MapEntity retrievedEntity = mapEntityController.getEntityById(1L);

        Assert.assertEquals(ENTITY_1, retrievedEntity);
    }

    @Test
    public void postEntity_shouldReturnCreatedEntity() throws Exception {
        MapEntityRequest request = new MapEntityRequest("Test Entity 1", "X", "imagePath");
        when(mapEntityService.createNewEntity(request)).thenReturn(ENTITY_1);

        ResponseEntity<MapEntity> createdEntity = mapEntityController.postEntity(request);

        Assert.assertEquals(ENTITY_1, createdEntity.getBody());
        Assert.assertEquals(HttpStatus.CREATED, createdEntity.getStatusCode());
    }

    @Test
    public void updateEntity_shouldReturnUpdatedEntity() throws Exception {
        MapEntityRequest request = new MapEntityRequest("Test Entity 1","X", "imagePath");
        when(mapEntityService.updateEntity(1L, request)).thenReturn(ENTITY_1);

        ResponseEntity<MapEntity> updatedEntity = mapEntityController.updateEntity(1L, request);

        Assert.assertEquals(ENTITY_1, updatedEntity.getBody());
        Assert.assertEquals(HttpStatus.ACCEPTED, updatedEntity.getStatusCode());
    }

    @Test
    public void deleteEntity_shouldReturnDeletedEntityMessage() throws Exception{
        when(mapEntityService.deleteEntityById(1L)).thenReturn("Entity successfully deleted");

        ResponseEntity<String> deletedEntity = mapEntityController.deleteEntity(1L);

        Assert.assertEquals("Entity successfully deleted", deletedEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, deletedEntity.getStatusCode());
    }
}
