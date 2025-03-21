package com.sparta.slack_service.slack.presentation.controller;

import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.application.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

  private final SlackService slackService;

  @PostMapping
  public ResponseEntity<?> sendDirectMessage(@RequestBody SlackRequestDto requestDto) {
    slackService.sendDirectMessage(requestDto);
    return ResponseEntity.ok().body("Slack 메시지 전송 완료");
  }
}
