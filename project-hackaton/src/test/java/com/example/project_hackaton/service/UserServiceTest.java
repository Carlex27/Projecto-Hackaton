package com.example.project_hackaton.service;

import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<User> users;
    private User user;

    /**
     * Se ejecuta antes de cada test
     * Se crear un nuevo objeto User
     * Se le asignan valores
     */
    @BeforeEach
    void setUp(){
       User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setRole("USER");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test2");
        user2.setEmail("text2@example.com");
        user2.setPassword("test2");
        user2.setRole("ADMIN");
        users = List.of(user, user2);
        userRepository.saveAll(users);
    }


    /**
     * Test para comprobar que se borra un usuario existente
     */
    @Test
    void deleteUser_existingUser() {
        when(userRepository.existsById(2L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(2L);
        userService.deleteUser(2L);
        verify(userRepository, times(1)).deleteById(2L);
    }

    @Test
    void deleteUser_nonExistingUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void findByUsername_existingUser() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.findByUsername("test");
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getUsername());
    }

    @Test
    void findByUsername_nonExistingUser() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername("nonExistingUser"));
    }

    @Test
    void updateUser_existingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        User result = userService.updateUser(1L, updatedUser);
        assertEquals("updatedUser", result.getUsername());
    }

    @Test
    void updateUser_nonExistingUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        User updatedUser = new User();
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updatedUser));
    }
}