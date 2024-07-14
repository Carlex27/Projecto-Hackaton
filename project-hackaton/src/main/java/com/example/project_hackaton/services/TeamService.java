package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.entities.Teams;
import com.example.project_hackaton.repositories.TeamsRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamsRepository teamsRepository;
    private final IEventService eventService;
    
    public Teams createTeam(String name, Long eventId) {
        Event event = eventService.getEventById(eventId);
        Teams team = Teams.builder()
                .name(name)
                .event(event)
                .build();
        return teamsRepository.save(team);
    }

    public Teams getTeamById(Long id) {
        return teamsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + id + " not found"));
    }

    public List<Teams> getAllTeams() {
        return teamsRepository.findAll();
    }

    public List<Teams> getAllTeamsByEventId(Long eventId) {
        return teamsRepository.findAllByEventId(eventId);
    }

    public Teams getTeamByNameAndEventId(String name, Long eventId) {
        return teamsRepository.findByNameAndEventId(name, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Team with name " + name + " and event id " + eventId + " not found"));
    }

    public void deleteTeam(Long id) {
        teamsRepository.deleteById(id);
    }
}
