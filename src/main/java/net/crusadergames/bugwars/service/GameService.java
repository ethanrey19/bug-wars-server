package net.crusadergames.bugwars.service;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.GameRequest;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.exceptions.GameMapNotFoundException;
import net.crusadergames.bugwars.exceptions.ScriptNotFoundException;
import net.crusadergames.bugwars.game.Direction;
import net.crusadergames.bugwars.game.Game;
import net.crusadergames.bugwars.game.GameEntity;
import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.repository.GameMapRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameMapRepository gameMapRepository;

    private final ScriptRepository scriptRepository;

    public GameResponse playGame(GameRequest gameRequest) {
        GameMap gameMap = getGameMapFromId(gameRequest.getMapId());
        List<GameEntity> gameEntities = createBugs(gameRequest.getScriptIds());

        Game game = new Game(gameMap, gameEntities, 2);
        return game.play();
    }

    public List<GameEntity> createBugs(List<Long> scriptIds){
        List<GameEntity> gameEntities = new ArrayList<>();
        for (Long scriptId: scriptIds){
            Script script = getScriptFromId(scriptId);
            List<Integer> testBytecode = new ArrayList<>();
            testBytecode.add(10);
            testBytecode.add(10);
            testBytecode.add(11);
            testBytecode.add(10);
            testBytecode.add(10);
            GameEntity gameEntity = new GameEntity(script.getName(), testBytecode, Direction.NORTH, new Point(0,0), true, 0);
            gameEntities.add(gameEntity);
        }
        return gameEntities;
    }

    private GameMap getGameMapFromId(Long id) {
        return gameMapRepository.findById(id)
                .orElseThrow(GameMapNotFoundException::new);
    }

    private Script getScriptFromId(Long id) {
        return scriptRepository.findById(id)
                .orElseThrow(ScriptNotFoundException::new);
    }

}
