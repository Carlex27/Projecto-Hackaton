package com.example.project_hackaton.service;

import com.example.project_hackaton.dto.UserInfoDto;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.mapper.UserInfoMapper;
import com.example.project_hackaton.service.interfaces.IUserSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserInfoService {

    private final IUserSearchService userService;
    private final UserInfoMapper userInfoMapper;

    public UserInfoDto getUserInfo(String username){
        Optional<User> user = userService.findByUsername(username);
        return userInfoMapper.convertToDto(user.get());
    }


}
