package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.GameRequest;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.service.GameService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/play")
    public GameResponse play(@RequestBody GameRequest gameRequest) {
        return gameService.playGame(gameRequest);
    }

}
