package com.project.userservice.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.userservice.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", nullable = false)
  private String username; // 사용자 아아디

  @JsonIgnore
  @Column(name = "password", nullable = false)
  private String password; // 사용자 비밀번호

  @JsonIgnore
  @Column(name = "refresh_token")
  private String refreshToken; // 리프레쉬 토큰

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.USER;

  public void saveRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
