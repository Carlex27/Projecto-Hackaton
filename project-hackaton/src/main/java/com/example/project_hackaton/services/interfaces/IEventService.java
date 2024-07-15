package com.example.project_hackaton.services.interfaces;

import com.example.project_hackaton.entities.Event;

import java.util.Optional;

public interface IEventService {
    Optional<Event> getEventById(Long id);
    Optional<Event> getEventByName(String name);
}
