package net.crusadergames.bugwars.exceptions.map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Tile with that ASCII is not found")
public class TileNotFoundException extends RuntimeException{
}
