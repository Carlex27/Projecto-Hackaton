package com.example.project_hackaton.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Username of the user", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED )
    @Column(unique = true, nullable = false)
    private String username;

    @Schema(description = "Email of the user", example = "email@user.com", requiredMode = Schema.RequiredMode.REQUIRED )
    @Column(unique = true, nullable = false)
    private String email;

    @Schema(description = "Password of the user", example = "password", requiredMode = Schema.RequiredMode.REQUIRED )
    @Column(nullable = false)
    private String password;

    @Schema(description = "Role of the User", example = "ROLE_USER / ROLE_MANAGER / ROLE_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED )
    @Column(nullable = false)
    private String roles;

    @Schema(description = "JWT refresh token of the user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private  List<RefreshTokenEntity> refreshTokens;


}
