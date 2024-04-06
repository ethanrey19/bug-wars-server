package net.crusadergames.bugwars.service;

import lombok.SneakyThrows;
import net.crusadergames.bugwars.dto.request.GameMapRequest;
import net.crusadergames.bugwars.exceptions.map.MapNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.map.MapNameOrBodyBlankException;
import net.crusadergames.bugwars.exceptions.map.TileNotFoundException;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.auth.ERole;
import net.crusadergames.bugwars.model.auth.Role;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.GameMapRepository;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameMapServiceTest {

    private final User ADMIN = new User(UUID.randomUUID(),"admin", "gmail@email.com", "passing", Set.of(new Role(3,
            ERole.ROLE_ADMIN)));

    private final GameMap MAP_1 = new GameMap(1L,"Map 1",5,5,"11111\n10001\n10101\n11011\n11111", "[]",
            "src/test");
    private final GameMap MAP_2 = new GameMap(2L,"Map 2",7,7, """
            1111111
            1000001
            1000001
            1000001
            1000001
            1000001
            1111111""", "[]", "src/test");
    private final GameMap OLD_MAP = new GameMap(1L,"Old Map",5,5,"11111\n10001\n10101\n11011\n11111"
            , "[]", "src/test");
    private GameMap NEW_MAP = new GameMap(3L,"I Am New",2,2,"XX\nXX", "[]",
            "src/test");

    private GameMapService gameMapService;
    private GameMapRepository gameMapRepository;
    private UserRepository userRepository;
    private final List<GameMap> allMaps = new ArrayList<>();

    @BeforeEach
    public void beforeEachTest(){

        gameMapRepository = Mockito.mock(GameMapRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        gameMapService = new GameMapService(gameMapRepository,userRepository);
        allMaps.add(MAP_1);
        allMaps.add(MAP_2);
    }

    @Test
    void getAllGameMaps_shouldReturnAllGameMaps() {
       GameMap map1 = new GameMap(1L,"Map 1",5,5,"11111\n10001\n10101\n11011\n11111", "[]", "src/test");
       GameMap map2 = new GameMap(2L,"Map 2",7,7, """
               1111111
               1000001
               1000001
               1000001
               1000001
               1000001
               1111111""", "[]", "src/test");
       List<GameMap> expected = new ArrayList<>();
       expected.add(map1);
       expected.add(map2);
       when(gameMapRepository.findAll()).thenReturn(allMaps);
       List<GameMap> maps = gameMapService.getAllGameMaps();

       Assertions.assertEquals(expected, maps);

    }

    @Test
    void getGameMapById_ReturnsCorrectMap() throws Exception {
        GameMap expected = new GameMap(1L,"Map 1",5,5,"11111\n10001\n10101\n11011\n11111", "[]", "src/test");

        when(gameMapRepository.findById(any())).thenReturn(Optional.of(MAP_1));

        GameMap actual = gameMapService.getGameMapById(MAP_1.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected, actual);

    }
    @Test
    void getGameMapById_ThrowsExceptionIfMapDoesNotExist() {
       Assert.assertThrows(Exception.class,()->{
           when(gameMapRepository.findById(any())).thenReturn(Optional.empty());
           gameMapService.getGameMapById(MAP_1.getId());
       });

    }

    @SneakyThrows
    @Test
    void createNewGameMap_ShouldReturnANewMap() {
        GameMapRequest gmRequest = new GameMapRequest("Map 1",5,5,"XXXXX\nX000X\nX0X0X\nXX0XX\nXXXXX", "src/test");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
        when(gameMapRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
        when(gameMapRepository.save(Mockito.any(GameMap.class))).thenReturn(MAP_1);
        //act
        GameMap createdMap = gameMapService.createNewGameMap(gmRequest);

        Assertions.assertNotNull(createdMap);
        Assertions.assertEquals(createdMap.getId(), MAP_1.getId());
        Assertions.assertEquals(createdMap, MAP_1);
    }

    @Test
    void createNewGameMap_ShouldThrowExceptionIfMapNameExists() {
        Assert.assertThrows(MapNameAlreadyExistsException.class,()->{
            GameMapRequest gmRequest = new GameMapRequest("Map 1",2,2,"11\n11", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            when(gameMapRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(MAP_1));
            gameMapService.createNewGameMap(gmRequest);
        });
    }
    @Test
    void createNewGameMap_ShouldThrowExceptionIfTitleOrBodyIsBlank() {
        Assert.assertThrows(MapNameOrBodyBlankException.class,()->{
            GameMapRequest gmRequest = new GameMapRequest("",2,2,"", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            gameMapService.createNewGameMap(gmRequest);
        });
    }

    @Test
    void createNewGameMap_ShouldThrowExceptionIfTileDoesNotExist() {
        Assert.assertThrows(TileNotFoundException.class,()->{
            GameMapRequest gmRequest = new GameMapRequest("test",2,2,"12\n12", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            gameMapService.createNewGameMap(gmRequest);
        });
    }

    @SneakyThrows
    @Test
    void updateMap_ShouldReturnNewMap() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
        when(gameMapRepository.findById(any())).thenReturn(Optional.of(NEW_MAP));
        GameMapRequest oldRequest = new GameMapRequest("Old Map",5,5,"XXXXX\nX000X\nX0X0X\nXX0XX\nXXXXX", "src/test");

        GameMap newMap = gameMapService.updateMap(3L, oldRequest);
        NEW_MAP = newMap;

        Assertions.assertNotNull(newMap);
        Assertions.assertEquals(newMap.getId(), NEW_MAP.getId());
        Assertions.assertEquals(newMap, NEW_MAP);
    }

    @Test
    void updateMap_ShouldThrowExceptionIfMapNameExists() {
        GameMap oldMap = OLD_MAP;
        Assert.assertThrows(MapNameAlreadyExistsException.class,()->{
            GameMapRequest newRequest = new GameMapRequest("Map 2",2,2,"XX\nXX", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            when(gameMapRepository.findById(any())).thenReturn(Optional.of(oldMap));
            when(gameMapRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(MAP_2));
            gameMapService.updateMap(oldMap.getId(), newRequest);
        });
    }
    @Test
    void updateMap_ShouldThrowExceptionIfTitleOrBodyIsBlank() {
        GameMap oldMap = OLD_MAP;
        Assert.assertThrows(MapNameOrBodyBlankException.class,()->{
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            when(gameMapRepository.findById(any())).thenReturn(Optional.of(oldMap));
            GameMapRequest newRequest = new GameMapRequest("",2,2,"", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            gameMapService.updateMap(oldMap.getId(), newRequest);
        });
    }

    @Test
    void updateMap_ShouldThrowExceptionIfTileDoesNotExist() {
        GameMap oldMap = OLD_MAP;
        Assert.assertThrows(TileNotFoundException.class,()->{
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            when(gameMapRepository.findById(any())).thenReturn(Optional.of(oldMap));
            GameMapRequest newRequest = new GameMapRequest("Test",2,2,"11\n12", "src/test");
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
            gameMapService.updateMap(oldMap.getId(), newRequest);
        });
    }

    @SneakyThrows
    @Test
    void deleteGameMapById_ShouldDeleteCorrectMap() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(ADMIN));
        when(gameMapRepository.findById(any())).thenReturn(Optional.of(MAP_1));

        gameMapService.deleteGameMapById(1L);

        verify(gameMapRepository, times(1)).deleteById(1L);
    }

    @Test
    void mapNameAlreadyExists_ShouldReturnFalseWhenGameMapWithSameNameDoesNotExist() throws Exception {
        when(gameMapRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());

        boolean actual = gameMapService.mapNameAlreadyExists("Map 100");
        Assertions.assertFalse(actual);
    }

}