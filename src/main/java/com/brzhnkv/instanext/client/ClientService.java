package com.brzhnkv.instanext.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientDataAccessService clientDataAccessService;

    @Autowired
    public ClientService(ClientDataAccessService clientDataAccessService) {
        this.clientDataAccessService = clientDataAccessService;
    }

    public List<Client> getAllClients() {
        return clientDataAccessService.selectAllClients();
    }

    public Optional<Client> getClientByUsernameAndToken(String username, String token) {
        return clientDataAccessService.selectClientByUsernameAndToken(username, token);
    }

    public void addNewClient(Client client) {
        addNewClient(null, client);
    }

    public void addNewClient(UUID clientId, Client client) {
        UUID newClientId = Optional.ofNullable(clientId)
                .orElse(UUID.randomUUID());

        clientDataAccessService.insertClient(newClientId, client);
    }

    public void deleteClient(String username) {
        clientDataAccessService.deleteClientByUsername(username);
    }
}