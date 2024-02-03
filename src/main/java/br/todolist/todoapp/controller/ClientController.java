package br.todolist.todoapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.common.Encoder;
import br.todolist.todoapp.model.Client;
import br.todolist.todoapp.repository.ClientRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ClientController {
    @Autowired ClientRepository repository;
    @Autowired Encoder encoder;

    @GetMapping("/clients/{id}")
    public ResponseEntity<List<Client>> get(@RequestParam int id) {
        List<Client> client = repository.get(id);

        if(client.isEmpty()) {
            return new ResponseEntity<>(client, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.NO_CONTENT);
    }
    
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/clients")
    public void save(@RequestBody @Valid Client client) {
        String password = encoder.passwordEncoder().encode(client.getPassword());

        repository.save(new Client(0, client.getEmail(), password));
    }
    

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping("/clients/{id}")
    public void update(@PathVariable int id, @RequestBody @Valid Client client) {
        repository.update(id, client);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/clients/{id}")
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