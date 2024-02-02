package br.todolist.todoapp.interfaces;

import java.util.List;

import br.todolist.todoapp.model.Todo;

/**
 * TodoRepository
 */
public interface ITodoRepository {
    List<Todo> getTodos();
    List<Todo> getTodo(int id);
    void save(Todo todo);
    void delete(int id);
    void update(int id, Todo todo);
}
