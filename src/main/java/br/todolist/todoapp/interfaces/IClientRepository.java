package br.todolist.todoapp.interfaces;

import java.util.List;

import br.todolist.todoapp.model.Client;

public interface IClientRepository {
    List<Client> get(int id);
    Client getByEmail(String email);
    void save(Client client);
    void update(int id, Client client);
    void delete(int id);
}
