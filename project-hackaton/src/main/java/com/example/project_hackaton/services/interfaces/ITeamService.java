package com.example.project_hackaton.services.interfaces;

import com.example.project_hackaton.entities.Teams;

import java.util.Optional;

public interface ITeamService {
    Optional<Teams> getTeamById(Long id);
}
