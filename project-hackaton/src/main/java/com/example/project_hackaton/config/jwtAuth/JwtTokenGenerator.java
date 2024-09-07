package com.example.project_hackaton.config.jwtAuth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Jwt token generator class
 * Contains
 * - generateAccessToken
 * - generateRefreshToken
 * - getRolesOfUser
 * - getPermissionsFromRoles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {
    private final JwtEncoder jwtEncoder;

    /**
     * Generate access token for user authentication
     * @param authentication
     * @return
     */
    public String generateAccessToken(Authentication authentication) {

        log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", authentication.getName());

        String roles = getRolesOfUser(authentication);

        String permissions = getPermissionsFromRoles(roles);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("myapp")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", permissions)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Get roles of user
     * @param authentication
     * @return
     */
    private static String getRolesOfUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    /**
     * Get permissions from roles
     * @param roles
     * @return
     */
    private String getPermissionsFromRoles(String roles) {
        Set<String> permissions = new HashSet<>();

        if (roles.contains("ROLE_ADMIN")) {
            permissions.addAll(List.of("READ", "WRITE", "DELETE", "WRITE_TEAM"));
        }
        if (roles.contains("ROLE_MANAGER")) {
            permissions.addAll(List.of("READ","WRITE_TEAM"));
        }
        if (roles.contains("ROLE_USER")) {
            permissions.addAll(List.of("READ","WRITE_TEAM"));
        }

        return String.join(" ", permissions);
    }

    /**
     * Generate refresh token for user authentication
     * @param authentication
     * @return
     */

    public String generateRefreshToken(Authentication authentication){
        log.info("[JwtTokenGenerator:generateRefreshToken] Token Creation Started for:{}", authentication.getName());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("myapp")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", "REFRESH_TOKEN")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
