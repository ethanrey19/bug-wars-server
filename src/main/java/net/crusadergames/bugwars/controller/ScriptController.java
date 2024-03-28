package net.crusadergames.bugwars.controller;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.model.Script;


import net.crusadergames.bugwars.service.ScriptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/scripts")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ScriptController {


    private final ScriptService scriptService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user-scripts/{userId}")
    public ResponseEntity<Script> postScript(@RequestBody ScriptRequest scriptRequest, @PathVariable Long userId) {
        Script script = scriptService.createNewScript(userId, scriptRequest);



        return new ResponseEntity<>(script, HttpStatus.CREATED);
    }

    @DeleteMapping("/{scriptId}")
    public ResponseEntity<String> deleteScript(@PathVariable Long scriptId) {
        String response = scriptService.deleteScriptById(scriptId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{scriptId}")
    public ResponseEntity<Script> getUserScript(@PathVariable Long scriptId) {
        Script script = scriptService.getScript(scriptId);

        return new ResponseEntity<>(script, HttpStatus.OK);
    }

    @GetMapping("/user-scripts/{userId}")
    public List<Script> getAllScriptsByUser(@PathVariable Long userId) {
        return scriptService.getAllScriptsByUser(userId);
    }

    @GetMapping("/name/{scriptName}")
    public ResponseEntity<Script> getScriptByName(@PathVariable String scriptName, Principal principal){
        Script script = scriptService.getScriptByName(scriptName, principal);

        return new ResponseEntity<>(script, HttpStatus.OK);
    }

    @PutMapping("/{scriptId}")
    public ResponseEntity<Script> updateScript(@RequestBody ScriptRequest scriptRequest, @PathVariable Long scriptId) {
        Script script = scriptService.updateOldScript(scriptRequest, scriptId);

        return new ResponseEntity<>(script, HttpStatus.ACCEPTED);
    }
}