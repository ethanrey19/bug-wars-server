package net.crusadergames.bugwars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.crusadergames.bugwars.controller.ScriptController;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.*;
import net.crusadergames.bugwars.model.SampleString;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import net.crusadergames.bugwars.service.ScriptService;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private Principal mockPrincipal;
    private final User USER = new User(1L,"jeff", "gmail@email.com", "passing");
    private final User USER_2 = new User(2L,"mac", "gmail@email.com", "passing");

    @Test
    public void postScript_ReturnBadRequestIfInvalidRequest() throws Exception {
        SampleString string = SampleString.builder().content("polar bear").build();
        when(scriptService.createNewScript(any(), any())).thenThrow(new ScriptSaveException());

        mockMvc.perform(post("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(string)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postScript_ReturnUserNotFoundException() throws Exception {
        SampleString string = SampleString.builder().content("polar bear").build();
        when(scriptService.createNewScript(any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(string)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postScript_ReturnConflictException() throws Exception {
        SampleString string = SampleString.builder().content("polar bear").build();
        when(scriptService.createNewScript(any(), any())).thenThrow(new ScriptNameAlreadyExistsException());

        mockMvc.perform(post("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(string)))
                .andExpect(status().isConflict());
    }

    @Test
    public void postScript_ReturnResponseIfValidRequest() throws Exception {
        ScriptRequest request = new ScriptRequest("First Script", "I am a Script");
        Script scriptRes = new Script(1L, "John Doe", "This is a test script", LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.createNewScript(mockPrincipal, request)).thenReturn(scriptRes);

        ResultActions response = mockMvc.perform(post("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(scriptRes)));
    }

    @Test
    public void deleteScript_ReturnScriptDeletedIfSuccess() throws Exception {
        when(scriptService.deleteScriptById(any(), any())).thenReturn("Script Deleted");

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/scripts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Script Deleted"));
    }

    @Test
    public void deleteScript_ReturnScriptNotFoundException() throws Exception {
        when(scriptService.deleteScriptById(any(), any())).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/scripts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteScript_ReturnUserNotFoundException() throws Exception {
        when(scriptService.deleteScriptById(any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/scripts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteScript_ReturnScriptDoesNotBelongToUserException() throws Exception {
        when(scriptService.deleteScriptById(any(), any())).thenThrow(new ScriptDoesNotBelongToUserException());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/scripts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUserScript_ReturnUserNotFoundException() throws Exception {
        when(scriptService.getScript(any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserScript_ReturnScriptNotFoundException() throws Exception {
        when(scriptService.getScript(any(), any())).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserScript_ScriptDoesNotBelongToUserException() throws Exception {
        when(scriptService.getScript(any(), any())).thenThrow(new ScriptDoesNotBelongToUserException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUserScript_ReturnScriptIfSuccess() throws Exception {
        Script scriptRes = new Script(1L, "John Doe", "This is a test script", LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.getScript(1L, mockPrincipal)).thenReturn(scriptRes);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(scriptRes)));
    }

    @Test
    public void getAllUserScripts_ReturnScriptsIfSuccess() throws Exception {
        Script script1 = new Script(1L, "John Doe", "This is a test script", LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        Script script2 = new Script(2L, "John Doe", "This is a test script", LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER_2);
        List<Script> expectedScript = new ArrayList<>();
        expectedScript.add(script1);
        expectedScript.add(script2);

        when(scriptService.getAllScriptsByUser(mockPrincipal)).thenReturn(expectedScript);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedScript)));
    }

    @Test
    public void getAllUserScripts_ReturnUserNotFoundException() throws Exception {
        when(scriptService.getAllScriptsByUser(mockPrincipal)).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/scripts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateScript_ReturnResponseIfValidRequest() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        Script scriptRes = new Script(1L, "Updated Script", "This is a test script", LocalDate.parse("2024-01-23"), LocalDate.parse("2024-01-24"), USER);
        when(scriptService.updateOldScript(mockPrincipal, request, 1L)).thenReturn(scriptRes);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/scripts/{scriptId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void updateScript_ReturnScriptNotFoundException() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        when(scriptService.updateOldScript(any(), any(), any())).thenThrow(new ScriptNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateScript_ReturnUserNotFoundException() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        when(scriptService.updateOldScript(any(), any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateScript_ScriptDoesNotBelongToUserException() throws Exception {
        ScriptRequest request = new ScriptRequest("Updated Script", "I am a Script");
        when(scriptService.updateOldScript(any(), any(), any())).thenThrow(new ScriptDoesNotBelongToUserException());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/scripts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}