package br.todolist.todoapp;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class Index {

    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "It works";
    }

    @GetMapping(path = "/*", produces = "application/json")
    public ResponseEntity<Map<String, String>> defaultRoute() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("message", "route not found");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
}
