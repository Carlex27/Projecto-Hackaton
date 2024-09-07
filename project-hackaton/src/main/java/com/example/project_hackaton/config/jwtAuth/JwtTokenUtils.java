package com.example.project_hackaton.config.jwtAuth;

import com.example.project_hackaton.config.userConfig.UserConfig;
import com.example.project_hackaton.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

/**
 * Jwt token utility class
 * Contains
 * - getUserName
 * - isTokenValid
 * - userDetails
 * - getIfTokenIsExpired
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    /**
     * User repository
     */
    private final UserRepository userRepository;

    /**
     * Get username from jwt token
     * @param jwtToken
     * @return
     */
    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    /**
     * Check if  the JWT token is valid
     * @param jwtToken
     * @param userDetails
     * @return
     */
    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;
    }

    /**
     * Check if the token is expired
     * @param jwtToken
     * @return
     */
    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    /**
     * Get user details
     * @param username
     * @return
     */
    public UserDetails userDetails(String username){
        return userRepository
                .findByUsername(username)
                .map(UserConfig::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Username: " + username + " does not exist"));

    }

}
