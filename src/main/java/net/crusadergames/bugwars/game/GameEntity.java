package net.crusadergames.bugwars.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    private String name;

    @JsonIgnore
    private List<Integer> bytecode;

    private Direction direction;

    private Point location;

    private boolean isAlive;

    @JsonIgnore
    private int byteIndex;

    public GameEntity(GameEntity gameEntity) {
        this.name = gameEntity.name;
        this.bytecode = new ArrayList<>(gameEntity.bytecode);
        this.direction = gameEntity.direction;
        this.location = new Point(gameEntity.location.x, gameEntity.location.y);
        this.isAlive = gameEntity.isAlive;
        this.byteIndex = gameEntity.byteIndex;
    }

    @JsonIgnore
    public Point getForwardLocation() {
        int futureX = location.x;
        int futureY = location.y;
        switch (direction) {
            case NORTH -> {
                futureY--;
            }
            case EAST -> {
                futureX++;
            }
            case SOUTH -> {
                futureY++;
            }
            case WEST -> {
                futureX--;
            }
        }
        return new Point(futureX, futureY);
    }

    @JsonIgnore
    public Direction getRotateRight() {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

    @JsonIgnore
    public Direction getRotateLeft() {
        return switch (direction) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
    }
}
