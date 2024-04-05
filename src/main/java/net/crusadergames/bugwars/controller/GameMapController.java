package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.GameMapRequest;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.service.GameMapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GameMapController {

    private final GameMapService gameMapService;

    @GetMapping()
    public List<GameMap> getAllGameMaps() {
        return gameMapService.getAllGameMaps();
    }

    @GetMapping("/{gameMapId}")
    public GameMap getGameMapById(@PathVariable Long gameMapId) throws Exception {
        return gameMapService.getGameMapById(gameMapId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GameMap> postGameMap(@RequestBody GameMapRequest gameMapRequest) throws Exception{
        GameMap gameMap = gameMapService.createNewGameMap(gameMapRequest);
        return new ResponseEntity<>(gameMap, HttpStatus.CREATED);
    }

    @PutMapping("/{gameMapId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GameMap> updateGameMap(@PathVariable Long gameMapId, @RequestBody GameMapRequest gameMapRequest) throws Exception{
        GameMap gameMap = gameMapService.updateMap(gameMapId, gameMapRequest);
        return new ResponseEntity<>(gameMap, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{gameMapId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteGameMap(@PathVariable Long gameMapId) throws Exception {
        String response = gameMapService.deleteGameMapById(gameMapId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
