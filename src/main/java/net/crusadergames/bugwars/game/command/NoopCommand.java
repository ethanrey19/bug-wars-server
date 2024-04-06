package net.crusadergames.bugwars.game.command;

import net.crusadergames.bugwars.game.GameEntity;
import net.crusadergames.bugwars.model.GameMap;

import java.util.List;

public class NoopCommand implements GameCommand{
    @Override
    public void execute(GameMap gameMap, GameEntity gameEntity, List<GameEntity> gameEntityList) {
        // nothing.
    }
}
