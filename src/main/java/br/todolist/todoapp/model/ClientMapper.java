package br.todolist.todoapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ClientMapper implements RowMapper<Client> {
    private boolean bringPassword;

    public ClientMapper() {
        this.bringPassword = false;
    }

    public ClientMapper(boolean bringPassword) {
        this.bringPassword = bringPassword;
    }

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();

        client.setId(rs.getInt("id"));
        client.setEmail(rs.getString("email"));

        if (bringPassword)
            client.setPassword(rs.getString("password"));

        return client;
    }
}
