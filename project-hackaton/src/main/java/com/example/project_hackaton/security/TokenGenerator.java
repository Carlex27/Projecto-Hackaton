package com.example.project_hackaton.security;

import com.example.project_hackaton.dto.Token;
import com.example.project_hackaton.entities.User;
import com.nimbusds.jwt.JWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;


    @Value("${jwt.cookie.expires}")
    private int COOKIE_EXPIRES;

    private String createAccessToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("ProjectHackaton")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("ProjectHackaton")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(user.getUsername())
                .build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public Token createToken(Authentication authentication, HttpServletResponse response){
        if(!(authentication.getPrincipal() instanceof User user)){
            throw new BadCredentialsException(
                    MessageFormat.format("Principal {0} is not of User type", authentication.getPrincipal().getClass())
            );
        }
        Token tokenDTO = new Token();
        tokenDTO.setUserId(user.getUsername());
        String accessToken = createAccessToken(authentication);
        tokenDTO.setAccessToken(accessToken);

        String refreshToken;
        if(authentication.getCredentials() instanceof Jwt jwt){
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if(daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        }else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDTO.setRefreshToken(refreshToken);

        createJwtCookie(response, accessToken);
        return tokenDTO;
    }
    public void createJwtCookie(HttpServletResponse response, String jwtToken) {
        // Crear la cookie
        Cookie jwtCookie = new Cookie("JWT", jwtToken);

        // Configurar la cookie
        jwtCookie.setHttpOnly(true); // La cookie no es accesible mediante JavaScript
        jwtCookie.setPath("/"); // La cookie es accesible desde todo el dominio
        // jwtCookie.setSecure(true); // Descomentar esta línea si estás usando HTTPS
        jwtCookie.setMaxAge(COOKIE_EXPIRES); // La cookie expira en 7 días

        // Añadir la cookie a la respuesta
        response.addCookie(jwtCookie);
    }

}
