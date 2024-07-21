package com.example.project_hackaton.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Intenta obtener la cookie JWT de la solicitud
        Cookie jwtCookie = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (jwtCookie != null) {
            String jwtToken = jwtCookie.getValue();
            // Aquí deberías validar el token y extraer los detalles del usuario
            // Por ejemplo, asumimos que el token es válido y extraemos el nombre de usuario
            try{
                //Obtener el bean JwtDecoder
                JwtDecoder jwtDecoder = applicationContext.getBean(JwtDecoder.class);
                Jwt jwt = jwtDecoder.decode(jwtToken);
                String username = jwt.getClaimAsString("sub"); // Reemplaza esto con la lógica de extracción real

                // Si el token es válido, configura la autenticación
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER")); // Asume un rol, ajusta según sea necesario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);
    }
}
