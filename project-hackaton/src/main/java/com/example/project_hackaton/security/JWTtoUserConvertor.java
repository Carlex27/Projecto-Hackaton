package com.example.project_hackaton.security;

import com.example.project_hackaton.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JWTtoUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source){
        User user = new User();
        user.setId(source.getClaim("id"));
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }
}
