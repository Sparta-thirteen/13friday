package com.sparta.slack_service.slack.presentation.controller;

import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.application.dto.SlackResponseDto;
import com.sparta.slack_service.slack.application.service.SlackService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto requestDto) {
    slackService.sendMessage(requestDto);
    return ResponseEntity.ok().body("Slack 메시지 전송 완료");
  }

  @GetMapping("/{slackId}")
  public ResponseEntity<SlackResponseDto> getMessage(@PathVariable UUID slackId) {
    SlackResponseDto responseDto = slackService.getMessage(slackId);
    return ResponseEntity.ok().body(responseDto);
  }

  @PatchMapping("/{slackId}")
  public ResponseEntity<?> updateMessage(@PathVariable UUID slackId,
      @RequestBody SlackRequestDto requestDto) {
    slackService.updateMessage(slackId, requestDto);
    return ResponseEntity.ok().body("Slack 메시지 수정 완료");
  }
}
