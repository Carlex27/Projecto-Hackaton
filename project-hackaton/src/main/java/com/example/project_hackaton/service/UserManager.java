package com.example.project_hackaton.service;

import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class UserManager implements UserDetailsManager {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(UserDetails user) {
        try {
            ((User) user).setPassword(passwordEncoder.encode(user.getPassword()));
            if(emailOrUsernameExist((User) user)){
                throw new RuntimeException("User is not valid");
            }
            userRepository.save((User) user);
        } catch (Exception e) {
            // Log the exception details here
            throw new RuntimeException("Failed to create user", e);
        }

    }

    private boolean emailOrUsernameExist(User user) {
        return userRepository.existsByUsername(user.getUsername())
                || userRepository.existsByEmail(user.getEmail());
    }

    @Override
    public void updateUser(UserDetails user) {
        User userToUpdate = (User) user;
        User existingUser = userRepository.findByUsername(userToUpdate.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format(
                                "User with username {0} not found", userToUpdate.getUsername())));
        userToUpdate.setUsername(existingUser.getUsername());
        userToUpdate.setEmail(existingUser.getEmail());
        userToUpdate.setRole(existingUser.getRole());
        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(String username) {
        // Delete the user from the repository by username
        if(!userRepository.existsByUsername(username)){
            throw new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", username));
        }
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // Change the password of the user in the repository
        User user = userRepository.findByPassword(oldPassword)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format(
                                "User with password {0} not found", oldPassword)));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        // Check if the user exists in the repository by username
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the repository by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", username));
        }

        // Return the UserDetails extracted from the User entity
        return userOptional.get();
    }
}
