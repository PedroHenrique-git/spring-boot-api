package br.todolist.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.model.Todo;
import br.todolist.todoapp.repository.TodoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class TodoController {
    @Autowired TodoRepository repository;

    Logger logger = LoggerFactory.getLogger(TodoController.class);

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
       List<Todo> todos = repository.getTodos();
    
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<List<Todo>> getTodo(@PathVariable int id) {
        List<Todo> todo = repository.getTodo(id);

        if(todo.isEmpty()) {
            return new ResponseEntity<>(todo, HttpStatus.NOT_FOUND);
        }
    
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/todos")
    public void save(@RequestBody Todo todo) {
        logger.info("Create Todo: " + todo.toString());

        repository.save(todo);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping("/todos/{id}")
    public void update(@PathVariable int id, @RequestBody Todo todo) {
        repository.update(id, todo);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/todos/{id}")
    public void delete(@PathVariable int id) {
        repository.delete(id);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handler(Exception error) {
        logger.error("Error handler TodoController.java: " + error.getMessage());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
