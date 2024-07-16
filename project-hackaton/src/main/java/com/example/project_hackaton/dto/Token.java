package com.example.project_hackaton.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String userId;
    private String accessToken;
    private String refreshToken;

}
