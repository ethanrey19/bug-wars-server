package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.MapEntityRequest;
import net.crusadergames.bugwars.model.MapEntity;
import net.crusadergames.bugwars.service.MapEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/entity")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MapEntityController {

    private final MapEntityService mapEntityService;

    @GetMapping
    public List<MapEntity> getAllEntities() {
        return mapEntityService.getAllEntities();
    }

    @GetMapping("/{entityId}")
    public MapEntity getEntityById(@PathVariable UUID entityId) throws Exception {
        return mapEntityService.getEntityById(entityId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<MapEntity> postEntity(@RequestBody MapEntityRequest mapEntityRequest) throws Exception {
        MapEntity mapEntity = mapEntityService.createNewEntity(mapEntityRequest);
        return new ResponseEntity<>(mapEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{entityId}")
    public ResponseEntity<MapEntity> updateEntity(@PathVariable UUID entityId, @RequestBody MapEntityRequest mapEntityRequest) throws Exception {
        MapEntity mapEntity = mapEntityService.updateEntity(entityId, mapEntityRequest);
        return new ResponseEntity<>(mapEntity, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{entityId}")
    public ResponseEntity<String> deleteEntity(@PathVariable UUID entityId) throws Exception {
        String response = mapEntityService.deleteEntityById(entityId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
