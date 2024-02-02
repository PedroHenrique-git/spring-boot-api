package br.todolist.todoapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.todolist.todoapp.database.Database;
import br.todolist.todoapp.interfaces.ITodoRepository;
import br.todolist.todoapp.model.Todo;
import br.todolist.todoapp.model.TodoMapper;

@Repository
public class TodoRepository implements ITodoRepository {
    @Autowired Database database;

    private static final String SQL_GET_TODOS = "SELECT id, task, done FROM todo";
    private static final String SQL_GET_TODO = "SELECT id, task, done FROM todo WHERE id = ?";
    private static final String SQL_ADD_TODO = "INSERT INTO todo(task, done) VALUES(?, ?)";
    private static final String SQL_DELETE_TODO = "DELETE FROM todo WHERE id = ?";
    private static final String SQL_UPDATE_TODO = "UPDATE todo SET task = ?, done = ? WHERE id = ?";

    @Override
    public List<Todo> getTodos() {
        return database.getJdbc().query(SQL_GET_TODOS, new TodoMapper());
    }

    @Override
    public List<Todo> getTodo(int id) {
        return database.getJdbc().query(SQL_GET_TODO, new TodoMapper(), id);
    }

    @Override
    public void save(Todo todo) {
        database.getJdbc().update(SQL_ADD_TODO, todo.getTask(), todo.getDone());
    }

    @Override
    public void delete(int id) {
        database.getJdbc().update(SQL_DELETE_TODO, id);
    }

    @Override
    public void update(int id, Todo todo) {
        database.getJdbc().update(SQL_UPDATE_TODO, todo.getTask(), todo.getDone(), id);
    }
}
