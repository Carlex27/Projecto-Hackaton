package com.example.project_hackaton.mapper;

import com.example.project_hackaton.dto.UserInfoDto;
import com.example.project_hackaton.entity.User;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper {

    public UserInfoDto convertToDto(User user){
        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
