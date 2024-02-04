package br.todolist.todoapp.model;

import jakarta.validation.constraints.NotNull;

public class Auth {
    @NotNull
    private String email;
    
    @NotNull
    private String password;
    
    public Auth() {}

    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("Auth{ email: %s, password: %s }", email, password);
    }
}
