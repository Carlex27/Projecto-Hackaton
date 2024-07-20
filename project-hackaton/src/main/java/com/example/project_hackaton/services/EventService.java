package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.repositories.EventRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import com.example.project_hackaton.services.interfaces.IUserSearchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService implements IEventService {
    private final EventRepository eventRepository;
    private final IUserSearchService userService;
    //CRUD
    //CREATE
    public Event createEvent(String name, String description, String startDate,String endDate, Long creatorId) {
        User creator = userService.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + creatorId + " not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        Event event = Event.builder()
                .name(name)
                .description(description)
                .startDate(start)
                .endDate(end)
                .creator(creator)
                .build();
        return eventRepository.save(event);
    }
    //READ
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
    public Optional<Event> getEventByCreatorId(Long creatorId) {
        return Optional.ofNullable(eventRepository.findByCreatorId(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Event with creator id " + creatorId + " not found")));
    }

    //UPDATE
    public Event updateEvent(Long id, String name, String description, String startDate,String endDate) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        event.setName(name);
        event.setDescription(description);
        event.setStartDate(start);
        event.setEndDate(end);
        return eventRepository.save(event);
    }

    //DELETE
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

}
