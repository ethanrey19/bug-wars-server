package net.crusadergames.bugwars.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    private Long mapId;
    private List<Long> scriptIds;

}
