package com.sparta.eureka.client.auth.domain.user.entity;

import com.sparta.eureka.client.auth.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
@Comment("사용자")
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  @Comment("사용자 기본키")
  private Long userId;

  @NotNull
  @Column(name = "username", nullable = false, unique = true, length = 10)
  @Comment("사용자 아이디")
  private String username;

  @NotNull
  @Column(name = "password", nullable = false)
  @Comment("사용자 비밀번호")
  private String password;

  @NotNull
  @Column(name = "slack_id", nullable = false)
  @Comment("슬랙ID")
  private String slackId;

  @NotNull
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("사용자 권한")
  private Role role;

  public static UserBuilder builder(
      String username,
      String password,
      String slackId,
      Role role) {
    return User.innerBuilder()
        .username(username)
        .password(password)
        .slackId(slackId)
        .role(role);
  }

  public void updateRole(Role role) {
    this.role = role;
  }
}
