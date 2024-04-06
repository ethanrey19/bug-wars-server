package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.dto.request.GameMapRequest;
import net.crusadergames.bugwars.exceptions.NotAnAdminException;
import net.crusadergames.bugwars.exceptions.map.MapNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.map.MapNameOrBodyBlankException;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.auth.ERole;
import net.crusadergames.bugwars.model.auth.Role;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.service.GameMapService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GameMapControllerIT {

    private final User USER = new User(UUID.randomUUID(),"user", "gmail@email.com", "passing", Set.of(new Role(1, ERole.ROLE_USER)));

    private final GameMap MAP_1 = new GameMap(1L, "Map 1", 11, 11, """
            XXXXXXXXXXX
            X000010000X
            X000000000X
            X000000000X
            X000000000X
            X000000000X
            X000000000X
            X000000000X
            X000000000X
            X000000000X""", "[[WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL]]",
            "src/assets/images/map-0.png");
    private final GameMap MAP_2 = new GameMap(2L,"Map 2",7,7, """
            1111111
            1000001
            1000001
            1000001
            1000001
            1000001
            1111111""", "[[WALL]]", "src/main/test");
    private final GameMap NEW_MAP = new GameMap(1L,"I Am New",2,2, "11\n11", "[]", "src/test");

    private GameMapService gameMapService;
    private GameMapController gameMapController;
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEachTest() {
        gameMapService = Mockito.mock(GameMapService.class);
        gameMapController = new GameMapController(gameMapService);
        userRepository = Mockito.mock(UserRepository.class);
    }

    @Test
    public void getAllGameMapsShouldReturnAllMaps() {
        List<GameMap> expectedGameMaps = new ArrayList<>();
        expectedGameMaps.add(MAP_1);
        expectedGameMaps.add(MAP_2);
        when(gameMapService.getAllGameMaps()).thenReturn(expectedGameMaps);

        List<GameMap> listOfGameMaps = gameMapController.getAllGameMaps();

        Assertions.assertEquals(expectedGameMaps, listOfGameMaps);
    }

    @Test
    public void getGameMapByIdShouldReturnCorrectGameMap() throws Exception{
        when(gameMapService.getGameMapById(1L)).thenReturn(MAP_1);

        GameMap retrievedMap = gameMapController.getGameMapById(1L);

        Assertions.assertEquals(MAP_1, retrievedMap);
    }

    @Test
    public void getGameMapByIdShouldThrowExceptionIfMapDoesNotExist() throws Exception{
        when(gameMapController.getGameMapById(any())).thenThrow(new Exception("Map name not found"));
        Assert.assertThrows(Exception.class, () -> gameMapController.getGameMapById(3L));
    }

    @Test
    public void postGameMapShouldReturnCreatedMap() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(gameMapService.createNewGameMap(request)).thenReturn(NEW_MAP);

        ResponseEntity<GameMap> createdGameMap = gameMapController.postGameMap(request);

        Assertions.assertEquals(NEW_MAP, createdGameMap.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, createdGameMap.getStatusCode());
    }

    @Test
    public void postGameMapShouldThrowExceptionIfNotAdmin() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(gameMapController.postGameMap(any())).thenThrow(new NotAnAdminException());
        Assert.assertThrows(NotAnAdminException.class, () -> gameMapController.postGameMap(request));
    }

    @Test
    public void postGameMapShouldThrowExceptionIfMapNameAlreadyExists() throws Exception {
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(gameMapController.postGameMap(request)).thenThrow(new MapNameAlreadyExistsException());
        Assert.assertThrows(MapNameAlreadyExistsException.class, () -> gameMapController.postGameMap(request));
    }

    @Test
    public void postGameMapShouldThrowExceptionIfTitleOrBodyIsEmpty() throws Exception {
        GameMapRequest requestEmptyTitle = new GameMapRequest("",2,2,"11\n11", "src/test");
        GameMapRequest requestEmptyBody = new GameMapRequest("I Am New",2,2,"", "src/test");
        when(gameMapController.postGameMap(requestEmptyTitle)).thenThrow(new MapNameOrBodyBlankException());
        when(gameMapController.postGameMap(requestEmptyBody)).thenThrow(new MapNameOrBodyBlankException());
        Assert.assertThrows(MapNameOrBodyBlankException.class, () -> gameMapController.postGameMap(requestEmptyTitle));
        Assert.assertThrows(MapNameOrBodyBlankException.class, () -> gameMapController.postGameMap(requestEmptyBody));
    }

    @Test
    public void updateGameMapShouldReturnUpdatedGameMap() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(gameMapService.updateMap(1L, request)).thenReturn(NEW_MAP);

        ResponseEntity<GameMap> updatedGameMap = gameMapController.updateGameMap(1L, request);

        Assertions.assertEquals(NEW_MAP, updatedGameMap.getBody());
        Assertions.assertEquals(HttpStatus.ACCEPTED, updatedGameMap.getStatusCode());
    }

    @Test
    public void updateGameMapShouldThrowExceptionIfNotAdmin() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(gameMapService.updateMap(1L, request)).thenThrow(new NotAnAdminException());
        Assert.assertThrows(NotAnAdminException.class, () -> gameMapController.updateGameMap(1L, request));
    }

    @Test
    public void updateGameMapShouldThrowExceptionIfTitleOrBodyIsEmpty() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(gameMapService.updateMap(1L, request)).thenThrow(new MapNameOrBodyBlankException());
        Assert.assertThrows(MapNameOrBodyBlankException.class, () -> gameMapController.updateGameMap(1L, request));
    }

    @Test
    public void updateGameMapShouldThrowExceptionIfMapNameAlreadyExists() throws Exception{
        GameMapRequest request = new GameMapRequest("I Am New",2,2,"11\n11", "src/test");
        when(gameMapService.updateMap(1L, request)).thenThrow(new MapNameAlreadyExistsException());
        Assert.assertThrows(MapNameAlreadyExistsException.class, () -> gameMapController.updateGameMap(1L, request));
    }

    @Test
    public void deleteGameMapShouldReturnDeletedGameMapDeletedScriptMessage() throws Exception{
        when(gameMapService.deleteGameMapById(1L)).thenReturn("Game map successfully deleted");

        ResponseEntity<String> deletedGameMap = gameMapController.deleteGameMap(1L);

        Assertions.assertEquals("Game map successfully deleted", deletedGameMap.getBody());
        Assertions.assertEquals(HttpStatus.OK, deletedGameMap.getStatusCode());
    }

    @Test
    public void deleteGameMapShouldThrowExceptionIfNotAdmin() throws Exception{
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(gameMapService.deleteGameMapById(1L)).thenThrow(new NotAnAdminException());
        Assert.assertThrows(NotAnAdminException.class, () -> gameMapController.deleteGameMap(1L));
    }

}
