package net.crusadergames.bugwars.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapEntityRequest {

    @Size(max = 25)
    private String name;

    private String entityCode;

    private String imagePath;

}
