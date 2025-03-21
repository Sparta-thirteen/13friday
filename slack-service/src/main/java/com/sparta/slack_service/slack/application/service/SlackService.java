package com.sparta.slack_service.slack.application.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatDeleteRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.chat.ChatUpdateRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatDeleteResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public void sendMessage(SlackRequestDto requestDto) throws IOException, SlackApiException {
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
    Slacks slacks = requestDto.toEntity(dmChannelId, response.getTs());
    slackRepository.save(slacks);
  }

  @Transactional(readOnly = true)
  public SlackResponseDto getMessage(UUID slackId) {
    Slacks slacks = findSlacks(slackId);
    return SlackResponseDto.toDto(slacks);
  }

  @Transactional(readOnly = true)
  public Page<SlackResponseDto> getMessages(Pageable pageable) {
    return slackRepository.findAllByDeletedAtIsNull(pageable)
        .map(SlackResponseDto::toDto);
  }

  @Transactional(readOnly = true)
  public Page<SlackResponseDto> searchMessage(String keyword, Pageable pageable) {
    return slackRepository.findAllByMessage(keyword, pageable)
        .map(SlackResponseDto::toDto);
  }

  @Transactional
  public void updateMessage(UUID slackId, SlackRequestDto requestDto)
      throws IOException, SlackApiException {
    Slacks slacks = findSlacks(slackId);

    ChatUpdateResponse response = slack.methods(slackToken).chatUpdate(
        ChatUpdateRequest.builder()
            .channel(slacks.getChannelId())
            .ts(slacks.getSentAt())
            .text(requestDto.getMessage())
            .build()
    );
    validateSlackResponse(response);
    slacks.update(requestDto);
  }

  @Transactional
  public void deleteMessage(UUID slackId) throws IOException, SlackApiException {
    Slacks slacks = findSlacks(slackId);

    ChatDeleteResponse response = slack.methods(slackToken).chatDelete(
        ChatDeleteRequest.builder()
            .channel(slacks.getChannelId())
            .ts(slacks.getSentAt())
            .build()
    );
    validateSlackResponse(response);
    slacks.softDelete();
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

  private Slacks findSlacks(UUID slackId) {
    Slacks slacks = slackRepository.findById(slackId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "Slack 메시지를 찾을 수 없습니다"));

    if (slacks.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 Slack 메시지입니다");
    }

    return slacks;
  }

  private <T> void validateSlackResponse(T response) {
    if (response instanceof ChatPostMessageResponse
        && !((ChatPostMessageResponse) response).isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "Slack DM 메시지 전송 실패: " + ((ChatPostMessageResponse) response).getError());
    } else if (response instanceof ConversationsOpenResponse
        && !((ConversationsOpenResponse) response).isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "Slack DM 채널 생성 실패: " + ((ConversationsOpenResponse) response).getError());
    } else if (response instanceof ChatUpdateResponse && !((ChatUpdateResponse) response).isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "Slack DM 메시지 수정 실패: " + ((ChatUpdateResponse) response).getError());
    } else if (response instanceof ChatDeleteResponse && !((ChatDeleteResponse) response).isOk()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST,
          "Slack DM 메시지 삭제 실패: " + ((ChatDeleteResponse) response).getError());
    }
  }
}
