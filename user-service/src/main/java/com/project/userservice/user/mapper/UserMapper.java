package com.project.userservice.user.mapper;

import com.project.userservice.user.dto.request.SignUpRequest;
import com.project.userservice.user.dto.response.SignUpResponse;
import com.project.userservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public SignUpResponse tosignUpResponse(User user) {
    return SignUpResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .build();

  }
}
