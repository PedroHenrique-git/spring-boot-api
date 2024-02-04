package br.todolist.todoapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.model.Client;
import br.todolist.todoapp.model.Task;
import br.todolist.todoapp.repository.ClientRepository;
import br.todolist.todoapp.repository.TaskRepository;
import br.todolist.todoapp.security.services.JWTUtils;
import jakarta.validation.Valid;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ClientController {
    @Autowired
    ClientRepository repository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    JWTUtils jwtUtils;

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping("/clients/{id}")
    public ResponseEntity<List<Client>> get(@PathVariable(required = true) int id,
            @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        if (id != userId) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }

        List<Client> client = repository.get(id);

        if (client.isEmpty()) {
            return new ResponseEntity<>(client, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping("/clients/tasks/{id}")
    public ResponseEntity<List<Task>> getTasks(@PathVariable(required = true) int id,
            @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        if (id != userId) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }

        List<Task> tasks = taskRepository.getClientTasks(id);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<Void> update(@PathVariable(required = true) int id, @RequestBody @Valid Client client,
            @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        if (id != userId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        repository.update(id, client);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> delete(@PathVariable(required = true) int id, @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        if (id != userId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        repository.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException error) {
        Map<String, String> response = new HashMap<>();

        error.getFieldErrors().forEach(e -> response.put(e.getField(), e.getDefaultMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handler(Exception error) {
        logger.info("ClientController.java: " + error.toString());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
