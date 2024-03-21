package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.TerrainRequest;
import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.repository.TerrainRepository;
import net.crusadergames.bugwars.service.TerrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/terrain")
@RequiredArgsConstructor
public class TerrainController {

    private final TerrainService terrainService;

    @GetMapping
    public List<Terrain> getAllTerrain() {
        return terrainService.getAllTerrain();
    }

    @GetMapping("/{terrainId}")
    public Terrain getTerrainById(@PathVariable Long terrainId) throws Exception {
        return terrainService.getTerrainById(terrainId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Terrain> postTerrain(@RequestBody TerrainRequest terrainRequest) throws Exception {
        Terrain terrain = terrainService.createNewTerrain(terrainRequest);
        return new ResponseEntity<>(terrain, HttpStatus.CREATED);
    }

    @PutMapping("/{terrainId}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long terrainId, @RequestBody TerrainRequest terrainRequest) throws Exception {
        Terrain terrain = terrainService.updateTerrain(terrainId, terrainRequest);
        return new ResponseEntity<>(terrain, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{terrainId}")
    public ResponseEntity<String> deleteTerrain(@PathVariable Long terrainId) throws Exception {
        String response = terrainService.deleteTerrainById(terrainId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
