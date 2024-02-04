package br.todolist.todoapp.model;

import jakarta.validation.constraints.NotNull;

public class Task {
    private int id;

    @NotNull
    private String name;

    @NotNull
    private boolean done;

    @NotNull
    private int clientId;

    public Task() {
    }

    public Task(int id, String name, boolean done) {
        this.id = id;
        this.done = done;
        this.name = name;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean getDone() {
        return this.done;
    }

    @Override
    public String toString() {
        return String.format("Todo{ id: %d, name: %s, done: %b }", id, name, done);
    }
}
