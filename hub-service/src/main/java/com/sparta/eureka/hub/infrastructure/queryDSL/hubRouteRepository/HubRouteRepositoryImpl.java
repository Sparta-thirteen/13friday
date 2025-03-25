package com.sparta.eureka.hub.infrastructure.queryDSL.hubRouteRepository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.eureka.hub.domain.entity.HubRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.eureka.hub.domain.entity.QHub.hub;
import static com.sparta.eureka.hub.domain.entity.QHubRoute.hubRoute;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryImpl implements HubRouteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HubRoute> searchHubRoutes(String keyword, boolean isDesc, Pageable pageable) {
        int size = pageable.getPageSize();
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        JPAQuery<HubRoute> query = queryFactory
                .selectFrom(hubRoute)
                .join(hubRoute.departHub, hub)
                .join(hubRoute.arriveHub, hub)
                .where(
                        hub.hubName.likeIgnoreCase("%" + keyword + "%"),
                        hubRoute.isDeleted.isFalse()
                )
                .orderBy(getOrderSpecifier(isDesc))
                .offset(pageable.getOffset())
                .limit(size);

        List<HubRoute> hubRoutes = query.fetch();

        long total = query.fetchCount();

        return new PageImpl<>(hubRoutes, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(boolean isDesc) {
        return isDesc ? hub.createdAt.desc() : hub.createdAt.asc();
    }
}
