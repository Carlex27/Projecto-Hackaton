package com.example.project_hackaton.service;

import com.example.project_hackaton.entity.Event;
import com.example.project_hackaton.entity.Teams;
import com.example.project_hackaton.repository.TeamsRepository;
import com.example.project_hackaton.service.interfaces.IEventService;
import com.example.project_hackaton.service.interfaces.ITeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService implements ITeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);
    private final TeamsRepository teamsRepository;
    private final IEventService eventService;

    //CRUD

    //CREATE
    public Teams createTeam(String teamName, String eventName){
        log.info("Creating team with name: {} in the event name: {}", teamName, eventName);
        Optional<Event> event = eventService.getEventByName(eventName);
        if(event.isEmpty()){
            throw new EntityNotFoundException("Event with name " + eventName + " not found");
        }
        Teams team = Teams.builder()
                .name(teamName)
                .event(event.get())
                .build();
        return teamsRepository.save(team);
    }

    public Teams createTeam(String teamName, Long eventId) {
        log.info("Creating team with name: {} in the event id: {}", teamName, eventId);
        Optional<Event> eventOptional = eventService.getEventById(eventId);

        if(eventOptional.isEmpty()){
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }

        Event event = eventOptional.get();
        Teams team = Teams.builder()
                .name(teamName)
                .event(event)
                .build();
        return teamsRepository.save(team);
    }

    //READ
    public Optional<Teams> getTeamByName(String name){
        log.info("Finding team by name: {}", name);
        return Optional.ofNullable(teamsRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Team with name " + name + " not found")));
    }
    public Optional<Teams> getTeamById(Long id) {
        log.info("Finding team by id: {}", id);
        return Optional.ofNullable(teamsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + id + " not found")));
    }

    public List<Teams> getAllTeams() {
        log.info("Finding all teams");
        return teamsRepository.findAll();
    }

    public List<Teams> getAllTeamsByEventId(Long eventId) {
        log.info("Finding all teams by event id: {}", eventId);
        return teamsRepository.findAllByEventId(eventId);
    }

    public Optional<Teams> getTeamByNameAndEventId(String name, Long eventId) {
        log.info("Finding team by name: {} and event id: {}", name, eventId);
        return Optional.ofNullable(teamsRepository.findByNameAndEventId(name, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Team with name " + name + " and event id " + eventId + " not found")));
    }

    //UPDATE
    public Teams updateTeam(Long id, Teams updateTeam){
        Teams existingTeam = teamsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found id: " + id));
        existingTeam.setName(updateTeam.getName());
        existingTeam.setEvent(updateTeam.getEvent());
        log.info("Updating team with id: {}",id);
        return teamsRepository.save(existingTeam);
    }

    public Teams updateTeam(String name, Teams updateTeam){
        Long idTeam = teamsRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Team with name " + name + " not found"))
                .getId();
        return updateTeam(idTeam, updateTeam);
    }

    //DELETE
    public void deleteTeam(Long id) {
        log.warn("Deleting team with id: {}", id);
        teamsRepository.deleteById(id);
    }
    public void deleteTeam(String name){
        log.warn("Deleting team with name: {}", name);
        teamsRepository.deleteByName(name);
    }
    public void deleteTeam(String name, Long eventId){
        log.warn("Deleting team with name: {} and event id: {}", name, eventId);
        teamsRepository.deleteByNameAndEventId(name, eventId);
    }

}
