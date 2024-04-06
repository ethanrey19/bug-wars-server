package net.crusadergames.bugwars.game;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.crusadergames.bugwars.dto.response.GameResponse;
import net.crusadergames.bugwars.game.command.*;
import net.crusadergames.bugwars.model.GameMap;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Game {

    private GameMap gameMap;

    private List<GameEntity> gameEntities;

    private int ticks;

    private final MoveCommand moveCommand = new MoveCommand();

    private final RotateRightCommand rotateRightCommand = new RotateRightCommand();

    private final RotateLeftCommand rotateLeftCommand = new RotateLeftCommand();

    private final NoopCommand noopCommand = new NoopCommand();

    public Game(GameMap gameMap, List<GameEntity> gameEntities, int ticks) {
        this.gameMap = gameMap;
        this.gameEntities = gameEntities;
        this.ticks = ticks;
    }

    public GameResponse play(){
        List<Tick> tickObjects = new ArrayList<>();
        Tick firstTick = new Tick(copyGameEntities(gameEntities));
        tickObjects.add(firstTick);
        for(int i = 0; i < ticks; i ++){
            List<GameEntity> newEntities = handleNextTick();

            List<GameEntity> copyEntities = copyGameEntities(newEntities);

            Tick tick = new Tick(copyEntities);
            tickObjects.add(tick);
        }

        return new GameResponse(gameMap, tickObjects);
    }

    private List<GameEntity> handleNextTick() {
        // read all game entities bytecode, update position off entity.

        for (GameEntity gameEntity : gameEntities) {
            if(gameEntity.getByteIndex() >= gameEntity.getBytecode().size()){
                continue;
            }

            int byteCode = gameEntity.getBytecode().get(gameEntity.getByteIndex());
            switch (byteCode) {
                case 0 -> {
                    noopCommand.execute(gameMap, gameEntity, gameEntities);
                    gameEntity.setByteIndex(gameEntity.getByteIndex() + 1);
                }
                case 10 -> {
                    moveCommand.execute(gameMap, gameEntity, gameEntities);
                    gameEntity.setByteIndex(gameEntity.getByteIndex() + 1);
                }
                case 11 -> {
                    rotateRightCommand.execute(gameMap, gameEntity, gameEntities);
                    gameEntity.setByteIndex(gameEntity.getByteIndex() + 1);
                }
                case 12 -> {
                    rotateLeftCommand.execute(gameMap, gameEntity, gameEntities);
                    gameEntity.setByteIndex(gameEntity.getByteIndex() + 1);
                }
            }
        }
        return gameEntities;
    }

    private List<GameEntity> copyGameEntities(List<GameEntity> newEntities){
        List<GameEntity> copyEntities = new ArrayList<>();
        for (GameEntity entity : newEntities) {
            copyEntities.add(new GameEntity(entity));
        }
        return copyEntities;
    }

}
