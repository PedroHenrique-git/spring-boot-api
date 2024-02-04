package br.todolist.todoapp.interfaces;

import java.util.List;

import br.todolist.todoapp.model.Task;

public interface ITaskRepository {
    List<Task> getAll();
    List<Task> get(int id);
    void save(Task todo);
    void delete(int id);
    void update(int id, Task todo);
}
