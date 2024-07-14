package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.repositories.EventRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService implements IEventService {
    private final EventRepository eventRepository;
    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
    }
}
