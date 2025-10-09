package com.project.userservice.auth.service;

import com.project.userservice.auth.dto.request.LoginRequest;
import com.project.userservice.auth.dto.response.LoginResponse;
import com.project.userservice.auth.mapper.AuthMapper;
import com.project.userservice.global.exception.CustomException;
import com.project.userservice.global.jwt.JwtProvider;
import com.project.userservice.user.entity.Role;
import com.project.userservice.user.entity.User;
import com.project.userservice.user.exception.UserErrorCode;
import com.project.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;
  private final AuthMapper authMapper;
  private final PasswordEncoder passwordEncoder;


  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword());

    // 인증 처리
    authenticationManager.authenticate(authenticationToken);

    // 액세스 토큰 및 리프레시 토큰 발급
    String accessToken = jwtProvider.createAccessToken(user.getUsername());
    String refreshToken = jwtProvider.createRefreshToken(user.getUsername(),
        UUID.randomUUID().toString());

    // 리프레시 토큰 저장
    user.saveRefreshToken(refreshToken);

    // Access Token의 만료 시간을 가져옴
    Long expirationTime = jwtProvider.getExpiration(accessToken);

    // 로그인 성공 로깅
    log.info("로그인 성공: {}", user.getUsername());

    // 로그인 응답 반환
    return authMapper.toLoginResponse(user, accessToken, expirationTime);
  }

  @Value("${spring.profiles.active:default}") // 로컬에서만 작동하도록
  private String activeProfile;

  private boolean isLocalProfile() {
    return "local".equalsIgnoreCase(activeProfile);
  }

  @Transactional
  public LoginResponse testLogin(LoginRequest loginRequest) {
    String username = loginRequest.getUsername();
    String rawPassword = loginRequest.getPassword();

    // 1. 사용자 조회 또는 자동 회원가입
    User user = userRepository.findByUsername(username)
        .orElseGet(() -> {
          if (isLocalProfile()) {
            log.warn("테스트 환경에서 자동 회원가입 시도: {}", username);

            // 자동 권한 결정
            Role role = "admin".equals(username) && "1234".equals(rawPassword)
                ? Role.ADMIN
                : Role.USER;

            String nickname = role == Role.ADMIN
                ? "관리자"
                : "로그인 사용지" + UUID.randomUUID().toString().substring(0, 6);

            User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

            log.info("자동 생성된 유저: {} / 권한: {}", newUser.getUsername(), newUser.getRole());
            return userRepository.save(newUser);
          } else {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
          }
        });

    // 2. 인증 처리
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, rawPassword);
    authenticationManager.authenticate(authenticationToken);

    // 3. 토큰 발급
    String accessToken = jwtProvider.createAccessToken(user.getUsername());
    String refreshToken = jwtProvider.createRefreshToken(user.getUsername(), UUID.randomUUID().toString());
    user.saveRefreshToken(refreshToken);
    Long expirationTime = jwtProvider.getExpiration(accessToken);

    // 4. 반환
    return authMapper.toLoginResponse(user, accessToken, expirationTime);
  }

}
