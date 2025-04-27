package com.example.investi.Controllers;


import com.example.investi.Entities.Event;
import com.example.investi.Services.IEventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {
    @Autowired
    private IEventService iEventService;


    @PostMapping("create")
    public Event creatingEvent(@RequestBody Event Event){
        iEventService.addEvent(Event);
        return Event;

    }

    @GetMapping("all")
    public List<Event> getAllEvents(){
        return iEventService.getAllEvents();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> Event = iEventService.getEventById(id);
        return Event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /*@GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable String category) {
        return iEventService.getEventsByCategory(category);
    }*/

    @PutMapping("/update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event Event) {

        Event updatedEvent = iEventService.updateEvent(id, Event);
        return ResponseEntity.ok(updatedEvent);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {

        iEventService.deleteEvent(id);
        return ResponseEntity.noContent().build(); //
    }


}


