package com.example.investiprojet.services;

import com.example.investiprojet.entities.Client;
import com.example.investiprojet.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements IClientService{

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }
}
