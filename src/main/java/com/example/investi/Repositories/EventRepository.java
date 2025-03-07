package com.example.investi.Repositories;


import com.example.investi.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {
    //List<Event> findByEventDate(LocalDate date);

}
