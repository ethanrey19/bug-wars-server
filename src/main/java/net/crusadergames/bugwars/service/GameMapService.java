package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.GameMapRequest;
import net.crusadergames.bugwars.exceptions.map.MapNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.map.MapNameOrBodyBlankException;
import net.crusadergames.bugwars.exceptions.map.TileNotFoundException;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.Tile;
import net.crusadergames.bugwars.repository.GameMapRepository;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;
import java.util.*;

@Service
public class GameMapService {

    @Autowired
    GameMapRepository gameMapRepository;

    @Autowired
    UserRepository userRepository;

    public GameMapService(GameMapRepository gameMapRepository, UserRepository userRepository) {
        this.gameMapRepository = gameMapRepository;
        this.userRepository = userRepository;
    }

    public List<GameMap> getAllGameMaps() {
        return gameMapRepository.findAll();
    }

    public GameMap getGameMapById(Long gameMapId) throws Exception {
        Optional<GameMap> optionalGameMap = gameMapRepository.findById(gameMapId);
        throwMapNotFound(optionalGameMap);
        return optionalGameMap.get();
    }

    public GameMap createNewGameMap(GameMapRequest gameMapRequest) throws Exception {
        throwMapNameOrBodyBlank(gameMapRequest);

        mapNameAlreadyExists(gameMapRequest.getName());

        List<List<Tile>> tiles = getTilesFromBody(gameMapRequest.getBody());

        GameMap gameMap = new GameMap(null, gameMapRequest.getName(), gameMapRequest.getHeight(), gameMapRequest.getWidth(), gameMapRequest.getBody(), tiles.toString(), gameMapRequest.getImagePath());
        gameMap = gameMapRepository.save(gameMap);
        return gameMap;
    }

    public GameMap updateMap(Long gameMapId, GameMapRequest gameMapRequest) throws Exception {
        throwMapNameOrBodyBlank(gameMapRequest);

        Optional<GameMap> optionalGameMap = gameMapRepository.findById(gameMapId);
        throwMapNotFound(optionalGameMap);

        mapNameAlreadyExists(gameMapRequest.getName());

        List<List<Tile>> tiles = getTilesFromBody(gameMapRequest.getBody());

        GameMap newGameMap = new GameMap(gameMapId, gameMapRequest.getName(), gameMapRequest.getHeight(), gameMapRequest.getWidth(), gameMapRequest.getBody(), tiles.toString(), gameMapRequest.getImagePath());
        gameMapRepository.save(newGameMap);

        return newGameMap;
    }

    public String deleteGameMapById(Long gameMapId) throws Exception {
        Optional<GameMap> optionalGameMap = gameMapRepository.findById(gameMapId);
        throwMapNotFound(optionalGameMap);

        gameMapRepository.deleteById(gameMapId);
        return ("Game map successfully deleted");
    }

    private List<List<Tile>> getTilesFromBody(String body) {
        List<List<Tile>> allTiles = new ArrayList<>();
        List<Tile> row = new ArrayList<>();
        for (int i = 0; i < body.length(); i++) {
            char index = body.charAt(i);
            switch (index) {
                case 'X' -> {
                    row.add(Tile.WALL);
                }
                case '0' -> {
                    row.add(Tile.FLOOR);
                }
                case '\n' -> {
                    allTiles.add(createCopyOfTileList(row));
                    row.clear();
                }
                default -> throw new TileNotFoundException();
            }
        }

        //  add the last row if the body does not end with a newline
        if (!row.isEmpty()) {
            allTiles.add(createCopyOfTileList(row));
        }
        return allTiles;
    }

    public List<Tile> createCopyOfTileList(List<Tile> originalList) {
        return new ArrayList<>(originalList);
    }


    public boolean mapNameAlreadyExists(String gameMapName) throws Exception {
        Optional<GameMap> optionalGameMap = gameMapRepository.findByNameIgnoreCase(gameMapName);
        if (optionalGameMap.isPresent()) {
            throw new MapNameAlreadyExistsException();
        }
        return false;
    }

    public void throwMapNameOrBodyBlank(GameMapRequest gameMapRequest) throws Exception {
        if (gameMapRequest.getName().isBlank() || gameMapRequest.getBody().isBlank()) {
            throw new MapNameOrBodyBlankException();
        }
    }

    private void throwMapNotFound(Optional<GameMap> gameMap) throws Exception {
        if (gameMap.isEmpty()) {
            throw new NameNotFoundException("Map not found");
        }
    }


}
