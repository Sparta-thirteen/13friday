package com.sparta.eureka.hub.infrastructure.queryDSL;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.eureka.hub.domain.entity.Hub;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.eureka.hub.domain.entity.QHub.hub;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Hub> searchByKeyword(String keyword,boolean isDesc, Pageable pageable) {
        JPAQuery<Hub> query = queryFactory
                .selectFrom(hub)
                .where(
                        hub.hubName.likeIgnoreCase("%" + keyword + "%")
                )
                .orderBy(getOrderSpecifier(isDesc))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Hub> hubs = query.fetch();

        long total = query.fetchCount();
        System.out.println("Query: " + query);
        return new PageImpl<>(hubs, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(boolean isDesc) {
        return isDesc ? hub.createdAt.desc() : hub.createdAt.asc();
    }
}
