package br.todolist.todoapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.todolist.todoapp.database.Database;
import br.todolist.todoapp.interfaces.IClientRepository;
import br.todolist.todoapp.model.Client;
import br.todolist.todoapp.model.ClientMapper;

@Repository
public class ClientRepository implements IClientRepository {
    @Autowired
    Database database;

    private static final String SQL_GET_CLIENT = "SELECT id, email FROM client WHERE id = ?";
    private static final String SQL_GET_CLIENT_BY_EMAIL = "SELECT id, email, password FROM client WHERE email = ?";
    private static final String SQL_ADD_CLIENT = "INSERT INTO client(email, password) VALUES (?, ?)";
    private static final String SQL_UPDATE_CLIENT = "UPDATE client SET email = ?, password = ? where id = ?";
    private static final String SQL_DELETE_CLIENT = "DELETE FROM client WHERE id = ?";

    @Override
    public List<Client> get(int id) {
        return database.getJdbc().query(SQL_GET_CLIENT, new ClientMapper(), id);
    }

    @Override
    public void delete(int id) {
        database.getJdbc().update(SQL_DELETE_CLIENT, id);
    }

    @Override
    public void save(Client client) {
        database.getJdbc().update(SQL_ADD_CLIENT, client.getEmail(), client.getPassword());
    }

    @Override
    public void update(int id, Client client) {
        database.getJdbc().update(SQL_UPDATE_CLIENT, client.getEmail(), client.getPassword());
    }

    @Override
    public Client getByEmail(String email) {
        List<Client> result = database.getJdbc().query(SQL_GET_CLIENT_BY_EMAIL, new ClientMapper(true),
                email);

        return result.stream().findFirst().orElse(null);
    }
}
