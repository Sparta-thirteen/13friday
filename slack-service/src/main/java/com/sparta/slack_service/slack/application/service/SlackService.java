package com.sparta.slack_service.slack.application.service;

import com.sparta.slack_service.slack.domain.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

  private final SlackRepository slackRepository;
}
