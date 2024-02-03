package br.todolist.todoapp.model;

import jakarta.validation.constraints.NotNull;

public class Task {
    private int id;

    @NotNull
    private String name;

    @NotNull
    private boolean done;

    public Task() {}

    public Task(int id, String name, boolean done) {
        this.id = id;
        this.done = done;
        this.name = name;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
