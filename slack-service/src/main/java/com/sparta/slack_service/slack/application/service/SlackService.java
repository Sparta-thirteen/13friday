package com.sparta.slack_service.slack.application.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.sparta.slack_service.common.global.GlobalException;
import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.domain.entity.Slacks;
import com.sparta.slack_service.slack.domain.repository.SlackRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

  @Value(value = "${slack.token}")
  private String slackToken;

  private final SlackRepository slackRepository;
  private final Slack slack = Slack.getInstance();

  public void sendDirectMessage(SlackRequestDto requestDto) {
    try {
      // Slack 사용자 이메일로 사용자 ID 조회
      String slackId = getSlackId(requestDto.getReceiverEmail());

      // 사용자와 DM 채널 생성
      String dmChannelId = getDmChannelId(slackId);

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
      Slacks slacks = requestDto.toEntity();
      slackRepository.save(slacks);

    } catch (IOException | SlackApiException e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack DM 메시지 전송 실패: " + e.getMessage());
    }
  }

  private String getSlackId(String email) throws IOException, SlackApiException {
    UsersLookupByEmailResponse response = slack.methods(slackToken).usersLookupByEmail(
        UsersLookupByEmailRequest.builder().email(email).build()
    );

    return getSlackIdByResponse(response);
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

  private String getSlackIdByResponse(UsersLookupByEmailResponse response) {
    if (response.isOk() && response.getUser() != null) {
      return response.getUser().getId();
    } else {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "Slack 사용자 ID 조회 실패");
    }
  }
}
