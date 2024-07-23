package com.example.project_hackaton.mapper;

import com.example.project_hackaton.dto.UserRegistrationDto;
import com.example.project_hackaton.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User convertToEntity(UserRegistrationDto userRegistrationDto){
        return User.builder()
                .username(userRegistrationDto.username())
                .email(userRegistrationDto.userEmail())
                .roles(userRegistrationDto.userRole())
                .password(passwordEncoder.encode(userRegistrationDto.userPassword()))
                .build();
    }
}
