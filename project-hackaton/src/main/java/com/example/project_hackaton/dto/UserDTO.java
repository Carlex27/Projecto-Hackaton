package com.example.project_hackaton.dto;


import com.example.project_hackaton.entities.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private Long id;
    private String username;
    public static User from(User user){
        return user;
    }
}
