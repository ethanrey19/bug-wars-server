package net.crusadergames.bugwars.game.command;

import net.crusadergames.bugwars.game.GameEntity;
import net.crusadergames.bugwars.model.GameMap;

import java.awt.*;
import java.util.List;

public class MoveCommand implements GameCommand {

    @Override
    public void execute(GameMap gameMap, GameEntity gameEntity, List<GameEntity> gameEntityList) {
        Point forwardLocation = gameEntity.getForwardLocation();
        if (isTileUnoccupied(forwardLocation, gameEntityList)) {
            gameEntity.setLocation(forwardLocation);
        }
    }

    private boolean isTileUnoccupied(Point inputLocation, List<GameEntity> gameEntityList) {
        return gameEntityList.stream().noneMatch(entity -> entity.getLocation().equals(inputLocation));
    }

    private boolean isFloor(Point point, GameMap gameMap) {
        String body = "" +
                "XXXXXXXXXXX\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "X000000000X\n" +
                "XXXXXXXXXXX";

        int width = gameMap.getWidth();
        int height = gameMap.getHeight();
        int x = point.x;
        int y = point.y;

        System.out.println(body.charAt(y * height));
        return false;
    }
}
