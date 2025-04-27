package com.example.investi.Services;

import com.example.investi.Entities.Client;
import com.example.investi.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServices {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public Client AddClient(Client client ) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client); }

   ;


        public Client updateClient(Client client) {
            // Vérifier si le client existe dans la base de données
            Client existingClient = clientRepository.findById(client.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + client.getId()));

            // Mettre à jour les champs modifiables
            if (client.getFirstName() != null) {
                existingClient.setFirstName(client.getFirstName());
            }
            if (client.getLastName() != null) {
                existingClient.setLastName(client.getLastName());
            }
            if (client.getEmail() != null) {
                existingClient.setEmail(client.getEmail());
            }
            if (client.getAdresse() != null) {
                existingClient.setAdresse(client.getAdresse());
            }
            if (client.getPhonenumber() != null) {
                existingClient.setPhonenumber(client.getPhonenumber());
            }
            if (client.getDateOfBirth() != null) {
                existingClient.setDateOfBirth(client.getDateOfBirth());
            }
            if (client.getGender() != null) {
                existingClient.setGender(client.getGender());
            }
            if (client.getProfession() != null) {
                existingClient.setProfession(client.getProfession());
            }
            if (client.getPreferredLanguage() != null) {
                existingClient.setPreferredLanguage(client.getPreferredLanguage());
            }
            if (client.getNationalId() != null) {
                existingClient.setNationalId(client.getNationalId());
            }

            // Sauvegarder les modifications dans la base de données
            return clientRepository.save(existingClient);
        }


        // Get a client by ID
        public Client getClientById(Long id) {
            return clientRepository.findById(id).get();
        }

        // Get all clients
        public List<Client> getAllClients() {
            return clientRepository.findAll();
        }

        // Delete a client by ID
        public void deleteClient(Long id) {
            clientRepository.deleteById(id);
        }
    }

