package com.example.project_hackaton.service.interfaces;

import com.example.project_hackaton.entity.User;

import java.util.Optional;

public interface IUserSearchService {
    Optional<User> findById(Long id);
}
