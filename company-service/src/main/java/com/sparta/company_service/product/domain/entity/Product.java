package com.sparta.company_service.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "p_product")
public class Product {

  @Id
  @UuidGenerator
  @Column(name = "product_id")
  private UUID id;

  private UUID companyId;

  private UUID hubId;

  @Column(nullable = false)
  private String name;
}
