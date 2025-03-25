package com.sparta.eureka.client.auth.domain.user.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.eureka.client.auth.domain.user.entity.QUser;
import com.sparta.eureka.client.auth.domain.user.entity.Role;
import com.sparta.eureka.client.auth.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

  private final JPAQueryFactory japQueryFactory;

  QUser user = QUser.user;

  @Override
  public Page<User> findUsers(
      String username,
      String slackId,
      String role,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable) {

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(user.deletedBy.isNull());

    // username 검색
    if (username != null && !username.isBlank()) {
      builder.and(user.username.containsIgnoreCase(username));
    }

    // slackId 검색
    if (slackId != null && !slackId.isBlank()) {
      builder.and(user.slackId.containsIgnoreCase(slackId));
    }

    // role 필터링 (필요하면 추가)
    if (role != null) {
      builder.and(user.role.eq(Role.valueOf(role.toUpperCase())));
    }

    // 가입 날짜 범위 검색
    if (startDate != null) {
      builder.and(user.createdAt.goe(startDate.atStartOfDay()));
    }
    if (endDate != null) {
      builder.and(user.createdAt.loe(endDate.atTime(23, 59, 59)));
    }

    // 정렬 기준 설정
    OrderSpecifier<?> orderSpecifier;
    if (pageable.getSort().isSorted()) {
      orderSpecifier =
          pageable.getSort().stream()
              .findFirst()
              .map(
                  order -> {
                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    return switch (order.getProperty()) {
                      case "username" ->
                          (direction == Order.ASC) ? user.username.asc() : user.username.desc();
                      default ->
                          (direction == Order.ASC) ? user.createdAt.asc() : user.createdAt.desc();
                    };
                  })
              .orElse(user.createdAt.desc());
    } else {
      orderSpecifier = user.createdAt.desc();
    }

    // 페이징 적용
    List<User> content =
        japQueryFactory
            .selectFrom(user)
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    // 총 개수 조회
    long total = japQueryFactory.select(user.count()).from(user).where(builder).fetchOne();

    return new PageImpl<>(content, pageable, total);
  }
}
