package net.crusadergames.bugwars.service;

import lombok.RequiredArgsConstructor;
import net.crusadergames.bugwars.dto.request.ScriptRequest;
import net.crusadergames.bugwars.exceptions.*;
import net.crusadergames.bugwars.exceptions.parser.SyntaxException;
import net.crusadergames.bugwars.exceptions.script.ScriptDoesNotBelongToUserException;
import net.crusadergames.bugwars.exceptions.script.ScriptNameAlreadyExistsException;
import net.crusadergames.bugwars.exceptions.script.ScriptNotFoundException;
import net.crusadergames.bugwars.exceptions.script.ScriptSaveException;
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

    private final ParserService parserService;


    public Script createScript(ScriptRequest scriptRequest, String username) {
        if (scriptRequest.getName().isBlank() || scriptRequest.getBody().isBlank()) {
            throw new ScriptSaveException();
        }

        Optional<Script> optionalScript = scriptRepository.findByName(scriptRequest.getName());
        if (optionalScript.isPresent()) {
            throw new ScriptNameAlreadyExistsException();
        }

        User user = getUserFromUsername(username);

        List<Integer> bytecode;
        try{
            bytecode = parserService.returnByteCode(scriptRequest.getBody());
        } catch (SyntaxException e) {
            throw new SyntaxException(e.getMessage());
        }

        System.out.println(bytecode.toString());
        Script script = new Script(null, scriptRequest.getName(), scriptRequest.getBody(), bytecode.toString(), LocalDate.now(), LocalDate.now(), user);

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

        List<Integer> bytecode;
        try{
            bytecode = parserService.returnByteCode(scriptRequest.getBody());
        } catch (SyntaxException e) {
            throw new SyntaxException(e.getMessage());
        }

        LocalDate currentDate = LocalDate.now();
        Script newScript = new Script(scriptId, scriptRequest.getName(), scriptRequest.getBody(), bytecode.toString(), oldScript.getDateCreated(), currentDate, oldScript.getUser());
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
                .orElseThrow(UserNotFoundException::new);
    }

    private Script getScriptFromId(Long id) {
        return scriptRepository.findById(id)
                .orElseThrow(ScriptNotFoundException::new);
    }

    public void throwScriptDoesNotBelongToUser(User user, User scriptUser) {
        if (!user.getId().equals(scriptUser.getId())) {
            throw new ScriptDoesNotBelongToUserException();
        }
    }

}