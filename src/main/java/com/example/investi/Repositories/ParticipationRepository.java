package com.example.investi.Repositories;


import com.example.investi.Entities.Client;
import com.example.investi.Entities.Event;
import com.example.investi.Entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ParticipationRepository extends JpaRepository<Participation,Long> {
    List<Participation> findByClient(Client client);

    Optional<Participation> findByClientAndEvent(Client client, Event event);
}
