package br.todolist.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.model.Task;
import br.todolist.todoapp.repository.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @Autowired TaskRepository repository;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
       List<Task> tasks = repository.getAll();
    
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<Task>> getTask(@PathVariable int id) {
        List<Task> task = repository.get(id);

        if(task.isEmpty()) {
            return new ResponseEntity<>(task, HttpStatus.NOT_FOUND);
        }
    
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/tasks")
    public void save(@RequestBody @Valid Task task) {
        logger.info("Create Todo: " + task.toString());

        repository.save(task);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping("/tasks/{id}")
    public void update(@PathVariable int id, @RequestBody @Valid Task task) {
        repository.update(id, task);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/tasks/{id}")
    public void delete(@PathVariable int id) {
        repository.delete(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException error) {
        Map<String, String> response = new HashMap<>();

        error.getFieldErrors().forEach(e -> 
            response.put(e.getField(), e.getDefaultMessage())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handler(Exception error) {
        Map<String, String> response = new HashMap<>();

        response.put("message", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
