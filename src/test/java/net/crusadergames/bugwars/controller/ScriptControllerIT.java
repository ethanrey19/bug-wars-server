package net.crusadergames.bugwars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.script.ScriptNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.script.ScriptNotFoundException;
import net.crusadergames.bugwars.exceptions.script.ScriptSaveException;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import net.crusadergames.bugwars.service.ScriptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScriptController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ScriptControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScriptService scriptService;

    @MockBean
    private ScriptRepository scriptRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Principal principal;

    private final User USER = new User(UUID.randomUUID(), "jeff", "gmail@email.com", "passing");

    @BeforeEach
    void beforeEach() {
        principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USER.getUsername());
    }

    @Test
    public void postScript_ReturnBadRequestIfInvalidRequest() throws Exception {
        ScriptRequest request = new ScriptRequest("First Script", "ATT");
        when(scriptService.createScript(any(), any())).thenThrow(new ScriptSaveException());

        mockMvc.perform(post("/api/scripts")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postScript_ReturnConflictException() throws Exception {
        ScriptRequest request = new ScriptRequest("First Script", "ATT");
        when(scriptService.createScript(any(), any())).thenThrow(new ScriptNameAlreadyExistsException());

        mockMvc.perform(post("/api/scripts")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    public void postScript_ReturnResponseIfValidRequest() throws Exception {
        ScriptRequest request = new ScriptRequest("First Script", "ATT");
        Script scriptRes = new Script(1L, "John Doe", "att\n att\n att", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.createScript(request, USER.getUsername())).thenReturn(scriptRes);

        ResultActions response = mockMvc.perform(post("/api/scripts")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(scriptRes)));
    }

    @Test
    public void getAllScriptsByUser_ReturnEmptyArrayIfNoScriptsFound() throws Exception {
        List<Script> expectedScript = new ArrayList<>();

        when(scriptService.getAllScriptsByUser(USER.getUsername())).thenReturn(expectedScript);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/all")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedScript)));
    }

    @Test
    public void getAllScriptsByUser_ReturnScriptsIfSuccess() throws Exception {
        Script script1 = new Script(1L, "John Doe", "This is a test script", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        Script script2 = new Script(2L, "John Doe", "This is a test script", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        List<Script> expectedScript = new ArrayList<>();
        expectedScript.add(script1);
        expectedScript.add(script2);

        when(scriptService.getAllScriptsByUser(USER.getUsername())).thenReturn(expectedScript);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/all")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedScript)));
    }

    @Test
    public void getScript_ReturnScriptNotFoundException() throws Exception {
        when(scriptService.getScript(any(), eq(USER.getUsername()))).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/{id}", 2)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getScript_ReturnScriptIfSuccess() throws Exception {
        Script scriptRes = new Script(1L, "John Doe", "This is a test script", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.getScript(1L, USER.getUsername())).thenReturn(scriptRes);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(scriptRes)));
    }

    @Test
    public void getScriptByName_ReturnScriptNotFoundException() throws Exception {
        when(scriptService.getScriptByName(any(), eq(USER.getUsername()))).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/name/{scriptName}", USER.getUsername())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getScriptByName_ReturnScriptIfSuccess() throws Exception {
        Script scriptRes = new Script(1L, "John Doe", "This is a test script", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.getScriptByName(USER.getUsername(), USER.getUsername())).thenReturn(scriptRes);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/scripts/name/{scriptName}", USER.getUsername())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(scriptRes)));
    }

    @Test
    public void updateScript_ReturnResponseIfValidRequest() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        Script scriptRes = new Script(1L, "Updated Script", "This is a test script", List.of(10, 10, 10).toString(),
                LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.updateScript(request, 1L, USER.getUsername())).thenReturn(scriptRes);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/scripts/{scriptId}", "1")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void updateScript_ReturnScriptNotFoundException() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        when(scriptService.updateScript(any(), any(), any())).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/scripts/{id}", 2)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteScript_ReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/scripts/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}