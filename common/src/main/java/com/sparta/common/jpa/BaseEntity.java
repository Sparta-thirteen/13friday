package com.sparta.common.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  @CurrentTimestamp
  @Comment("생성 일시")
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", length = 100, updatable = false)
  @Comment("생성자")
  private String createdBy;

  @LastModifiedDate
  @Column(name = "updated_at", insertable = false)
  @CurrentTimestamp
  @Comment("수정 일시")
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by", length = 100, insertable = false)
  @Comment("수정자")
  private String updatedBy;

  @Column(name = "deleted_at", insertable = false)
  @Comment("삭제 일시")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by", length = 100, insertable = false)
  @Comment("삭제자")
  private String deletedBy;

  public void delete(String username) {
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = username;
  }
}
