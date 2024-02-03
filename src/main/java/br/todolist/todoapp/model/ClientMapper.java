package br.todolist.todoapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ClientMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();

        client.setId(rs.getInt("id"));
        client.setEmail(rs.getString("email"));
        client.setPassword(rs.getString("password"));

        return client;
    }
}
