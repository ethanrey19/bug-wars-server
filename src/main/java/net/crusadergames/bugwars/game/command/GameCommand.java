package net.crusadergames.bugwars.game.command;

import net.crusadergames.bugwars.game.GameEntity;
import net.crusadergames.bugwars.model.GameMap;

import java.util.List;

public interface GameCommand {
    void execute(GameMap gameMap, GameEntity gameEntity, List<GameEntity> gameEntityList);
}
