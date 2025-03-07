package com.example.investiprojet.controllers;

import com.example.investiprojet.entities.Client;
import com.example.investiprojet.entities.Event;
import com.example.investiprojet.services.IClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private IClientService iClientService;

    @PostMapping("create")
    public Client creatingClient(@RequestBody Client client){
        iClientService.addClient(client);
        return client;

    }
}
