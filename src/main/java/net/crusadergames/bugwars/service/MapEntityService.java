package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.MapEntityRequest;
import net.crusadergames.bugwars.exceptions.NameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.NameBlankException;
import net.crusadergames.bugwars.model.MapEntity;
import net.crusadergames.bugwars.repository.MapEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.NameNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MapEntityService {

    @Autowired
    MapEntityRepository mapEntityRepository;

    public MapEntityService(MapEntityRepository mapEntityRepository) {
        this.mapEntityRepository = mapEntityRepository;
    }

    public List<MapEntity> getAllEntities() {
        List<MapEntity> entityList = mapEntityRepository.findAll();
        return entityList;
    }

    public MapEntity getEntityById(Long entityId) throws Exception {
        Optional<MapEntity> optionalMapEntity = mapEntityRepository.findById(entityId);
        throwEntityNotFound(optionalMapEntity);
        MapEntity mapEntity = optionalMapEntity.get();
        return mapEntity;
    }

    public MapEntity createNewEntity(MapEntityRequest mapEntityRequest) throws Exception{
        throwEntityNameBlank(mapEntityRequest);

        throwEntityNameAlreadyExists(mapEntityRequest.getName());

        MapEntity entity = new MapEntity(null, mapEntityRequest.getName(), mapEntityRequest.getEntityCode(), mapEntityRequest.getImagePath());
        entity = mapEntityRepository.save(entity);
        return entity;
    }

    public MapEntity updateEntity(Long entityId, MapEntityRequest mapEntityRequest) throws Exception {
        throwEntityNameBlank(mapEntityRequest);

        Optional<MapEntity> optionalMapEntity = mapEntityRepository.findById(entityId);
        throwEntityNotFound(optionalMapEntity);

        throwEntityNameAlreadyExists(mapEntityRequest.getName());

        MapEntity newEntity = new MapEntity(entityId, mapEntityRequest.getName(), mapEntityRequest.getEntityCode(), mapEntityRequest.getImagePath());
        mapEntityRepository.save(newEntity);

        return newEntity;
    }

    public String deleteEntityById(Long entityId) throws Exception {
        Optional<MapEntity> optionalMapEntity = mapEntityRepository.findById(entityId);
        throwEntityNotFound(optionalMapEntity);

        mapEntityRepository.deleteById(entityId);
        return("Entity successfully deleted");
    }

    public void throwEntityNameAlreadyExists(String entityName) throws Exception{
        Optional<MapEntity> optionalEntity = mapEntityRepository.findByNameIgnoreCase(entityName);
        if (optionalEntity.isPresent()) {
            throw new NameAlreadyExistsException();
        }
    }

    public void throwEntityNameBlank(MapEntityRequest mapEntityRequest) throws Exception {
        if (mapEntityRequest.getName().isBlank()) {
            throw new NameBlankException();
        }
    }

    private void throwEntityNotFound(Optional<MapEntity> entity) throws Exception {
        if (entity.isEmpty()) {
            throw new NameNotFoundException("Entity not found");
        }
    }
}
