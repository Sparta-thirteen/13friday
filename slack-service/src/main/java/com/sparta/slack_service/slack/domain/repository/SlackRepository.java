package com.sparta.slack_service.slack.domain.repository;

import com.sparta.slack_service.slack.domain.entity.Slack;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackRepository extends JpaRepository<Slack, UUID> {

}
