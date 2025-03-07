package com.example.investiprojet.repositories;

import com.example.investiprojet.entities.Client;
import com.example.investiprojet.entities.Event;
import com.example.investiprojet.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ParticipationRepository extends JpaRepository<Participation,Long> {
    List<Participation> findByClient(Client client);

    Optional<Participation> findByClientAndEvent(Client client, Event event);
}
