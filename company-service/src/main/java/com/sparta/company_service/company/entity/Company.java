package com.sparta.company_service.company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_company")
public class Company {

  @Id
  @UuidGenerator
  @Column(name = "company_id")
  private UUID id;

  private UUID hubId;

  @Column(nullable = false)
  private CompanyType type;

  @Column(nullable = false)
  private String name;

  private String address;
}
