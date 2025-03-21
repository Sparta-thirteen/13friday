package com.sparta.slack_service.slack.domain.repository;

import com.sparta.slack_service.slack.domain.entity.Slacks;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackRepository extends JpaRepository<Slacks, UUID> {

  @Query("SELECT s FROM Slacks s WHERE s.message LIKE %:keyword% AND s.deletedAt IS NULL " +
      "ORDER BY s.createdAt DESC, s.updatedAt DESC")
  Page<Slacks> findAllByMessage(String keyword, Pageable pageable);

  Page<Slacks> findAllByDeletedAtIsNull(Pageable pageable);
}
