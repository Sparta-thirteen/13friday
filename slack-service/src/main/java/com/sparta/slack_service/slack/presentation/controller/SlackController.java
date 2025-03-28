package com.sparta.slack_service.slack.presentation.controller;

import com.slack.api.methods.SlackApiException;
import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.application.dto.SlackResponseDto;
import com.sparta.slack_service.slack.application.service.SlackService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

  private final SlackService slackService;

  @PostMapping
  public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto requestDto)
      throws IOException, SlackApiException {
    slackService.sendMessage(requestDto);
    return ResponseEntity.ok().body("Slack 메시지 전송 완료");
  }

  @PostMapping("/order/{orderId}")
  public ResponseEntity<?> sendOrderMessage(@PathVariable UUID orderId)
      throws IOException, SlackApiException {
    slackService.sendOrderMessage(orderId);
    return ResponseEntity.ok().body("Slack 주문 메시지 전송 완료");
  }

  @GetMapping("/{slackId}")
  public ResponseEntity<SlackResponseDto> getMessage(
      @RequestHeader("X-Role") String role, @PathVariable UUID slackId) {
    SlackResponseDto responseDto = slackService.getMessage(role, slackId);
    return ResponseEntity.ok().body(responseDto);
  }

  @GetMapping
  public ResponseEntity<Page<SlackResponseDto>> getMessages(
      @RequestHeader("X-Role") String role, Pageable pageable) {
    Page<SlackResponseDto> responseDto = slackService.getMessages(role, pageable);
    return ResponseEntity.ok().body(responseDto);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<SlackResponseDto>> searchMessage(
      @RequestHeader("X-Role") String role,
      @RequestParam String keyword, Pageable pageable) {
    Page<SlackResponseDto> responseDto = slackService.searchMessage(role, keyword, pageable);
    return ResponseEntity.ok().body(responseDto);
  }

  @PatchMapping("/{slackId}")
  public ResponseEntity<?> updateMessage(
      @RequestHeader("X-Role") String role,
      @PathVariable UUID slackId,
      @RequestBody SlackRequestDto requestDto) throws IOException, SlackApiException {
    slackService.updateMessage(role, slackId, requestDto);
    return ResponseEntity.ok().body("Slack 메시지 수정 완료");
  }

  @DeleteMapping("/{slackId}")
  public ResponseEntity<?> deleteMessage(
      @RequestHeader("X-User-Id") String userId,
      @RequestHeader("X-Role") String role,
      @PathVariable UUID slackId)
      throws IOException, SlackApiException {
    slackService.deleteMessage(userId, role, slackId);
    return ResponseEntity.ok().body("Slack 메시지 삭제 완료");
  }
}
