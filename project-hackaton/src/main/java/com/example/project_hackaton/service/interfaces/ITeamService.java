package com.example.project_hackaton.service.interfaces;

import com.example.project_hackaton.entity.Teams;

import java.util.Optional;

public interface ITeamService {
    Optional<Teams> getTeamById(Long id);
}
