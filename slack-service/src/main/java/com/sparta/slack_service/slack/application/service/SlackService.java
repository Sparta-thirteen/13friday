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
import com.sparta.slack_service.ai.application.service.AiService;
import com.sparta.slack_service.common.global.GlobalException;
import com.sparta.slack_service.common.infrastructure.client.HubClient;
import com.sparta.slack_service.common.infrastructure.client.OrderClient;
import com.sparta.slack_service.common.infrastructure.client.UserClient;
import com.sparta.slack_service.common.infrastructure.dto.OrderInternalResponse;
import com.sparta.slack_service.common.infrastructure.dto.UserResponseDto;
import com.sparta.slack_service.slack.application.dto.SlackRequestDto;
import com.sparta.slack_service.slack.application.dto.SlackResponseDto;
import com.sparta.slack_service.slack.domain.entity.Slacks;
import com.sparta.slack_service.slack.domain.repository.SlackRepository;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  private final OrderClient orderClient;
  private final UserClient userClient;
  private final HubClient hubClient;

  private final SlackRepository slackRepository;
  private final AiService aiService;

  @Value(value = "${slack.token}")
  private String slackToken;
  private final Slack slack = Slack.getInstance();

  public void sendMessage(SlackRequestDto requestDto) throws IOException, SlackApiException {
    // userId로 slackId 조회
    Long receiverId = requestDto.getReceiverId();
    UserResponseDto userResponse = userClient.getUser(receiverId, receiverId.toString(), "MASTER")
        .getBody().getData();

    // 사용자와 DM 채널 생성
    String dmChannelId = getDmChannelId(userResponse.getSlackId());

    // DM 채널 ID를 사용해 메시지 전송
    String slackMsg = requestDto.getMessage();
    ChatPostMessageResponse response = createChatPostMessage(dmChannelId, slackMsg);

    // response 검증
    validateSlackResponse(response);

    // DB에 저장
    Slacks slacks = Slacks.toEntity(receiverId, dmChannelId, slackMsg, response.getTs());
    slackRepository.save(slacks);
  }

  public void sendOrderMessage(UUID orderId) throws IOException, SlackApiException {
    // 주문 정보 조회
    OrderInternalResponse orderResponse = orderClient.getOrdersInternal(orderId);

    // 발송 허브 담당자의 slack Id 조회 -> dm 채널 생성
    Long hubUserId = orderResponse.getHubUserID();
    UserResponseDto userResponse = userClient.getUser(hubUserId, hubUserId.toString(), "MASTER")
        .getBody().getData();
    String slackUserId = getSlackUserId(userResponse.getSlackId());
    String dmChannelId = getDmChannelId(slackUserId);

    // 주문 정보를 기반으로 gemini ai에 발송 시한 요청 및 슬랙 메시지 작성
    String prompt = createOrderPrompt(orderResponse, userResponse);
    String deadline = aiService.getShippingDeadline(prompt);
    String slackMsg = prompt + "최종 발송 시한: " + deadline;

    // slack 메시지 전송 후 검증
    ChatPostMessageResponse response = createChatPostMessage(dmChannelId, slackMsg);
    validateSlackResponse(response);

    // 전송 메시지 DB 저장
    Slacks slacks = Slacks.toEntityByOrder(hubUserId, orderId, dmChannelId, slackMsg,
        response.getTs());
    slackRepository.save(slacks);
  }

  @Transactional(readOnly = true)
  public SlackResponseDto getMessage(String role, UUID slackId) {
    roleCheck(role);
    Slacks slacks = findSlacks(slackId);
    return SlackResponseDto.toDto(slacks);
  }

  @Transactional(readOnly = true)
  public Page<SlackResponseDto> getMessages(String role, Pageable pageable) {
    roleCheck(role);
    return slackRepository.findAllByDeletedAtIsNull(pageable)
        .map(SlackResponseDto::toDto);
  }

  @Transactional(readOnly = true)
  public Page<SlackResponseDto> searchMessage(String role, String keyword, Pageable pageable) {
    roleCheck(role);
    return slackRepository.findAllByMessage(keyword, pageable)
        .map(SlackResponseDto::toDto);
  }

  @Transactional
  public void updateMessage(String role, UUID slackId, SlackRequestDto requestDto)
      throws IOException, SlackApiException {
    roleCheck(role);
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
  public void deleteMessage(String userId, String role, UUID slackId)
      throws IOException, SlackApiException {
    roleCheck(role);
    Slacks slacks = findSlacks(slackId);

    ChatDeleteResponse response = slack.methods(slackToken).chatDelete(
        ChatDeleteRequest.builder()
            .channel(slacks.getChannelId())
            .ts(slacks.getSentAt())
            .build()
    );
    validateSlackResponse(response);
    slacks.softDelete(Long.parseLong(userId));
  }

  private ChatPostMessageResponse createChatPostMessage(String dmChannelId, String text)
      throws IOException, SlackApiException {
    return slack.methods(slackToken).chatPostMessage(
        ChatPostMessageRequest.builder()
            .channel(dmChannelId)
            .text(text)
            .build()
    );
  }

  // Slack 사용자 이메일로 사용자 ID 조회
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

  private void roleCheck(String role) {
    if (!role.equals("MASTER")) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다");
    }
  }

  private String getHubLocation(UUID hubId) {
    return hubClient.getHub(hubId).getBody().getAddress();
  }

  private String createOrderPrompt(OrderInternalResponse orderResponse,
      UserResponseDto userResponse) {
    StringBuilder message = new StringBuilder();

    message.append("주문자 정보 : ")
        .append(orderResponse.getUserName()).append(" / ")
        .append(orderResponse.getEmail()).append("\n");

    message.append("상품 정보 : ")
        .append(orderResponse.getOrderItemsRequests().stream()
            .map(item -> "상품: " + item.getName() + " / 수량: " + item.getStock())
            .collect(Collectors.joining(", ")))
        .append("\n");

    message.append("요청 사항 : ").append(orderResponse.getRequestMessage()).append("\n");

    message.append("발송지 : ").append(getHubLocation(orderResponse.getDepartHubId())).append("\n");
    message.append("도착지 : ").append(getHubLocation(orderResponse.getArriveHubId())).append("\n");

    message.append("배송담당자 : ").append(userResponse.getUsername()).append("\n");

    return message.toString();
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
