package com.example.project_hackaton.controller;

import com.example.project_hackaton.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> allUsers(

    ){
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findUserById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/find/username/{username}")
    public ResponseEntity<?> findUserByUsername(
            @PathVariable String username
    ){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/find/email/{email}")
    public ResponseEntity<?> findUserByEmail(
            @PathVariable String email
    ){
        return ResponseEntity.ok(userService.findByEmail(email));
    }



}
