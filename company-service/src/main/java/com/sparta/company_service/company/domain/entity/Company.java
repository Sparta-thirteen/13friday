package com.sparta.company_service.company.domain.entity;

import com.sparta.company_service.common.global.TimeStamped;
import com.sparta.company_service.company.application.dto.CompanyRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_company")
public class Company extends TimeStamped {

  @Id
  @UuidGenerator
  @Column(name = "company_id")
  private UUID id;

  private UUID hubId;

  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompanyType type;

  @Column(nullable = false)
  private String name;

  private String address;

  public void update(CompanyRequestDto requestDto) {
    this.hubId = requestDto.getHubId();
    this.type = requestDto.getType();
    this.name = requestDto.getName();
    this.address = requestDto.getAddress();
  }

  public void updateUser(String userId) {
    this.userId = Long.parseLong(userId);
  }
}
