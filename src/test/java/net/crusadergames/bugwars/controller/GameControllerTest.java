package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.dto.request.GameRequest;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.game.Tick;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class GameControllerTest {

    private final User USER = new User(UUID.randomUUID(), "jeff", "gmail@email.com", "passing");

    private final GameMap gameMap = new GameMap(1L, "Map 1", 11, 11, "XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\n" +
            "X000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X", "[[WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL]]",
            "src/assets/images/map-0.png");

    private final Script SCRIPT_1 = new Script(1L, "First Script", "att att att",
            List.of(10,10,10).toString(), LocalDate.now(), LocalDate.now(), USER);

    private GameController gameController;
    private GameService gameService;

    private Principal principal;

    @BeforeEach
    public void beforeEachTest() {
        gameService = Mockito.mock(GameService.class);
        gameController = new GameController(gameService);
        principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USER.getUsername());
    }

    @Test
    public void playGame_shouldReturnGameResponse(){
        GameRequest testRequest = new GameRequest(gameMap.getId(), List.of(SCRIPT_1.getScriptId(), SCRIPT_1.getScriptId(),
                SCRIPT_1.getScriptId(), SCRIPT_1.getScriptId()));
        GameResponse testResponse = new GameResponse(gameMap, List.of(new Tick(), new Tick()));
        when(gameService.playGame(testRequest, USER.getUsername())).thenReturn(testResponse);

        ResponseEntity<GameResponse> gameResponse = gameController.playGame(testRequest, principal);

        Assertions.assertEquals(testResponse, gameResponse.getBody());
        Assertions.assertEquals(HttpStatus.OK, gameResponse.getStatusCode());
    }

}
