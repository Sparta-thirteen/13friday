package com.sparta.slack_service.slack.application.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.chat.ChatUpdateRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.chat.ChatUpdateResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.sparta.slack_service.common.global.GlobalException;
import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.application.dto.SlackResponseDto;
import com.sparta.slack_service.slack.domain.entity.Slacks;
import com.sparta.slack_service.slack.domain.repository.SlackRepository;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackService {

  @Value(value = "${slack.token}")
  private String slackToken;

  private final SlackRepository slackRepository;
  private final Slack slack = Slack.getInstance();

  public void sendMessage(SlackRequestDto requestDto) {
    try {
      // Slack 사용자 이메일로 사용자 ID 조회
      String slackUserId = getSlackUserId(requestDto.getReceiverEmail());

      // 사용자와 DM 채널 생성
      String dmChannelId = getDmChannelId(slackUserId);

      // DM 채널 ID를 사용해 메시지 전송
      ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(
          ChatPostMessageRequest.builder()
              .channel(dmChannelId)
              .text(requestDto.getMessage())
              .build()
      );

      // response 검증
      validateSlackResponse(response);

      // DB에 저장
      Slacks slacks = requestDto.toEntity(response.getTs());
      slackRepository.save(slacks);

    } catch (IOException | SlackApiException e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 메시지 전송 실패: " + e.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public SlackResponseDto getMessage(UUID slackId) {
    Slacks slacks = findSlacks(slackId);
    return SlackResponseDto.toDto(slacks);
  }

  @Transactional
  public void updateMessage(UUID slackId, SlackRequestDto requestDto) {
    try {
      Slacks slacks = findSlacks(slackId);
      String slackUserId = getSlackUserId(requestDto.getReceiverEmail());
      String dmChannelId = getDmChannelId(slackUserId);

      ChatUpdateResponse response = slack.methods(slackToken).chatUpdate(
          ChatUpdateRequest.builder()
              .channel(dmChannelId)
              .ts(slacks.getSentAt())
              .text(requestDto.getMessage())
              .build()
      );
      validateSlackResponse(response);
      slacks.update(requestDto);

    } catch (IOException | SlackApiException e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 메시지 수정 실패: " + e.getMessage());
    }
  }

  private String getSlackUserId(String email) throws IOException, SlackApiException {
    UsersLookupByEmailResponse response = slack.methods(slackToken).usersLookupByEmail(
        UsersLookupByEmailRequest.builder().email(email).build()
    );

    return getSlackUserIdByResponse(response);
  }

  private String getSlackUserIdByResponse(UsersLookupByEmailResponse response) {
    if (response.isOk() && response.getUser() != null) {
      return response.getUser().getId();
    } else {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack 사용자 ID 조회 실패");
    }
  }

  private String getDmChannelId(String receiverId) throws IOException, SlackApiException {
    ConversationsOpenResponse response = slack.methods(slackToken).conversationsOpen(
        ConversationsOpenRequest.builder().users(List.of(receiverId)).build()
    );
    validateSlackResponse(response);
    return response.getChannel().getId();
  }

  private void validateSlackResponse(ChatPostMessageResponse response) {
    if (!response.isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 메시지 전송 실패");
    }
  }

  private void validateSlackResponse(ConversationsOpenResponse response) {
    if (!response.isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 채널 생성 실패");
    }
  }

  private void validateSlackResponse(ChatUpdateResponse response) {
    if (!response.isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 메시지 수정 실패");
    }
  }

  private Slacks findSlacks(UUID slackId) {
    return slackRepository.findById(slackId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "Slack 메시지를 찾을 수 없습니다"));
  }
}
