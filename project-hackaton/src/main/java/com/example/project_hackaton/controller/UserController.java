package com.example.project_hackaton.controller;

import com.example.project_hackaton.dto.UserDTO;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/user/{id}")
    public ResponseEntity<?> user(
            @AuthenticationPrincipal User user,
            @PathVariable String id) {
        Long userId = Long.parseLong(id);
        return ResponseEntity.ok(UserDTO.from(userService.findById(userId)
                .orElseThrow()));
    }
    @GetMapping("/user")
    public ResponseEntity<?> user(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        return ResponseEntity.ok(UserDTO.from(userService.findById(user.getId()).orElseThrow()));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(
            @PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username).get());
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email){
        return ResponseEntity.ok(userService.findByEmail(email).get());
    }
    @GetMapping("/role/{role}")
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
