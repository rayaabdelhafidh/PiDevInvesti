package com.example.investi.Services;


import com.example.investi.Entities.Event;

import java.util.List;
import java.util.Optional;

public interface IEventService {
    Event addEvent(Event event);
    List<Event> getAllEvents();
    Optional<Event> getEventById(Long id);

    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
}

