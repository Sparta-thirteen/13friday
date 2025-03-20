package com.sparta.slack_service.slack.presentation.controller;

import com.sparta.slack_service.slack.application.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

  private final SlackService slackService;
}
