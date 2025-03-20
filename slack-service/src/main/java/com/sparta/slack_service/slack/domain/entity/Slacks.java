package com.sparta.slack_service.slack.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_slack")
public class Slacks {

  @Id
  @UuidGenerator
  @Column(name = "slack_id")
  private UUID id;

  private Long receiverId;

  private UUID orderId;

  @Column(columnDefinition = "TEXT")
  private String message;

  private LocalDateTime sentAt;
}
