package com.example.project_hackaton.service;

import com.example.project_hackaton.entity.Rol;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.repository.UserRepository;
import com.example.project_hackaton.service.interfaces.IUserSearchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSearchService implements IUserSearchService {
    private final Logger log = LoggerFactory.getLogger(UserSearchService.class);
    private final UserRepository userRepository;

    public Optional<User> findById(Long id){
        log.info("Finding user by id: {}",id);
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }
    public Optional<User> findByUsername(String username){
        log.info("Finding user by username: {}",username);
        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public Optional<User> findByEmail(String email){
        log.info("Finding user by email: {}",email);
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public List<User> findAll(){
        log.info("Finding all users");
        return userRepository.findAll();
    }


}
