package com.project.userservice.auth.controller;

import com.project.userservice.auth.dto.request.LoginRequest;
import com.project.userservice.auth.dto.response.LoginResponse;
import com.project.userservice.auth.service.AuthService;
import com.project.userservice.global.exception.CustomException;
import com.project.userservice.global.response.BaseResponse;
import com.project.userservice.user.exception.UserErrorCode;
import com.project.userservice.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
@Tag(name = "Auth", description = "Auth 관리 API")
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;

  @Operation(summary = "사용자 로그인", description = "사용자 로그인을 위한 API")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
    LoginResponse loginResponse = authService.login(loginRequest);

    // refreshToken 가져오기
    String refreshToken = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND))
        .getRefreshToken();

    // Set-Cookie 설정 (HttpOnly + Secure)
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    // refreshTokenCookie.setSecure(true);  // HTTPS일 때만
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);  // 예: 7일

    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(BaseResponse.success("로그인에 성공했습니다.", loginResponse));
  }

  /*@Operation(summary = "테스트용 로그인(관리자 역할로 자동 회원가입)", description = "개발 환경에서만 사용하세요")
  @PostMapping("/test-login")
  public ResponseEntity<BaseResponse<LoginResponse>> testLogin(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {

    log.info("testLogin API 호출 - username: {}", loginRequest.getUsername());

    LoginResponse loginResponse = authService.testLogin(loginRequest);

    String refreshToken = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND))
        .getRefreshToken();

    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);

    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(BaseResponse.success("테스트용 로그인에 성공했습니다.", loginResponse));
  }*/

}
