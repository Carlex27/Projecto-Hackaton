package com.example.project_hackaton.service.jwtService;

import com.example.project_hackaton.config.jwtAuth.JwtTokenGenerator;
import com.example.project_hackaton.dto.AuthResponseDto;
import com.example.project_hackaton.dto.TokenType;
import com.example.project_hackaton.dto.UserRegistrationDto;
import com.example.project_hackaton.entity.RefreshTokenEntity;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.mapper.UserMapper;
import com.example.project_hackaton.repository.RefreshTokenRepository;
import com.example.project_hackaton.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

/**
 * AuthService class is responsible for authenticating the user and generating the jwt tokens
 * for the user. It also provides the functionality to generate the access token using the refresh token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Value("${jwt.expiration-time.access-token}")
    private int ACCESS_TOKEN_EXPIRY;

    @Value("${jwt.expiration-time.refresh-token}")
    private int REFRESH_TOKEN_EXPIRY;

    private final UserRepository userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UserMapper userInfoMapper;

    /**
     * This method is responsible for authenticating the user and generating the jwt tokens
     * for the user. Refresh token is also generated and saved in the database.
     * Access token is generated and returned to the user.
     * @param authentication
     * @param response
     * @return AuthResponseDto
     */
    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {

            var user = userInfoRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> {

                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());

                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            creatRefreshTokenCookie(response, refreshToken);

            saveUserRefreshToken(user, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getUsername());

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(ACCESS_TOKEN_EXPIRY)
                    .userName(user.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    /**
     * This method is responsible for saving the refresh token in the database.
     * @param userInfoEntity
     * @param refreshToken
     */
    private void saveUserRefreshToken(User userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    /**
     * This method is responsible for creating the refresh token cookie.
     * @param response
     * @param refreshToken
     * @return
     */
    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(REFRESH_TOKEN_EXPIRY);
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    /**
     * This method is responsible for generating the access token using the refresh token.
     * @param authorizationHeader
     * @return
     */
    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }
        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        User user = refreshTokenEntity.getUser();

        Authentication authentication = createAuthenticationObject(user);

        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(ACCESS_TOKEN_EXPIRY)
                .userName(user.getUsername())
                .tokenType(TokenType.Bearer)
                .build();

    }

    /**
     * This method is responsible for creating the authentication object.
     * @param userInfoEntity
     * @return
     */
    private static Authentication createAuthenticationObject(User userInfoEntity){
        String username = userInfoEntity.getUsername();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority)role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    /**
     * This method is responsible for registering the user.
     * @param userRegistrationDto
     * @param httpServletResponse
     * @return
     */

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse){
        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            if(userVerificacion(userRegistrationDto)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or email already exists");
            }

            User userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            User savedUserDetails = userInfoRepo.save(userDetailsEntity);

            saveUserRefreshToken(userDetailsEntity, refreshToken);

            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUsername());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(REFRESH_TOKEN_EXPIRY)
                    .userName(savedUserDetails.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (ResponseStatusException e) {
            log.error("[AuthService:registerUser]ResponseStatusException while registering the user due to :" + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    /**
     * This method is responsible for verifying the user.
     * if the user already exists then it will return true.
     * if the email already exists then it will return true.
     * @param userRegistrationDto
     * @return
     */
    private boolean userVerificacion(UserRegistrationDto userRegistrationDto) {
        if(userInfoRepo.existsByUsername(userRegistrationDto.username())){
            log.error("[AuthService:userVerificacion]User:{} already exists",userRegistrationDto.username());
            return true;
        }
        if (userInfoRepo.existsByEmail(userRegistrationDto.userEmail())){
            log.error("[AuthService:userVerificacion]Email:{} already exists",userRegistrationDto.userEmail());
            return true;
        }
        return false;
    }
}
