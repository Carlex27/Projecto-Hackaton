package com.example.project_hackaton.dto;


import com.example.project_hackaton.entities.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private String id;
    private String username;
    public static UserDTO from(User user){
        return UserDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .build();
    }
}
