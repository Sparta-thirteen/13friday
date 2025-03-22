package com.sparta.eureka.client.auth.domain.shippingmanager.enitity;

import com.sparta.eureka.client.auth.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_shippingManager")
@Comment("배송담당자")
public class ShippingManager extends BaseEntity {
  @Id
  @GeneratedValue
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "shippingManager_id", updatable = false, nullable = false)
  private UUID id;

  @NotNull
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "hub_id", nullable = true)
  private UUID hubId;

  @NotNull
  @Column(name = "slack_id", nullable = false)
  @Comment("슬랙ID")
  private String slackId;

  @NotNull
  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("배송담당자타입")
  private ShippingManagerType type;

  @NotNull
  @Column(name = "delivery_order", nullable = false)
  private int deliveryOrder;

  //todo:hubId값 집어 넣을 수 있어야 함
  public static ShippingManagerBuilder builder(
      Long userId,
      String slackId,
      UUID hubId,
      ShippingManagerType type,
      int deliveryOrder) {
    return ShippingManager.innerBuilder()
        .userId(userId)
        .slackId(slackId)
        .hubId(hubId)
        .type(type)
        .deliveryOrder(deliveryOrder);
  }
}
