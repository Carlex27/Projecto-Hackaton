package com.example.project_hackaton.controller;


import com.example.project_hackaton.dto.UserRegistrationDto;
import com.example.project_hackaton.service.jwtService.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AuthController is responsible for handling the authentication and authorization of the user.
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    /**
     * This method is responsible for authenticating the user and generating the JWT tokens.
     *
     * @param authentication
     * @param response
     * @return
     */

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(
            Authentication authentication,
            HttpServletResponse response){

        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
    }

    /**
     * This method is responsible for generating the access token using the refresh token.
     * @param authorizationHeader
     * @return
     */
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    /**
     * This method is responsible for registering the user.
     * {
     *     "username": "username",
     *     "userEmail": "emailUser",
     *     "userPassword": "password",
     *     "userRole": "ROLE_USER" / ROLE_ADMIN / ROLE_MANAGER
     * }
     * @param userRegistrationDto
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse
    ){
        log.info("[AuthController:registerUser]Signup Process Started for user:{}",userRegistrationDto.username());
        if(bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return  ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
    }
}
