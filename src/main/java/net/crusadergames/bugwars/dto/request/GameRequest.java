package net.crusadergames.bugwars.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    private UUID mapId;
    private List<UUID> scriptIds;
}
