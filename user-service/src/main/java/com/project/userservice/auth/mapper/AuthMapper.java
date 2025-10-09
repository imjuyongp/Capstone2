package com.project.userservice.auth.mapper;

import com.project.userservice.auth.dto.response.LoginResponse;
import com.project.userservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
  public LoginResponse toLoginResponse(User user, String accessToken, Long expirationTime) {
    return LoginResponse.builder()
        .accessToken(accessToken)
        .userId(user.getId())
        .username(user.getUsername())
        .role(user.getRole())
        .expirationTime(expirationTime)
        .build();
  }
}
