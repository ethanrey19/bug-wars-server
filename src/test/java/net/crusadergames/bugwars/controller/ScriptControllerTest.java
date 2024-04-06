package net.crusadergames.bugwars.controller;

import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.service.ScriptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class ScriptControllerTest {

    private final User USER = new User(UUID.randomUUID(), "jeff", "gmail@email.com", "passing");
    private final User USER_NEW = new User(UUID.randomUUID(), "andrew", "gmail@email.com", "passing");

    private final Script SCRIPT_1 = new Script(1L, "First Script", "I am a script", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER);
    private final Script SCRIPT_Uno = new Script(1L, "Old Script", "Old Food!", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER_NEW);
    private final Script SCRIPT_Dos = new Script(1L, "andrew", "Updated Script", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER_NEW);


    private ScriptService scriptService;
    private ScriptController scriptController;

    private Principal principal;
    private Principal principal_NEW;

    @BeforeEach
    public void beforeEachTest() {
        scriptService = Mockito.mock(ScriptService.class);
        scriptController = new ScriptController(scriptService);
        principal = Mockito.mock(Principal.class);
        principal_NEW = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USER.getUsername());
        when(principal_NEW.getName()).thenReturn(USER_NEW.getUsername());
    }

    @Test
    public void postScripts_shouldReturnCreatedScript() {
        ScriptRequest request = new ScriptRequest("First Script", "I am a Script");
        when(scriptService.createScript(request, USER.getUsername())).thenReturn(SCRIPT_1);

        ResponseEntity<Script> createdScript = scriptController.postScript(request, principal);

        Assertions.assertEquals(SCRIPT_1, createdScript.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, createdScript.getStatusCode());
    }

    @Test
    public void getAllScriptsByUser_shouldReturnAllScriptsFromUser() {
        List<Script> expectedScript = new ArrayList<>();
        expectedScript.add(SCRIPT_Uno);
        expectedScript.add(SCRIPT_Dos);
        when(scriptService.getAllScriptsByUser(USER_NEW.getUsername())).thenReturn(expectedScript);

        List<Script> listOfScripts = scriptController.getAllScriptsByUser(principal_NEW);

        Assertions.assertEquals(expectedScript, listOfScripts);
    }

    @Test
    public void getScript_shouldReturnUserScript() {
        when(scriptService.getScript(1L, principal.getName())).thenReturn(SCRIPT_1);

        ResponseEntity<Script> retrievedScript = scriptController.getScript(1L, principal);

        Assertions.assertEquals(SCRIPT_1, retrievedScript.getBody());
        Assertions.assertEquals(HttpStatus.OK, retrievedScript.getStatusCode());
    }

    @Test
    public void getScriptByName_shouldReturnUserScript() {
        when(scriptService.getScriptByName(SCRIPT_1.getName(), USER.getUsername())).thenReturn(SCRIPT_1);

        ResponseEntity<Script> retrievedScript = scriptController.getScriptByName(SCRIPT_1.getName(), principal);

        Assertions.assertEquals(SCRIPT_1, retrievedScript.getBody());
        Assertions.assertEquals(HttpStatus.OK, retrievedScript.getStatusCode());
    }


    @Test
    public void updateScript_shouldReturnUpdatedScript() {
        ScriptRequest request = new ScriptRequest("First Script", "I am a Script");
        when(scriptService.updateScript(request, 1L, USER.getUsername())).thenReturn(SCRIPT_1);

        ResponseEntity<Script> updatedScript = scriptController.updateScript(request, 1L, principal);

        Assertions.assertEquals(SCRIPT_1, updatedScript.getBody());
        Assertions.assertEquals(HttpStatus.ACCEPTED, updatedScript.getStatusCode());
    }

}
