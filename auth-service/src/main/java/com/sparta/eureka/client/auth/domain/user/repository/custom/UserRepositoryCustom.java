package com.sparta.eureka.client.auth.domain.user.repository.custom;

import com.sparta.eureka.client.auth.domain.user.entity.User;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom {
  Page<User> findUsers(
      String username,
      String slackId,
      String role,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable);
}
