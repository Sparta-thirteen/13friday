package com.sparta.eureka.client.auth.domain.shippingmanager.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.QShippingManager;
import com.sparta.eureka.client.auth.domain.shippingmanager.enitity.ShippingManager;
import com.sparta.eureka.client.auth.domain.user.entity.Role;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShippingManagerRepositoryCustomImpl implements ShippingManagerRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  QShippingManager shippingManager = QShippingManager.shippingManager;

  @Override
  public Page<ShippingManager> findShippingManagers(UUID shippingManagerId, UUID hubId,
      LocalDate startDate, LocalDate endDate, Pageable pageable) {
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(shippingManager.deletedBy.isNull());

    // shippingManagerId 검색
    if (shippingManagerId != null) {
      builder.and(shippingManager.id.eq(shippingManagerId));
    }

    // hubId 검색
    if (hubId != null) {
      builder.and(shippingManager.hubId.eq(hubId));
    }

    // 가입 날짜 범위 검색
    if (startDate != null) {
      builder.and(shippingManager.createdAt.goe(startDate.atStartOfDay()));
    }
    if (endDate != null) {
      builder.and(shippingManager.createdAt.loe(endDate.atTime(23, 59, 59)));
    }

    // 정렬 기준 설정
    OrderSpecifier<?> orderSpecifier;
    if (pageable.getSort().isSorted()) {
      orderSpecifier = pageable.getSort().stream()
          .findFirst()
          .map(order -> {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            return (direction == Order.ASC) ? shippingManager.createdAt.asc() : shippingManager.createdAt.desc();
          })
          .orElse(shippingManager.createdAt.desc());
    } else {
      orderSpecifier = shippingManager.createdAt.desc();
    }

    // 페이징 적용
    List<ShippingManager> content =
        jpaQueryFactory
            .selectFrom(shippingManager)
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    // 총 개수 조회
    long total = jpaQueryFactory.select(shippingManager.count()).from(shippingManager).where(builder).fetchOne();

    return new PageImpl<>(content, pageable, total);
  }
}
