package br.todolist.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.model.Task;
import br.todolist.todoapp.repository.TaskRepository;
import br.todolist.todoapp.security.services.JWTUtils;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class TaskController {
    @Autowired
    TaskRepository repository;
    @Autowired
    JWTUtils jwtUtils;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<Task>> getTask(@PathVariable(required = true) int id,
            @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        List<Task> task = repository.get(id);

        if (task.isEmpty()) {
            return new ResponseEntity<>(task, HttpStatus.NOT_FOUND);
        }

        if (task.get(0).getClientId() != userId) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/tasks")
    public void save(@RequestBody @Valid Task task) {
        repository.save(task);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Void> update(@PathVariable(required = true) int id, @RequestBody @Valid Task task,
            @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        List<Task> t = repository.get(id);

        if (t.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (t.get(0).getClientId() != userId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        repository.update(id, task);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable(required = true) int id, @CookieValue("session") String session) {
        var user = jwtUtils.decodePayloadToMap(session);
        var userId = Integer.parseInt(user.get("id"));

        List<Task> t = repository.get(id);

        if (t.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (t.get(0).getClientId() != userId) {
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
        logger.info("TaskController.java: " + error.toString());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
