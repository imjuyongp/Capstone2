package com.project.userservice.user.controller;

import com.project.userservice.global.response.BaseResponse;
import com.project.userservice.user.dto.request.SignUpRequest;
import com.project.userservice.user.dto.response.SignUpResponse;
import com.project.userservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
@Tag(name="User", description = "User 관리 API")
public class UserController {

  private final UserService userService;

  @Operation(summary="회원가입 API", description = "사용자 회원가입을 위한 API")
  @PostMapping("/sign-up")
  public ResponseEntity<BaseResponse<SignUpResponse>> signUp(
      @RequestBody @Valid SignUpRequest signUpRequest) {
    System.out.println(signUpRequest.getUsername() + ", " + signUpRequest.getPassword());
    SignUpResponse signUpResponse = userService.signUp(signUpRequest);
    return ResponseEntity.ok(BaseResponse.success("회원가입에 성공했습니다.", signUpResponse));
  }

}
