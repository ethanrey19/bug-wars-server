package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.TerrainRequest;
import net.crusadergames.bugwars.exceptions.NameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.NameBlankException;
import net.crusadergames.bugwars.model.Terrain;
import net.crusadergames.bugwars.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.NameNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TerrainService {

    @Autowired
    TerrainRepository terrainRepository;

    public TerrainService(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    public List<Terrain> getAllTerrain() {
        List<Terrain> terrainList = terrainRepository.findAll();
        return terrainList;
    }

    public Terrain getTerrainById(UUID terrainId) throws Exception {
        Optional<Terrain> optionalTerrain = terrainRepository.findById(terrainId);
        throwTerrainNotFound(optionalTerrain);
        Terrain terrain = optionalTerrain.get();
        return terrain;
    }

    public Terrain createNewTerrain(TerrainRequest terrainRequest) throws Exception {
        throwTerrainNameBlank(terrainRequest);

        throwTerrainNameAlreadyExists(terrainRequest.getName());

        Terrain terrain = new Terrain(null, terrainRequest.getName(), terrainRequest.getImagePath());
        terrain = terrainRepository.save(terrain);
        return terrain;
    }

    public Terrain updateTerrain(UUID terrainId, TerrainRequest terrainRequest) throws Exception {
        throwTerrainNameBlank(terrainRequest);

        Optional<Terrain> optionalTerrain = terrainRepository.findById(terrainId);
        throwTerrainNotFound(optionalTerrain);

        throwTerrainNameAlreadyExists(terrainRequest.getName());

        Terrain newTerrain = new Terrain(terrainId, terrainRequest.getName(), terrainRequest.getImagePath());
        terrainRepository.save(newTerrain);

        return newTerrain;
    }

    public String deleteTerrainById(UUID terrainId) throws Exception {
        Optional<Terrain> optionalTerrain = terrainRepository.findById(terrainId);
        throwTerrainNotFound(optionalTerrain);

        terrainRepository.deleteById(terrainId);
        return("Terrain successfully deleted");
    }

    public void throwTerrainNameAlreadyExists(String terrainName) throws Exception{
        Optional<Terrain> optionalTerrain = terrainRepository.findByNameIgnoreCase(terrainName);
        if (optionalTerrain.isPresent()) {
            throw new NameAlreadyExistsException();
        }
    }

    public void throwTerrainNameBlank(TerrainRequest terrainRequest) throws Exception {
        if (terrainRequest.getName().isBlank()) {
            throw new NameBlankException();
        }
    }

    private void throwTerrainNotFound(Optional<Terrain> terrain) throws Exception {
        if (terrain.isEmpty()) {
            throw new NameNotFoundException("Terrain not found");
        }
    }
}
