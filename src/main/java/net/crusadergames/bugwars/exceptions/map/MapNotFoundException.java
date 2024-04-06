package net.crusadergames.bugwars.exceptions.map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "GameMap with that id NOT found")
public class MapNotFoundException extends RuntimeException{
}
