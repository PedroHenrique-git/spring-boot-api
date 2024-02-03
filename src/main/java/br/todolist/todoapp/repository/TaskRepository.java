package br.todolist.todoapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.todolist.todoapp.database.Database;
import br.todolist.todoapp.interfaces.ITaskRepository;
import br.todolist.todoapp.model.Task;
import br.todolist.todoapp.model.TaskMapper;

@Repository
public class TaskRepository implements ITaskRepository {
    @Autowired Database database;

    private static final String SQL_GET_TODOS = "SELECT id, name, done FROM task";
    private static final String SQL_GET_TODO = "SELECT id, name, done FROM task WHERE id = ?";
    private static final String SQL_ADD_TODO = "INSERT INTO task(name, done) VALUES(?, ?)";
    private static final String SQL_DELETE_TODO = "DELETE FROM task WHERE id = ?";
    private static final String SQL_UPDATE_TODO = "UPDATE task SET name = ?, done = ? WHERE id = ?";

    @Override
    public List<Task> getAll() {
        return database.getJdbc().query(SQL_GET_TODOS, new TaskMapper());
    }

    @Override
    public List<Task> get(int id) {
        return database.getJdbc().query(SQL_GET_TODO, new TaskMapper(), id);
    }

    @Override
    public void save(Task todo) {
        database.getJdbc().update(SQL_ADD_TODO, todo.getName(), todo.getDone());
    }

    @Override
    public void delete(int id) {
        database.getJdbc().update(SQL_DELETE_TODO, id);
    }

    @Override
    public void update(int id, Task todo) {
        database.getJdbc().update(SQL_UPDATE_TODO, todo.getName(), todo.getDone(), id);
    }
}
