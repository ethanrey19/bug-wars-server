package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.GameRequest;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.GameMapRepository;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    private final User USER = new User(UUID.randomUUID(), "jeff", "gmail@email.com", "passing");

    private final GameMap GAMEMAP = new GameMap(1L, "Map 1", 11, 11, "XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\n" +
            "X000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X", "[[WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], " +
            "[WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL], [WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL]]",
            "src/assets/images/map-0.png");

    private final Script SCRIPT_1 = new Script(1L, "First Script", "att\n att\n att",
            List.of(10,10,10).toString(), LocalDate.now(), LocalDate.now(), USER);

    private GameMapRepository gameMapRepository;
    private ScriptRepository scriptRepository;
    private UserRepository userRepository;
    private GameService gameService;

    @BeforeEach
    public void beforeEachTest() {
        gameMapRepository = Mockito.mock(GameMapRepository.class);
        scriptRepository = Mockito.mock(ScriptRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        gameService = new GameService(gameMapRepository, scriptRepository, userRepository);
    }

    @Test
    public void playGame_shouldReturnGameResponse() {
        GameRequest testRequest = new GameRequest(GAMEMAP.getId(), List.of(SCRIPT_1.getScriptId(), SCRIPT_1.getScriptId(),
                SCRIPT_1.getScriptId(), SCRIPT_1.getScriptId()));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(USER));
        when(gameMapRepository.findById(any())).thenReturn(Optional.of(GAMEMAP));
        when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

        GameResponse gameResponse = gameService.playGame(testRequest, USER.getUsername());

        Assertions.assertNotNull(gameResponse);
    }

}
