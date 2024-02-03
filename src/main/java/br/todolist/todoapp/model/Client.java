package br.todolist.todoapp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Client {
    private int id;

    @NotNull
    private String email;
    
    @NotNull
    @Size(min = 8, max = 128)
    private String password;

    public Client () {}

    public Client(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
