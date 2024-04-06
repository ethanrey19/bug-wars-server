package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.*;
import net.crusadergames.bugwars.exceptions.script.ScriptDoesNotBelongToUserException;
import net.crusadergames.bugwars.exceptions.script.ScriptNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.script.ScriptNotFoundException;
import net.crusadergames.bugwars.exceptions.script.ScriptSaveException;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScriptServiceTest {
    private final User USER = new User(UUID.randomUUID(), "jeff", "gmail@email.com", "passing");
    private final User USER_2 = new User(UUID.randomUUID(), "mac", "gmail@email.com", "passing");
    private final User USER_NEW = new User(UUID.randomUUID(), "andrew", "gmail@email.com", "passing");
    private final User USER_FAKE = new User();

    private final Script SCRIPT_1 = new Script(1L, "First Script", "mov", List.of(10).toString(), LocalDate.now(), LocalDate.now(), USER);
    private final Script SCRIPT_2 = new Script(2L, "Second Script", "Eat chicken", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER_2);
    private final Script SCRIPT_Old = new Script(1L, "Old Script", "Old Food!", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER_NEW);
    private final Script SCRIPT_NEW = new Script(1L, "andrew", "Updated Script", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER_NEW);


    private ScriptService scriptService;
    private ParserService parserService;
    private ScriptRepository scriptRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEachTest() {
        userRepository = Mockito.mock(UserRepository.class);
        scriptRepository = Mockito.mock(ScriptRepository.class);
        parserService = new ParserService();
        scriptService = new ScriptService(scriptRepository, userRepository, parserService);
    }

    @Test
    public void createScript_shouldReturnCreatedScript() {
        ScriptRequest request = new ScriptRequest("First Script", "att");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(USER));
        when(scriptRepository.findByName(any())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(USER);
        when(scriptRepository.save(Mockito.any(Script.class))).thenReturn(SCRIPT_1);

        Script createdScript = scriptService.createScript(request, USER.getUsername());

        Assert.assertNotNull(createdScript);
        Assert.assertEquals(createdScript.getScriptId(), SCRIPT_1.getScriptId());
        Assert.assertEquals(createdScript, SCRIPT_1);
    }


    @Test
    public void createScript_shouldReturnScriptNameExistsExceptionWhenRelevant() {
        Assert.assertThrows(ScriptNameAlreadyExistsException.class, () -> {
            ScriptRequest request = new ScriptRequest("First Script", "I am a Script");
            when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(USER));
            when(scriptRepository.findByName(any())).thenReturn(Optional.of(SCRIPT_1));

            scriptService.createScript(request, USER.getUsername());
        });
    }

    @Test
    public void createScript_shouldThrowScriptSaveErrorWhenTitleIsNull() {
        Assert.assertThrows(ScriptSaveException.class, () -> {
            ScriptRequest request = new ScriptRequest("", "I am a script");
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(USER_FAKE));
            when(scriptRepository.findByName(any())).thenReturn(Optional.empty());
            when(userRepository.save(Mockito.any(User.class))).thenReturn(USER);

            scriptService.createScript(request, USER.getUsername());
        });
    }

    @Test
    void getScript_shouldReturnCorrectScript() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

        Script script = scriptService.getScript(1L, USER.getUsername());

        Assert.assertNotNull(SCRIPT_1);
        Assert.assertEquals(script.getScriptId(), SCRIPT_1.getScriptId());
        Assert.assertEquals(script, SCRIPT_1);
    }

    @Test
    void getScript_shouldThrowScriptNotFoundException() {
        Assert.assertThrows(ScriptNotFoundException.class, () -> {
            when(scriptRepository.findById(any())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER_2));

            scriptService.getScript(1L, USER_2.getUsername());
        });
    }

    @Test
    void getScriptByName_shouldReturnCorrectScript() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(scriptRepository.findByName(any())).thenReturn(Optional.of(SCRIPT_1));

        Script script = scriptService.getScriptByName(SCRIPT_1.getName(), USER.getUsername());

        Assert.assertNotNull(SCRIPT_1);
        Assert.assertEquals(script.getScriptId(), SCRIPT_1.getScriptId());
        Assert.assertEquals(script, SCRIPT_1);
    }

    @Test
    void getScriptByName_shouldThrowScriptNotFoundException() {
        Assert.assertThrows(ScriptNotFoundException.class, () -> {
            when(scriptRepository.findByName(any())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER_2));

            scriptService.getScriptByName(SCRIPT_1.getName(), USER_2.getUsername());
        });
    }

    @Test
    void getAllScriptsByUser_shouldReturnListOfScriptsUnderAUser() {
        Script script1 = new Script(1L, "Script 1", "Script 1", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER);
        Script script2 = new Script(2L, "Script 2", "Script 2", List.of(10, 10, 10).toString(), LocalDate.now(), LocalDate.now(), USER);

        List<Script> expected = new ArrayList<>();
        expected.add(script1);
        expected.add(script2);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(scriptRepository.findByUser(USER)).thenReturn(expected);

        List<Script> script = scriptService.getAllScriptsByUser(USER.getUsername());

        Assert.assertEquals(expected, script);
    }

    @Test
    void getAllScriptsByUser_shouldThrowUserNotFoundException() {
        Assert.assertThrows(UserNotFoundException.class, () -> {
            when(userRepository.findById(any())).thenReturn(Optional.empty());
            when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

            scriptService.getAllScriptsByUser("fake username");
        });
    }

    @Test
    void updateOldScript_shouldReturnNewScript() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

        ScriptRequest requestOld = new ScriptRequest("andrew", "att\n att");
        ScriptRequest requestNew = new ScriptRequest("First Script", "mov");
        scriptService.createScript(requestOld, USER.getUsername());

        Script script = scriptService.updateScript(requestNew, 1L, USER.getUsername());

        Assert.assertNotNull(script);
        Assert.assertEquals(script.getScriptId(), SCRIPT_1.getScriptId());
        Assert.assertEquals(script, SCRIPT_1);
    }

    @Test
    void updateOldScript_shouldThrowScriptNotFoundException() {
        Assert.assertThrows(ScriptNotFoundException.class, () -> {
            when(scriptRepository.findById(any())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(USER_2));

            ScriptRequest requestNew = new ScriptRequest("andrew", "att att att");
            scriptService.updateScript(requestNew, 1L, USER_2.getUsername());
        });
    }

    @Test
    void deleteScriptById_shouldDeleteScriptById() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));
        when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

        scriptService.deleteScriptById(1L, USER.getUsername());

        verify(scriptRepository, times(1)).deleteById(1L);

    }

    @Test
    void deleteScriptById_shouldThrowScriptNotFoundException() {
        Assert.assertThrows(ScriptNotFoundException.class, () -> {
            when(scriptRepository.findById(any())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER));

            scriptService.deleteScriptById(1L, USER.getUsername());
        });
    }

    @Test
    void throwScriptDoesNotBelongToUser_shouldThrowScriptDoesNotBelongToUserException() {
        Assert.assertThrows(ScriptDoesNotBelongToUserException.class, () -> {
            when(userRepository.findByUsername(any())).thenReturn(Optional.of(USER_2));
            when(scriptRepository.findById(any())).thenReturn(Optional.of(SCRIPT_1));

            scriptService.throwScriptDoesNotBelongToUser(USER_2, SCRIPT_1.getUser());
        });
    }

}

