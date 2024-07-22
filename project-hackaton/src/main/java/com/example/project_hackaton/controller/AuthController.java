package com.example.project_hackaton.controller;

import com.example.project_hackaton.dto.LoginDTO;
import com.example.project_hackaton.dto.SignUp;
import com.example.project_hackaton.dto.Token;
import com.example.project_hackaton.entity.Rol;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.security.TokenGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @PostMapping("/register")
    public ResponseEntity<Token> register(
            @RequestBody SignUp signUpDTO,
            HttpServletResponse response){

        User user = new User(signUpDTO.getUsername().toLowerCase(),
                signUpDTO.getEmail().toLowerCase(),
                signUpDTO.getPassword(),
                Rol.USER);
        userDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user,
                signUpDTO.getPassword(),
                Collections.EMPTY_LIST);
        return  ResponseEntity.ok(tokenGenerator.createToken(authentication, response));
    }
    @PostMapping("/login")
    public ResponseEntity<Token> login(
            @RequestBody LoginDTO loginDTO,
            HttpServletResponse response) {
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));

        return ResponseEntity.ok(tokenGenerator.createToken(authentication, response));
    }

    @PostMapping("/token")
    public ResponseEntity<Token> token(
            @RequestBody Token tokenDTO,
            HttpServletResponse response) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        // check if present in db and not revoked, etc

        return ResponseEntity.ok(tokenGenerator.createToken(authentication, response));
    }
}
