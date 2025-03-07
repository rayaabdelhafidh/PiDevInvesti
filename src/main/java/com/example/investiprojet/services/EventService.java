package com.example.investiprojet.services;


import com.example.investiprojet.entities.Event;
import com.example.investiprojet.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    @Autowired
    private EventRepository eventRepository;
    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        // Find the existing training by ID
        Optional<Event> existingEvent = eventRepository.findById(id);

        if (existingEvent.isPresent()) {
            // Retrieve the existing training entity
            Event existingevent = existingEvent.get();

            // Update the fields of the existing training entity
            existingevent.setTitle(event.getTitle());
            existingevent.setDescription(event.getDescription());
            existingevent.setEventType(event.getEventType());
            existingevent.setStartDate(event.getStartDate());
            existingevent.setEndDate(event.getEndDate());
            existingevent.setMaxParticipants(event.getMaxParticipants());


            // Save and return the updated training entity
            return eventRepository.save(existingevent);
        }
        return null;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);

    }
}

