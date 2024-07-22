package com.example.project_hackaton.service.interfaces;

import com.example.project_hackaton.entity.Event;

import java.util.Optional;

public interface IEventService {
    Optional<Event> getEventById(Long id);
    Optional<Event> getEventByName(String name);
}
