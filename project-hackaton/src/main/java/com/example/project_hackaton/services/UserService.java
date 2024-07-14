package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger log;
    private final UserRepository userRepository;

    public void deleteUser(Long id){
        log.warn("Deleting user with id: {}",id);
        if (!userRepository.existsById(id)){
            log.error("User with id: {} not found",id);
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
    public void deleteUser(String username){
        deleteUser(findByUsername(username).get().getId());
    }

    public Optional<User> findByUsername(String username){
        log.info("Finding user by username: {}",username);
        if(!userRepository.findByUsername(username).isPresent()){
            log.error("User with username: {} not found",username);
            throw new RuntimeException("User not found");
        }
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email){
        log.info("Finding user by email: {}",email);
        if(!userRepository.findByEmail(email).isPresent()){
            log.error("User with email: {} not found",email);
            throw new RuntimeException("User not found");
        }
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User updateUser){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found id: " + id));
        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(updateUser.getPassword());
        existingUser.setRole(updateUser.getRole());
        log.info("Updating user with id: {}",id);
        return userRepository.save(existingUser);
    }

    public List<User> findAllByRole(String role){
        log.info("Finding all users by role: {}",role);
        return userRepository.findAllByRole(role);
    }

    public User saveUser(User user){
        log.info("Saving user: {}",user.getUsername());
        if(!isUserValid(user)){
            log.error("User is not valid");
            throw new RuntimeException("User is not valid");
        }
        return userRepository.save(user);
    }
    private boolean isUserValid(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent()
                && userRepository.findByEmail(user.getEmail()).isPresent()){
            log.error("Username or email already exists");
            return false;
        }
        return true;
    }
}
