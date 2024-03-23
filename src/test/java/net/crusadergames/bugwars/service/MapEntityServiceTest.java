package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.MapEntityRequest;
import net.crusadergames.bugwars.exceptions.NameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.NameBlankException;
import net.crusadergames.bugwars.model.MapEntity;
import net.crusadergames.bugwars.repository.MapEntityRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MapEntityServiceTest {
    private final MapEntity ENTITY_1 = new MapEntity(1L, "Test Entity 1", "X", "imagePath");
    private final MapEntity ENTITY_2 = new MapEntity(2L, "Test Entity 2", "Y", "imagePath");
    private final MapEntity UPDATED_ENTITY = new MapEntity(1L, "Updated Entity", "Z", "New imagePath");

    private MapEntityService mapEntityService;
    private MapEntityRepository mapEntityRepository;

    @BeforeEach
    public void beforeEachTest() {
        mapEntityRepository = Mockito.mock(MapEntityRepository.class);
        mapEntityService = new MapEntityService(mapEntityRepository);
    }

    @Test
    public void getAllEntities_shouldReturnListOfEntities() {
        MapEntity mapEntity1 = new MapEntity(1L, "Test Entity 1", "X", "imagePath");
        MapEntity mapEntity2 = new MapEntity(2L, "Test Entity 2", "Y", "imagePath");
        List<MapEntity> expected = new ArrayList<>();
        expected.add(mapEntity1);
        expected.add(mapEntity2);
        when(mapEntityRepository.findAll()).thenReturn(expected);

        List<MapEntity> actual = mapEntityService.getAllEntities();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getEntityById_shouldReturnCorrectEntity() throws Exception{
        when(mapEntityRepository.findById(any())).thenReturn(Optional.of(ENTITY_1));

        MapEntity mapEntity = mapEntityService.getEntityById(1L);

        Assert.assertNotNull(mapEntity);
        Assert.assertEquals(mapEntity.getId(), ENTITY_1.getId());
        Assert.assertEquals(mapEntity, ENTITY_1);
    }

    @Test
    public void getEntityById_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
            when(mapEntityRepository.findById(any())).thenReturn(Optional.empty());

            mapEntityService.getEntityById(3L);
        });
    }

    @Test
    public void createNewEntity_shouldReturnCreatedEntity() throws Exception {
        MapEntityRequest mapEntityRequest = new MapEntityRequest("Test Entity 1", "X", "imagePath");
        when(mapEntityRepository.save(any())).thenReturn(ENTITY_1);

        MapEntity createdEntity = mapEntityService.createNewEntity(mapEntityRequest);

        Assert.assertNotNull(createdEntity);
        Assert.assertEquals(createdEntity.getId(), ENTITY_1.getId());
        Assert.assertEquals(createdEntity, ENTITY_1);
    }

    @Test
    public void createNewEntity_shouldThrowNameBlankException() {
        Assert.assertThrows(NameBlankException.class, () -> {
            mapEntityService.createNewEntity(new MapEntityRequest("", "", ""));
        });
    }

    @Test
    public void createNewEntity_shouldThrowNameAlreadyExistsException() {
        Assert.assertThrows(NameAlreadyExistsException.class, () -> {
            when(mapEntityRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(ENTITY_1));
            mapEntityService.createNewEntity(new MapEntityRequest("Test Entity 1", "X", "imagePath"));
        });
    }

    @Test
    public void updateEntity_shouldReturnNewEntity() throws Exception {
        when(mapEntityRepository.findById(any())).thenReturn(Optional.of(ENTITY_1));
        MapEntityRequest oldEntityRequest = new MapEntityRequest("Test Entity 1", "X", "imagePath");
        MapEntityRequest newEntityRequest = new MapEntityRequest("Updated Entity", "Z", "New imagePath");
        mapEntityService.createNewEntity(oldEntityRequest);

        MapEntity mapEntity = mapEntityService.updateEntity(1L, newEntityRequest);

        Assert.assertNotNull(mapEntity);
        Assert.assertEquals(mapEntity.getId(), UPDATED_ENTITY.getId());
        Assert.assertEquals(mapEntity, UPDATED_ENTITY);
    }

    @Test
    public void updateEntity_shouldThrowNameBlankException() {
        Assert.assertThrows(NameBlankException.class, () -> {
            mapEntityService.updateEntity(1L, new MapEntityRequest("", "", ""));
        });
    }

    @Test
    public void updateEntity_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
            when(mapEntityRepository.findById(any())).thenReturn(Optional.empty());
            mapEntityService.updateEntity(3L, new MapEntityRequest("Updated Entity", "Z", "New imagePath"));
        });
    }

    @Test
    public void updateEntity_shouldThrowNameAlreadyExistsException() {
        Assert.assertThrows(NameAlreadyExistsException.class, () -> {
            when(mapEntityRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(ENTITY_1));
            mapEntityService.createNewEntity(new MapEntityRequest("Test Entity 1", "X", "imagePath"));
            mapEntityService.updateEntity(1L, new MapEntityRequest("Test Entity 1", "X", "imagePath"));
        });
    }

    @Test
    public void deleteEntity_shouldReturnString() throws Exception{
        MapEntityRequest mapEntityRequest = new MapEntityRequest("Test Entity 1", "X", "imagePath");
        when(mapEntityRepository.save(any())).thenReturn(ENTITY_1);
        when(mapEntityRepository.findById(any())).thenReturn(Optional.of(ENTITY_1));
        MapEntity createdEntity = mapEntityService.createNewEntity(mapEntityRequest);
        String string = mapEntityService.deleteEntityById(createdEntity.getId());
        Assert.assertNotNull(string);
        Assert.assertEquals(string, "Entity successfully deleted");
    }

    @Test
    public void deleteEntity_shouldThrowNotFoundException() {
        Assert.assertThrows(NameNotFoundException.class, () -> {
            mapEntityService.deleteEntityById(3L);
        });
    }
}
