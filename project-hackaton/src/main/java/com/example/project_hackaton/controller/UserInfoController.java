package com.example.project_hackaton.controller;

import com.example.project_hackaton.service.UserInfoService;
import com.example.project_hackaton.service.interfaces.IUserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-info")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        log.info("User info for user: {}", username);
        return ResponseEntity.ok(userInfoService.getUserInfo(username));

    }


}
