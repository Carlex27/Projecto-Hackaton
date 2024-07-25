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
    

}
