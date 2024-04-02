package net.crusadergames.bugwars.service;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.*;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScriptService {

    private final ScriptRepository scriptRepository;

    private final UserRepository userRepository;


    public Script createScript(ScriptRequest scriptRequest, String username) {
        if (scriptRequest.getName().isBlank() || scriptRequest.getBody().isBlank()) {
            throw new ScriptSaveException();
        }

        Optional<Script> optionalScript = scriptRepository.findByName(scriptRequest.getName());
        if (optionalScript.isPresent()) {
            throw new ScriptNameAlreadyExistsException();
        }

        User user = getUserFromUsername(username);

        Script script = new Script(null, scriptRequest.getName(), scriptRequest.getBody(), LocalDate.now(), LocalDate.now(), user);

        script = scriptRepository.save(script);

        return script;
    }

    public List<Script> getAllScriptsByUser(String username) {
        User user = getUserFromUsername(username);
        return scriptRepository.findByUser(user);
    }

    public Script getScript(Long scriptId, String username) {
        User user = getUserFromUsername(username);
        Script script = getScriptFromId(scriptId);

        throwScriptDoesNotBelongToUser(user, script.getUser());

        return script;
    }

    public Script getScriptByName(String scriptName, String username) {
        User user = getUserFromUsername(username);
        Optional<Script> optionalScript = scriptRepository.findByName(scriptName);

        if (optionalScript.isEmpty()) {
            throw new ScriptNotFoundException();
        }

        Script script = optionalScript.get();

        throwScriptDoesNotBelongToUser(user, script.getUser());

        return script;
    }

    public Script updateScript(ScriptRequest scriptRequest, Long scriptId, String username) {
        Script oldScript = getScriptFromId(scriptId);

        User user = getUserFromUsername(username);
        throwScriptDoesNotBelongToUser(user, oldScript.getUser());

        LocalDate currentDate = LocalDate.now();
        Script newScript = new Script(scriptId, scriptRequest.getName(), scriptRequest.getBody(), oldScript.getDateCreated(), currentDate, oldScript.getUser());
        scriptRepository.save(newScript);

        return newScript;
    }

    public void deleteScriptById(Long scriptId, String username) {
        User user = getUserFromUsername(username);
        Script script = getScriptFromId(scriptId);

        throwScriptDoesNotBelongToUser(user, script.getUser());

        scriptRepository.deleteById(scriptId);
    }

    private User getUserFromUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private Script getScriptFromId(Long id) {
        return scriptRepository.findById(id)
                .orElseThrow(() -> new ScriptNotFoundException());
    }

    public void throwScriptDoesNotBelongToUser(User user, User scriptUser) {
        if (!user.getId().equals(scriptUser.getId())) {
            throw new ScriptDoesNotBelongToUserException();
        }
    }

}