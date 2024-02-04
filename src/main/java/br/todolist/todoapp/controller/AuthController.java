package br.todolist.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.todolist.todoapp.model.Auth;
import br.todolist.todoapp.model.Client;
import br.todolist.todoapp.repository.ClientRepository;
import br.todolist.todoapp.security.services.JWTUtils;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    ClientRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtils jwtUtils;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo(@CookieValue("session") String session) {
        if (session == null) {
            return new ResponseEntity<>(Map.of(), HttpStatus.OK);
        }

        return new ResponseEntity<>(jwtUtils.decodePayloadToMap(session), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/register")
    public void save(@RequestBody @Valid Client client) {
        String password = passwordEncoder.encode(client.getPassword());

        repository.save(new Client(0, client.getEmail(), password));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        SecurityContextHolder.clearContext();

        HttpCookie cookie = ResponseCookie.from("session", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "user logged out"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid Auth payload) {
        var response = new HashMap<String, String>(Map.of("message", "user or password invalid"));

        String payloadEmail = payload.getEmail();
        String payloadPassword = payload.getPassword();

        Client client = repository.getByEmail(payloadEmail);

        if (client == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean isCorrectPassword = passwordEncoder.matches(payloadPassword, client.getPassword());

        if (!isCorrectPassword) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var token = new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword(), null);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtils.buildJwtToken(client);

        response.put("message", "user logged");

        HttpCookie cookie = ResponseCookie.from("session", jwtToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException error) {
        Map<String, String> response = new HashMap<>();

        error.getFieldErrors().forEach(e -> response.put(e.getField(), e.getDefaultMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handler(Exception error) {
        logger.info("AuthController.java: " + error.toString());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
