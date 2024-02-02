package br.todolist.todoapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TodoMapper implements RowMapper<Todo>   {
    @Override
    public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Todo todo = new Todo();

        todo.setId(rs.getInt("id"));
        todo.setDone(rs.getBoolean("done"));
        todo.setTask(rs.getString("task"));

        return todo;
    }
}
