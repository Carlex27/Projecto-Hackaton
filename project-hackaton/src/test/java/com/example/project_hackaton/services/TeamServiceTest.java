package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.Event;
import com.example.project_hackaton.entities.Teams;
import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.repositories.TeamsRepository;
import com.example.project_hackaton.services.interfaces.IEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock
    private TeamsRepository teamsRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private TeamService teamService;

    private Teams team;
    private Event event;
    @BeforeEach
    void setUp() {
        crearEvento();
        team = Teams.builder()
                .id(1L)
                .name("test")
                .event(event)
                .build();
    }

    private void crearEvento() {
        event = Event.builder()
                .id(1L)
                .name("test")
                .description("test")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .creator(User.builder()
                        .id(1L)
                        .username("test")
                        .email("test@test.com")
                        .password("test")
                        .role("USER")
                        .build())
                .build();
    }

    @Test
    void createTeam() {

    }

    @Test
    void testCreateTeam() {
    }

    @Test
    void updateTeam() {
    }

    @Test
    void testUpdateTeam() {
    }

    @Test
    void getTeamByName() {
    }

    @Test
    void getTeamById() {
    }

    @Test
    void getAllTeams() {
    }

    @Test
    void getAllTeamsByEventId() {
    }

    @Test
    void getTeamByNameAndEventId() {
    }

    @Test
    void deleteTeam() {
    }

    @Test
    void testDeleteTeam() {
    }

    @Test
    void testDeleteTeam1() {
    }
}