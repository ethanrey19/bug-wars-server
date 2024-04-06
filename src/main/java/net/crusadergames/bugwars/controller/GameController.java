package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.GameRequest;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/game")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/play")
    public ResponseEntity<GameResponse> playGame(@RequestBody GameRequest gameRequest, Principal principal) {
        GameResponse gameResponse = gameService.playGame(gameRequest, principal.getName());

        return new ResponseEntity<>(gameResponse, HttpStatus.OK);
    }

}
