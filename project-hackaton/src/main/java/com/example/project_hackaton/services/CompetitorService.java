package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Competitor;
import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.repositories.CompetitorRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import com.example.project_hackaton.services.interfaces.IUserSearchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompetitorService {
    private final CompetitorRepository competitorRepository;
    private final IUserSearchService userService;
    private final IEventService eventService;
    //CRUD

    //CREATE
    public Competitor createCompetitor(Long userId, Long eventId){

        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " not found"));


        Competitor competitor = Competitor.builder()
                .user(user)
                .event(event)
                .build();

        return competitorRepository.save(competitor);
    }

    //READ
    public Competitor getCompetitorById(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competitor with id " + id + " not found"));
    }
    public List<Competitor> getCompetitorsByUserId(Long userId) {
        return competitorRepository.findAllByUserId(userId);
    }
    public List<Competitor> getCompetitorsByEventId(Long eventId) {
        return competitorRepository.findAllByEventId(eventId);
    }
    public Competitor getCompetitorByUserIdAndEventId(Long userId, Long eventId) {
        return competitorRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Competitor with user id " + userId + " and event id " + eventId + " not found"));
    }
    //UPDATE


    //DELETE
    public void deleteCompetitor(Long id) {
        competitorRepository.deleteById(id);
    }
    public void deleteCompetitorByUserIdAndEventId(Long userId, Long eventId) {
        Competitor competitor = getCompetitorByUserIdAndEventId(userId, eventId);
        competitorRepository.delete(competitor);
    }

}
