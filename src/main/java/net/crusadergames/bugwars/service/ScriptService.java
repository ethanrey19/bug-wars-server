package net.crusadergames.bugwars.service;

import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.*;
import net.crusadergames.bugwars.model.Script;
import net.crusadergames.bugwars.model.auth.User;
import net.crusadergames.bugwars.repository.auth.UserRepository;
import net.crusadergames.bugwars.repository.script.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static net.crusadergames.bugwars.Util.Constants.RESPONSE_SCRIPTDELETED;

@Service
public class ScriptService {

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;


    public ScriptService(ScriptRepository scriptRepository, UserRepository userRepository){
        this.scriptRepository = scriptRepository;
        this.userRepository = userRepository;
    }

    public Script createNewScript(Long userId, ScriptRequest scriptRequest) {
        if (scriptRequest.getName().isBlank() || scriptRequest.getBody().isBlank()) {
            throw new ScriptSaveException();
        }

        Optional<Script> optionalScript = scriptRepository.findScriptByName(scriptRequest.getName());
        if (optionalScript.isPresent()) {
            throw new ScriptNameAlreadyExistsException();
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        throwUserNotFound(optionalUser);

        Script script = new Script(null, scriptRequest.getName(), scriptRequest.getBody(), LocalDate.now(), LocalDate.now(), optionalUser.get());

        script = scriptRepository.save(script);

        return script;
    }

    public String deleteScriptById(Long scriptId) {
        Optional<Script> optionalScript = scriptRepository.findById(scriptId);
        throwScriptNotPresent(optionalScript);
        scriptRepository.deleteById(scriptId);
        return RESPONSE_SCRIPTDELETED;
    }

    public Script getScript(Long scriptId) {
        Optional<Script> scriptOptional = scriptRepository.findById(scriptId);
        throwScriptNotPresent(scriptOptional);

        Script script = scriptOptional.get();

        return script;
    }

    public Script updateOldScript(ScriptRequest scriptRequest, Long scriptId) {
        Optional<Script> optionalScript = scriptRepository.findById(scriptId);

        throwScriptNotPresent(optionalScript);

        Script oldScript = optionalScript.get();

        LocalDate currentDate = LocalDate.now();
        Script newScript = new Script(scriptId, scriptRequest.getName(), scriptRequest.getBody(), oldScript.getDateCreated(), currentDate, oldScript.getUser());
        scriptRepository.save(newScript);

        return newScript;
    }

    public List<Script> getAllScriptsByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        throwUserNotFound(optionalUser);

        User user = optionalUser.get();
        return scriptRepository.findScriptsByUser(user);
    }

    public Script getScriptByName(String scriptName, Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Script> optionalScript = scriptRepository.findScriptByName(scriptName);

        throwUserNotFound(optionalUser);
        throwScriptNotPresent(optionalScript);

        User user = optionalUser.get();
        Script script = optionalScript.get();

        throwScriptDoesNotBelongToUser(user, script.getUser());

        return script;
    }

    private void throwUserNotFound(Optional<User> user) {
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
    }

    private void throwScriptNotPresent(Optional<Script> script) {
        if (script.isEmpty()) {
            throw new ScriptNotFoundException();
        }
    }

}