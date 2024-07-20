package com.example.project_hackaton.services.interfaces;

import com.example.project_hackaton.entities.User;

import java.util.Optional;

public interface IUserSearchService {
    Optional<User> findById(Long id);
}
