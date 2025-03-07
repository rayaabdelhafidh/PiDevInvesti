package com.example.investiprojet.repositories;

import com.example.investiprojet.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {
}
