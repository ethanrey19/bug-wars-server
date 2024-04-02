package net.crusadergames.bugwars.controller;

import jakarta.validation.Valid;
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
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/scripts")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ScriptController {


    private final ScriptService scriptService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<Script> postScript(@Valid @RequestBody ScriptRequest scriptRequest, Principal principal) {
        Script script = scriptService.createScript(scriptRequest, principal.getName());

        return new ResponseEntity<>(script, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Script> getAllScriptsByUser(Principal principal) {
        return scriptService.getAllScriptsByUser(principal.getName());
    }

    @GetMapping("/{scriptId}")
    public ResponseEntity<Script> getScript(@PathVariable Long scriptId, Principal principal) {
        Script script = scriptService.getScript(scriptId, principal.getName());

        return new ResponseEntity<>(script, HttpStatus.OK);
    }

    @GetMapping("/name/{scriptName}")
    public ResponseEntity<Script> getScriptByName(@PathVariable String scriptName, Principal principal) {
        Script script = scriptService.getScriptByName(scriptName, principal.getName());

        return new ResponseEntity<>(script, HttpStatus.OK);
    }

    @PutMapping("/{scriptId}")
    public ResponseEntity<Script> updateScript(@RequestBody ScriptRequest scriptRequest, @PathVariable Long scriptId, Principal principal) {
        Script script = scriptService.updateScript(scriptRequest, scriptId, principal.getName());

        return new ResponseEntity<>(script, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{scriptId}")
    public void deleteScript(@PathVariable Long scriptId, Principal principal) {
        scriptService.deleteScriptById(scriptId, principal.getName());
    }
}