package br.todolist.todoapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TaskMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getInt("id"));
        task.setDone(rs.getBoolean("done"));
        task.setName(rs.getString("name"));
        task.setClientId(rs.getInt("client_id"));

        return task;
    }
}
