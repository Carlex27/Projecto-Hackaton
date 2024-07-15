package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.repositories.EventRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService implements IEventService {
    private final EventRepository eventRepository;
    @Override
    public Optional<Event> getEventById(Long id) {
        return Optional.ofNullable(eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found")));
    }

    @Override
    public Optional<Event> getEventByName(String name) {
        return Optional.ofNullable(eventRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Event with name " + name + " not found")));
    }

}
