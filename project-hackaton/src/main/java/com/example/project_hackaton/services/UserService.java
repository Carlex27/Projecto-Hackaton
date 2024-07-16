package com.example.project_hackaton.services;

import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService{
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //CRUD

    //CREATE
    public List<User> saveUsers(List<User> users){
        log.info("Saving users: {}",users);
        if(users.stream().anyMatch(user -> !isUserValid(user))){
            log.error("Some users are not valid");
            throw new EntityNotFoundException("Some users are not valid");
        }

        users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
        return userRepository.saveAll(users);
    }
    public User saveUser(User user){
        log.info("Saving user: {}",user.getUsername());
        if(!isUserValid(user)){
            log.error("User is not valid");
            throw new EntityNotFoundException("User is not valid");
        }
        if (user.getPassword() == null) {
            log.error("Password cannot be null");
            throw new IllegalArgumentException("Password cannot be null");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    //READ

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
    public List<User> findAllByRole(String role){
        log.info("Finding all users by role: {}",role);
        return userRepository.findAllByRole(role);
    }
    public List<User> findAll(){
        log.info("Finding all users");
        return userRepository.findAll();
    }
    //UPDATE
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
    public User updateUser(String username, User updateUser){
        Long idUser = findByUsername(username).get().getId();
        return updateUser(idUser,updateUser);
    }
    //DELETE


    public void deleteUser(Long id){
        log.warn("Deleting user with id: {}",id);
        if (!userRepository.existsById(id)){
            log.error("User with id: {} not found",id);
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
    public void deleteUser(String username){
        Long idUser = findByUsername(username).get().getId();
        deleteUser(idUser);
    }


}
