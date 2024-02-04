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
    @Autowired
    Database database;

    private static final String SQL_GET_TODOS = "SELECT id, name, done, client_id FROM task";
    private static final String SQL_GET_TODO = "SELECT id, name, done, client_id FROM task WHERE id = ?";
    private static final String SQL_ADD_TODO = "INSERT INTO task(name, done, client_id) VALUES(?, ?, ?)";
    private static final String SQL_DELETE_TODO = "DELETE FROM task WHERE id = ?";
    private static final String SQL_UPDATE_TODO = "UPDATE task SET name = ?, done = ?, client_id = ? WHERE id = ?";
    private static final String SQL_GET_TASK_BY_CLIENT_ID = "SELECT t.id as id, t.name as name, t.done as done, t.client_id as client_id FROM task t INNER JOIN client c ON t.client_id = c.id WHERE c.id = ?";

    @Override
    public List<Task> getAll() {
        return database.getJdbc().query(SQL_GET_TODOS, new TaskMapper());
    }

    @Override
    public List<Task> get(int id) {
        return database.getJdbc().query(SQL_GET_TODO, new TaskMapper(), id);
    }

    @Override
    public void save(Task task) {
        database.getJdbc().update(SQL_ADD_TODO, task.getName(), task.getDone(), task.getClientId());
    }

    @Override
    public List<Task> getClientTasks(int id) {
        return database.getJdbc().query(SQL_GET_TASK_BY_CLIENT_ID, new TaskMapper(), id);
    }

    @Override
    public void delete(int id) {
        database.getJdbc().update(SQL_DELETE_TODO, id);
    }

    @Override
    public void update(int id, Task task) {
        database.getJdbc().update(SQL_UPDATE_TODO, task.getName(), task.getDone(), task.getClientId(), id);
    }
}
