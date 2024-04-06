package net.crusadergames.bugwars.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.crusadergames.bugwars.game.Tick;
import net.crusadergames.bugwars.model.GameMap;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private GameMap gameMap;

    private List<Tick> ticks;

}
