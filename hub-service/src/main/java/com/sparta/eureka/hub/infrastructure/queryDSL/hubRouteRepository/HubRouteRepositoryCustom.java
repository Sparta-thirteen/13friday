package com.sparta.eureka.hub.infrastructure.queryDSL.hubRouteRepository;

import com.sparta.eureka.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteRepositoryCustom {
    Page<HubRoute> searchHubRoutes(String keyword, boolean isDesc, Pageable pageable);
}
