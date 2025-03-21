package com.sparta.slack_service.slack.domain.entity;

import com.sparta.slack_service.common.global.TimeStamped;
import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Slacks extends TimeStamped {

  @Id
  @UuidGenerator
  @Column(name = "slack_id")
  private UUID id;

  private Long receiverId;

  private UUID orderId;

  private String channelId;

  @Column(columnDefinition = "TEXT")
  private String message;

  private String sentAt;

  public void update(SlackRequestDto requestDto) {
    this.message = requestDto.getMessage();
  }
}
