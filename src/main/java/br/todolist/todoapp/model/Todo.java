package br.todolist.todoapp.model;

public class Todo {
    private int id;
    private String task;
    private boolean done;

    public Todo() {}

    public Todo(int id, String task, boolean done) {
        this.id = id;
        this.done = done;
        this.task = task;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return this.task;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean getDone() {
        return this.done;
    }

    @Override
    public String toString() {
        return String.format("Todo{ id: %d, task: %s, done: %b }", id, task, done);
    }
}
