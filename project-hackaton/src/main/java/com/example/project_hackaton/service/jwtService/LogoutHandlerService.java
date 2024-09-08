package com.example.project_hackaton.service.jwtService;


import com.example.project_hackaton.dto.TokenType;
import com.example.project_hackaton.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * LogoutHandlerService class is responsible for handling the logout of the user.
 * It revokes the refresh token of the user.
 * It implements the LogoutHandler interface.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * This method is responsible for revoking the refresh token of the user.
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Get the refresh token from the request header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the refresh token is present in the request header
        if(!authHeader.startsWith(TokenType.Bearer.name())){
            return;
        }

        // Extract the refresh token from the request header
        final String refreshToken = authHeader.substring(7);

        // Revoke the refresh token
        var storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    return token;
                })
                .orElse(null);
    }
}
