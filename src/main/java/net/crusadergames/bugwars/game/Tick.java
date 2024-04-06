package net.crusadergames.bugwars.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tick {

    private List<GameEntity> gameEntities;

}
