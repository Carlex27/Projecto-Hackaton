package com.example.project_hackaton.controllers;

import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //CRUD
    //CREATE
    @PostMapping("/create")
    public ResponseEntity<User> createUser(User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @PostMapping("/create-users")
    public ResponseEntity<List<User>> createUsers(List<User> users){
        return ResponseEntity.ok(userService.saveUsers(users));
    }
    //READ
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(
            @PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username).get());
    }
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email){
        return ResponseEntity.ok(userService.findByEmail(email).get());
    }
    @GetMapping("/{role}")
    public ResponseEntity<List<User>> getAllUsersByRole(
            @PathVariable String role){
        return ResponseEntity.ok(userService.findAllByRole(role));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }
    //UPDATE

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(id,user));
    }

    @PostMapping("/update/{username}")
    public ResponseEntity<User> updateUserByUsername(
            @PathVariable String username,
            @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(username,user));
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUserByUsername(
            @PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }






}
