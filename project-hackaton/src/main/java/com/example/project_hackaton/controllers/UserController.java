package com.example.project_hackaton.controllers;

import com.example.project_hackaton.entities.User;
import com.example.project_hackaton.services.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserSearchService userService;

    //CRUD
    //CREATE

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



    //DELETE







}
